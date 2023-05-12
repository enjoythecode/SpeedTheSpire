package swapthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import swapthespire.SwapTheSpire;

public class RemoveTextureAtlasPatch {
    private static final Logger logger = LogManager.getLogger(RemoveTextureAtlasPatch.class.getName());

    @SpirePatch(
            clz= TextureAtlas.class,
            paramtypez = {String.class},
            method="findRegion"
    )
    public static class MakeTextureAtlasFindRegionAlwaysReturnNull {
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
            clz= ImageMaster.class,
            paramtypez = {String.class},
            method="loadImage"
    )
    public static class MakeImageMasterLoadImageAlwaysReturnNull {
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }

    @SpirePatch(
            clz= ImageMaster.class,
            paramtypez = {String.class, boolean.class},
            method="loadImage"
    )
    public static class MakeImageMasterLoadImageAlwaysReturnNull2 {
        public static SpireReturn Prefix() {
            return SpireReturn.Return(null);
        }
    }
}