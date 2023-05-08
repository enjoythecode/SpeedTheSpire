package swapthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import communicationmod.EndOfTurnAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;

import ludicrousspeed.simulator.patches.ScreenPatches;

public class RelicPatch {
    private static final Logger logger = LogManager.getLogger(  RelicPatch.class.getName());    

    @SpirePatch(
        clz= AbstractRelic.class,
        paramtypez={AbstractPlayer.class, int.class, boolean.class},
        method="instantObtain"
    )
    public static class DontCallGetUpdatedDescriptionOnInstantObtainPatch {
        @SpireInsertPatch(loc = 255)
        public static SpireReturn Insert(AbstractRelic _instance, AbstractPlayer p, int slot, boolean callOnEquip) {  
            logger.debug("Skipping unnecesary call to getUpdatedDescription in AbstractRelic:instantObtain:255");
            if (AbstractDungeon.topPanel != null) {
                AbstractDungeon.topPanel.adjustRelicHbs();
            }
            return SpireReturn.Return(null);
        }
    }
}