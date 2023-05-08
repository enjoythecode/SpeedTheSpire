/*
 * Bunch of patches to make the game immediately go back to the start menu when
 * it is over, either via death or victory 
 * 
 * TODO: Also do the same on Victory!
 */

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

import ludicrousspeed.simulator.patches.ScreenPatches;

public class RestartOnGameEndPatch {
    private static final Logger logger = LogManager.getLogger(  RestartOnGameEndPatch.class.getName());

    @SpirePatch(
            clz= ScreenPatches.DisableDeathScreenpatch.class,
            paramtypez = {DeathScreen.class, MonsterGroup.class},
            method="Prefix"
    )
    public static class PreventLudicrousFromStartingOverOnDeath {
        public static SpireReturn Prefix(DeathScreen _instance, MonsterGroup monsterGroup) {
            logger.info("I am patching a patch. What has my life come to?");
            return SpireReturn.Continue();
        }

    }

    @SpirePatch(
            clz= DeathScreen.class,
            paramtypez = {MonsterGroup.class},
            method=SpirePatch.CONSTRUCTOR
    )
    public static class StartOverOnDeath {
        // Modeled after screens.DeathScreen.update:666
        public static SpireReturn Prefix(DeathScreen _instance, MonsterGroup monsterGroup) {
            logger.info("we dead'ed, starting over immediately");
            Settings.isTrial = false;
            Settings.isDailyRun = false;
            Settings.isEndless = false;
            CardCrawlGame.trial = null;
            CardCrawlGame.playCreditsBgm = false;

            // reset the game, causing a restart
            CardCrawlGame.startOver();

            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
            clz= DeathScreen.class,
            method="update"
    )
    public static class DontUpdateDeathScreen {
        public static SpireReturn Prefix() {
            logger.info("skipping death screen update because we should be in main menu soon");
            return SpireReturn.Return(null);
        }
    }
    
}