
package swapthespire.patches;

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

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;

import ludicrousspeed.simulator.patches.ServerStartupPatches;

/* 
 * Patches against stuff LudicrousSpeed stuff does that I don't want it to do
 */

public class LudicrousSpeedPatches {
    private static final Logger logger = LogManager.getLogger(  RestartOnGameEndPatch.class.getName());

    @SpirePatch(
            clz= ServerStartupPatches.GameStartupPatch.class,
            paramtypez = {CardCrawlGame.class},
            method="afterStart"
    )
    public static class PreventLudicrousFromFixingRandomStateAfterStart {
        public static SpireReturn Prefix(CardCrawlGame game) {
            logger.info("SpeedTheSpire preventing LudicrousState.patches.ServerStartupPatches.GameStartupPatch.afterStart");
            return SpireReturn.Return(null);
        }

    }
}