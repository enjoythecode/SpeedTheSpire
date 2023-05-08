package swapthespire;

import basemod.*;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PreStartGameSubscriber;
import basemod.interfaces.PostDungeonInitializeSubscriber;
import basemod.interfaces.PreUpdateSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import savestate.SaveStateMod;

import static ludicrousspeed.LudicrousSpeedMod.controller;
import static ludicrousspeed.LudicrousSpeedMod.plaidMode;

@SpireInitializer
public class SwapTheSpire implements PostInitializeSubscriber, PostDungeonInitializeSubscriber, PreUpdateSubscriber, PreStartGameSubscriber  {
    
    private static final Logger logger = LogManager.getLogger(SwapTheSpire.class.getName());    

    private static LudicrousCommunicationController ludicCommControllerInstance;

    public static enum InControl{
        LUDICROUS_SPEED,
        COMMUNICATION_MOD
    }

    public static InControl active;

    public static boolean allowCommunicationMod() {
        return SwapTheSpire.active == InControl.COMMUNICATION_MOD;
    }
    
    public SwapTheSpire(){
        BaseMod.subscribe(this);
    }

    public static void initialize(){
        SwapTheSpire mod = new SwapTheSpire();

        System.out.println("Initialized SwapTheSpire mod");
    }
    
    public void receivePostInitialize() {
        // by default, we are out of combat, so we want CommMod in control
        active = InControl.COMMUNICATION_MOD;

        // cache this so that we don't constantly re-initialize it
        ludicCommControllerInstance = new LudicrousCommunicationController();

        // tell other mods to go fast
        SaveStateMod.shouldGoFast = true;
        plaidMode = true;

        // BattleAI (scumthespire) mod can set isDemo=true when ran in server mode
        // we always swap it back to ensure we are not in demo mode
        Settings.isDemo = false;

        // Borrowed from BattleAI logic when isServer=true
        Settings.MASTER_VOLUME = 0;

        Settings.ACTION_DUR_XFAST = 0.001F;
        Settings.ACTION_DUR_FASTER = 0.002F;
        Settings.ACTION_DUR_FAST = 0.0025F;
        Settings.ACTION_DUR_MED = 0.005F;
        Settings.ACTION_DUR_LONG = .01F;
        Settings.ACTION_DUR_XLONG = .015F;
    }

    // CommunicationMod::CommandExecutor::isInDungeon
    public static boolean isInDungeon() {
        return CardCrawlGame.mode == CardCrawlGame.GameMode.GAMEPLAY && AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.currMapNode != null;
    }

    public static InControl whoShouldBeInControl() {

        if (!SwapTheSpire.isInDungeon()) {
            return InControl.COMMUNICATION_MOD;
        }

        if (AbstractDungeon.player.currentHealth < 1) {
            return InControl.COMMUNICATION_MOD;
        }

        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            // we want to switch out of this controller when the enemies are dead
            boolean areMonstersDead = AbstractDungeon.getMonsters().areMonstersDead();
            boolean battleNotRegisteredAsOver = !AbstractDungeon.getCurrRoom().isBattleOver;
            boolean couldLoseCurrentRoom = !AbstractDungeon.getCurrRoom().cannotLose;

            if (areMonstersDead && battleNotRegisteredAsOver && couldLoseCurrentRoom){
                return InControl.COMMUNICATION_MOD;
            }
        }

        // LudicrousSpeed can (or always does) get stuck in combat with all enemies dead,
        // so we only want it on while enemies are not dead...
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().monsters != null 
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.getMonsters().areMonstersDead()) {
        
            return InControl.LUDICROUS_SPEED;

        }

        return InControl.COMMUNICATION_MOD;
    }

    public static boolean controlShouldBeInLudicrous() {
        // we also want to swap out of Ludicrous when we are dead so that CommunicationMod can 
        // (hopefully) handle that
        return SwapTheSpire.whoShouldBeInControl() == InControl.LUDICROUS_SPEED;
    }

    public static void setActive(InControl desired) {
        if (SwapTheSpire.active != desired){
            System.out.println("Setting active to " + desired.toString() + ". It was " + active.toString());
            SwapTheSpire.active = desired;
        
            // Toggle ludicrous communication controller to be active based on desired controller
            
            if (desired == InControl.LUDICROUS_SPEED){
                controller = ludicCommControllerInstance;
            } else {
                controller = null;
            }
        }
    }

    public void receivePreUpdate() {
        InControl desired = SwapTheSpire.whoShouldBeInControl();

        if (active != desired) {
            SwapTheSpire.setActive(desired);
        }
    }

    public void receivePreStartGame() {
        String seedFlag = System.getProperty("seed");
        // code imitated from megacrit.cardcrawl.helpers.SeedHelper:setSeed
        if(seedFlag != null){
            logger.info("SpeedTheSpire seed flag was set; using this seed: " + seedFlag);
            Settings.seedSet = true;
            Settings.seed = Long.valueOf(seedFlag);
            Settings.specialSeed = null;
            Settings.isDailyRun = false;
        }
        
    }

    public void receivePostDungeonInitialize() {
        logger.info("SwapTheSpire received post dungeon initialize. Seed is: " + Settings.seed.toString());
    }

}