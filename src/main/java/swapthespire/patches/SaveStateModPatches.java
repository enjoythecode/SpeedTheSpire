package swapthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import savestate.SaveStateMod;

import communicationmod.EndOfTurnAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;

import ludicrousspeed.simulator.patches.ScreenPatches;

public class SaveStateModPatches {
    private static final Logger logger = LogManager.getLogger(SaveStateModPatches.class.getName());    

    @SpirePatch(
        clz= SaveStateMod.class,
        paramtypez={AbstractRoom.class},
        method="receiveOnBattleStart"
    )
    // savestate.SaveStateMod.receiveOnBattleStart has some println()s that I don't like
    public static class SaveStateModTooTalkativePatch {
        @SpirePrefixPatch
        public static SpireReturn Prefix(SaveStateMod _instance, AbstractRoom room) {  
            SaveStateMod.saveStateController.initialize(); // this line from the original method seemed like it is functional, so keeping it just in case...
            return SpireReturn.Return(null);
        }
    }
}
