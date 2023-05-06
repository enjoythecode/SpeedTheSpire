package swapthespire;

import basemod.*;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PreUpdateSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

//import ludicrousspeed.LudicrousSpeedMod.controller;

@SpireInitializer
public class SwapTheSpire implements PostInitializeSubscriber, PreUpdateSubscriber {//, , PostDungeonUpdateSubscriber, PreUpdateSubscriber, OnStateChangeSubscriber {
    
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
        active = InControl.COMMUNICATION_MOD;
    }

    public static void initialize(){
        SwapTheSpire mod = new SwapTheSpire();

        System.out.println("Initialized SwapTheSpire mod");
    }
    
    public void receivePostInitialize() {
        for(int i = 0; i < 100; i++)System.out.println("hell yeah, we are in!");
    }

    private static void setActive(InControl desired) {
        System.out.println("Setting active to" + active.toString());
        SwapTheSpire.active = desired;
    
        // Toggle ludicrous communication controller to be active based on desired controller
        /*
        if (desired == InControl.LUDICROUS_SPEED){
            controller = new LudicrousCommunicationController();
        } else {
            controller = null;
        }
        */
    
    }

    public void receivePreUpdate() {

        InControl desired;
        
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().monsters != null 
                && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            desired = InControl.LUDICROUS_SPEED;
            System.out.println("desire ludicrous");
        } else {
            desired = InControl.COMMUNICATION_MOD;
            System.out.println("desire commmod");
        }
        if (active != desired) {
            SwapTheSpire.setActive(desired);
        }
    }

}