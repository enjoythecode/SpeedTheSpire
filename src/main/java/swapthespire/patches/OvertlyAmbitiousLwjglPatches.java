/*
 *  EXPERIMENTAL patches to see how much I can hack Lwjgl to be as lean as possible.
 */ 

package ludicrousspeed.simulator.patches;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.audio.OpenALAudio;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.desktop.DesktopLauncher;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.audio.MusicMaster;
import com.megacrit.cardcrawl.cards.AbstractCard;
import org.lwjgl.opengl.Display;

public class OvertlyAmbitiousLwjglPatches{

    @SpirePatch(
        clz = Display.class,
        paramtypez={boolean.class},
        method="update"
    )
    public static class YeetTheDisplayUpdate {
        @SpirePrefixPatch
        public static SpireReturn Prefix(boolean dontCareDidntAskPlusLPlusRatio) {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
        clz = Display.class,
        method="processMessages"
    )
    public static class YeetProcessMessages {
        @SpirePrefixPatch
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
        clz = OpenALAudio.class,
        method="update"
    )
    public static class YeetAudioUpdate {
        @SpirePrefixPatch
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
        clz = SoundMaster.class,
        method="update"
    )
    public static class YeetSoundMasterUpdate {
        @SpirePrefixPatch
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
        clz = MusicMaster.class,
        method="update"
    )
    public static class YeetMusicMasterUpdate {
        @SpirePrefixPatch
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
        clz = AbstractCard.class,
        method="update"
    )
    public static class YeetAbstractCardUpdate {
        @SpirePrefixPatch
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }
    

    @SpirePatch(
        clz = Display.class,
        paramtypez={int.class},
        method="sync"
    )
    public static class YeetTheDisplaySync {
        @SpirePrefixPatch
        public static SpireReturn Prefix(int fpsThroughTheRoofBaby) {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(clz = DesktopLauncher.class, method = "loadSettings")
    public static class OverclockTheConfiguration {
        @SpirePrefixPatch
        public static SpireReturn Prefix(LwjglApplicationConfiguration config) {

            // overclock the FPS
            config.foregroundFPS = 240;
            config.backgroundFPS = 240;

            // Make the window smaller
            // Code from LudicrousSpeed.simulator.patches.FXPatches.MessWithOutputPatch
            config.fullscreen = false;
            config.height = 800;
            config.width = 800;
            return SpireReturn.Return(null);
        }
    }
    @SpirePatch(clz = CardCrawlGame.class, method="render")
    public static class YeetRender {
        @SpirePrefixPatch
        public static SpireReturn Prefix(CardCrawlGame _instance) {
            _instance.update();
            return SpireReturn.Return(null);
        }
    }
/*
    @SpirePatch(clz = SpriteBatch.class, method="flush")
    public static class YeetFlush {
        @SpirePrefixPatch
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }
  */  
}