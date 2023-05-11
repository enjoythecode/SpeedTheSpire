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

import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.CharacterManager;
import com.megacrit.cardcrawl.daily.TimeHelper;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.helpers.AsyncSaver;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.DrawMaster;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.GameTips;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.helpers.Prefs;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.helpers.TrialHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.DevInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.helpers.steamInput.SteamInputHelper;
import com.megacrit.cardcrawl.integrations.DistributorFactory;
import com.megacrit.cardcrawl.integrations.PublisherIntegration;
import com.megacrit.cardcrawl.integrations.SteelSeries;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.metrics.BotDataUploader;
import com.megacrit.cardcrawl.metrics.MetricData;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.screens.DoorUnlockScreen;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.splash.SplashScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.trials.AbstractTrial;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import com.megacrit.cardcrawl.ui.panels.SeedPanel;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import com.megacrit.cardcrawl.map.MapRoomNode;

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;

import ludicrousspeed.simulator.patches.ScreenPatches;

/* We set the animation timers to small amounts because setting them to 0 seems to totally override the ability to skip actually do the logic needed*/
public class StartOverOnDeathScreenPatch {
    private static final Logger logger = LogManager.getLogger(  StartOverOnDeathScreenPatch.class.getName());

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
            // CardCrawlGame.startOver() would be called here, which sets fadeOut() at 2, but I don't want to wait,
            // so I am grabbing the actual start-over logic here, from CardCrawlGame.class:updateFade [line 517]
            if (AbstractDungeon.scene != null){
                AbstractDungeon.scene.fadeOutAmbiance();
            }
            long startTime = System.currentTimeMillis();
            AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
            AbstractDungeon.reset();
            FontHelper.cardTitleFont.getData().setScale(1.0F);
            AbstractRelic.relicPage = 0;
            SeedPanel.textField = "";
            ModHelper.setModsFalse();
            SeedHelper.cachedSeed = null;
            Settings.seed = null;
            Settings.seedSet = false;
            Settings.specialSeed = null;
            Settings.isTrial = false;
            Settings.isDailyRun = false;
            Settings.isEndless = false;
            Settings.isFinalActAvailable = false;
            Settings.hasRubyKey = false;
            Settings.hasEmeraldKey = false;
            Settings.hasSapphireKey = false;
            CustomModeScreen.finalActAvailable = false;
            CardCrawlGame.trial = null;
            logger.info("Dungeon Reset: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            ShopScreen.resetPurgeCost();
            CardCrawlGame.tips.initialize();
            CardCrawlGame.metricData.clearData();
            logger.info("Shop Screen Rest, Tips Initialize, Metric Data Clear: " + (
            System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            UnlockTracker.refresh();
            logger.info("Unlock Tracker Refresh:  " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            CardCrawlGame.mainMenuScreen = new MainMenuScreen();
            CardCrawlGame.mainMenuScreen.bg.slideDownInstantly();
            CardCrawlGame.saveSlotPref.putFloat(
            SaveHelper.slotName("COMPLETION", CardCrawlGame.saveSlot), 
            UnlockTracker.getCompletionPercentage());
            CardCrawlGame.saveSlotPref.putLong(SaveHelper.slotName("PLAYTIME", CardCrawlGame.saveSlot), UnlockTracker.getTotalPlaytime());
            CardCrawlGame.saveSlotPref.flush();
            logger.info("New Main Menu Screen: " + (System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();
            CardHelper.clear();
            CardCrawlGame.mode = CardCrawlGame.GameMode.CHAR_SELECT;
            CardCrawlGame.nextDungeon = "Exordium";
            CardCrawlGame.dungeonTransitionScreen = new DungeonTransitionScreen("Exordium");
            TipTracker.refresh();
            System.gc();
            logger.info("New Transition Screen, Tip Tracker Refresh: " + (
            System.currentTimeMillis() - startTime) + "ms");
            startTime = System.currentTimeMillis();

            return SpireReturn.Return(null);
        }
    }
}