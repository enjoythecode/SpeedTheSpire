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
import ludicrousspeed.simulator.commands.HandSelectConfirmCommand;

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

    // The following would be in the same nature as the above and below patches (redirect to Ludicrous for these commands
    // if they are encountered in CommunicationMod.ChoiceScreenUtils while Ludicrous is active). However, some of them might
    // not be necessary, and I would rather not make code changes until I have a test case for them, so I am not going to 
    // implement them right away.
    // TODO: HAND SELECT COMMAND
    // TODO: GRID SELECT COMMAND
    // TODO: GRID SELECT CONFIRM COMMAND

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
