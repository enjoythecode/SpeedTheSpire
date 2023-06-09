/*
 * Bunch of patches to make the game immediately go back to the start menu when
 * it is over, either via death or victory 
 * 
 * TODO: Also do the same on Victory!
 */

package swapthespire.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import communicationmod.EndOfTurnAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.map.MapRoomNode;

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;

import ludicrousspeed.simulator.patches.ScreenPatches;

/* We set the animation timers to small amounts because setting them to 0 seems to totally override the ability to skip actually do the logic needed*/
public class ImmediateAnimationPatches {
    private static final Logger logger = LogManager.getLogger(  ImmediateAnimationPatches.class.getName());
/*
    @SpirePatch(
            clz= DeathScreen.class,
            method="update"
    )
    public static class ZeroDeathScreenTimersOnUpdate {
        public static SpireReturn Prefix(DeathScreen _instance) {
            
            // Set timers to 0.001F to hopefully trick the game into thinking that the animation is over immediately
            // This should allow communication mod to, for example, truly "proceed" at high speeds.
            logger.info("we dead'ed, starting over immediately");

            ReflectionHacks.setPrivate(_instance, DeathScreen.class, "deathAnimWaitTimer", 0.001F);
            ReflectionHacks.setPrivateFinal(_instance, DeathScreen.class, "DEATH_TEXT_TIME", 0.001F);
            ReflectionHacks.setPrivate(_instance, DeathScreen.class, "deathTextTimer", 0.001F);
            
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz= MapRoomNode.class,
            method="update"
    )
    public static class ZeroScreenTimersOnUpdateForMapRoomNode {
        public static SpireReturn Prefix(MapRoomNode _instance) {
            
            // Set timers to 0.001F to hopefully trick the game into thinking that the animation is over immediately
            // This should allow communication mod to, for example, truly "proceed" at high speeds.
            ReflectionHacks.setPrivate(_instance, MapRoomNode.class, "animWaitTimer", 0.001F);
            
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz= CardCrawlGame.class,
            method="updateFade"
    )
    public static class ZeroScreenTimersOnUpdateFadeForCardCrawlGame {
        public static SpireReturn Prefix(CardCrawlGame _instance) {
            
            // Set timers to 0.001F to hopefully trick the game into thinking that the animation is over immediately
            // This should allow communication mod to, for example, truly "proceed" at high speeds.
            ReflectionHacks.setPrivate(_instance, CardCrawlGame.class, "screenTime", 0.001F);
            ReflectionHacks.setPrivate(_instance, CardCrawlGame.class, "screenTimer", 0.001F);
            
            return SpireReturn.Continue();
        }
    }
*/
    @SpirePatch(
            clz= ScreenPatches.DisableDeathScreenpatch.class,
            paramtypez = {DeathScreen.class, MonsterGroup.class},
            method="Prefix"
    )
    
    public static class PreventLudicrousFromStartingOverOnDeath {
        public static SpireReturn Prefix(DeathScreen _instance, MonsterGroup monsterGroup) {
            logger.info("I am patching a patch. What has my life come to?");
            return SpireReturn.Return(SpireReturn.Continue());
        }

    }
    
    /*
    @SpirePatch(
            clz= DeathScreen.class,
            method="update"
    )
    public static class DontUpdateDeathScreen {
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }
    */
    
}