package swapthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import communicationmod.EndOfTurnAction;
import communicationmod.ChoiceScreenUtils;

import communicationmod.CommunicationMod;
import swapthespire.SwapTheSpire;

import ludicrousspeed.simulator.commands.CardRewardSelectCommand;
import ludicrousspeed.simulator.commands.HandSelectCommand;
import ludicrousspeed.simulator.commands.HandSelectConfirmCommand;
import ludicrousspeed.simulator.commands.GridSelectCommand;
import ludicrousspeed.simulator.commands.GridSelectConfrimCommand;

public class CommunicationModPatches {

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

    @SpirePatch(
        clz=ChoiceScreenUtils.class,
        paramtypez={int.class},
        method="makeCardRewardChoice"
    )
    public static class RedirectCardRewardCommandExecutionToLudicrousIfLudicrousIsActive {
        public static SpireReturn Prefix(int choice) {
            if(!SwapTheSpire.allowCommunicationMod()){
                new CardRewardSelectCommand(choice).execute();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
        clz=ChoiceScreenUtils.class,
        paramtypez={int.class},
        method="makeGridScreenChoice"
    )
    public static class RedirectGridRewardCommandExecutionToLudicrousIfLudicrousIsActive {
        public static SpireReturn Prefix(int choice) {
            if(!SwapTheSpire.allowCommunicationMod()){
                new GridSelectCommand(choice).execute();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
        clz=ChoiceScreenUtils.class,
        method="clickGridScreenConfirmButton"
    )
    public static class RedirectGridScreenConfirmExecutionToLudicrousIfLudicrousIsActive {
        public static SpireReturn Prefix() {
            if(!SwapTheSpire.allowCommunicationMod()){
                GridSelectConfrimCommand.INSTANCE.execute(); // [typo present in LudicrousSpeed]
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
        clz=ChoiceScreenUtils.class,
        paramtypez={int.class},
        method="makeHandSelectScreenChoice"
    )
    public static class RedirectHandSelectCommandExecutionToLudicrousIfLudicrousIsActive {
        public static SpireReturn Prefix(int choice) {
            if(!SwapTheSpire.allowCommunicationMod()){
                new HandSelectCommand(choice).execute();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }


    @SpirePatch(
        clz=ChoiceScreenUtils.class,
        method="clickHandSelectScreenConfirmButton"
    )
    public static class RedirectHandSelectConfirmExecutionToLudicrousIfLudicrousIsActive {
        public static SpireReturn Prefix() {
            if(!SwapTheSpire.allowCommunicationMod()){
                HandSelectConfirmCommand.INSTANCE.execute();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
