package swapthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import communicationmod.EndOfTurnAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;
/*
public class TreasureRoomBossScreenDebugPatch {

    private static final Logger logger = LogManager.getLogger(  TreasureRoomBossScreenDebugPatch.class.getName());
    
    @SpirePatch(
            clz= AbstractDungeon.class,
            method="closeCurrentScreen"
    )
    public static class DebugCloseCurrentScreen {
        public static SpireReturn Prefix() {
            logger.info(">>closeCurrentScreen has been called. prev:" + AbstractDungeon.previousScreen + ". curr:" + AbstractDungeon.screen);
            return SpireReturn.Continue();
        }
        public static SpireReturn Postfix() {
            logger.info("<< after closeCurrentScreen. prev:" + AbstractDungeon.previousScreen + ". curr:" + AbstractDungeon.screen);
            return SpireReturn.Continue();
        }
    }
}
*/
