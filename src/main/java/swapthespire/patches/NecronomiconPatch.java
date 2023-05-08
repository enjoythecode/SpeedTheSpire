
package swapthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Necronomicon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;

import communicationmod.EndOfTurnAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;

import ludicrousspeed.simulator.patches.ScreenPatches;

public class NecronomiconPatch {
    private static final Logger logger = LogManager.getLogger(  NecronomiconPatch.class.getName());    

    @SpirePatch(
        clz= Necronomicon.class,
        method="onEquip"
    )
    public static class HaveNecronomiconOnlyPutInCardAndDontUpdateDescription {
        public static SpireReturn Prefix() {  
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect((AbstractCard)new Necronomicurse(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            return SpireReturn.Return(null);
        }
    }
}