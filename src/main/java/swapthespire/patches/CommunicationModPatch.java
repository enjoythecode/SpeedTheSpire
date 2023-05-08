package swapthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import communicationmod.EndOfTurnAction;

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;

public class CommunicationModPatch {

    @SpirePatch(
            clz= CommunicationMod.class,
            method="receivePostUpdate"
    )
    public static class PreventCommunicationModPublishGameStatePostUpdate {
        public static SpireReturn Prefix() {
            if(!SwapTheSpire.allowCommunicationMod()){
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz= CommunicationMod.class,
            method="receivePreUpdate"
    )
    public static class PreventCommunicationModReadIncomingPreUpdate {
        public static SpireReturn Prefix() {

            if(!SwapTheSpire.allowCommunicationMod()){
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
