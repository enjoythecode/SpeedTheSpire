package swapthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import communicationmod.EndOfTurnAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;

public class CardCrawlerGamePatches {
    private static final Logger logger = LogManager.getLogger(  CardCrawlerGamePatches.class.getName());

    @SpirePatch(
            clz= AbstractCreature.class,
            paramtypez = {},
            method="refreshHitboxLocation"
    )
    public static class PreventRefreshHitboxLocation {
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }

    }
}