package swapthespire;

import swapthespire.SwapTheSpire;
import ludicrousspeed.Controller;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static communicationmod.CommunicationMod.waitForMessageAndExecute;
import static communicationmod.CommunicationMod.sendGameState;

public class LudicrousCommunicationController implements Controller {

    public LudicrousCommunicationController(){
    }

    public void step() {
        sendGameState();
        waitForMessageAndExecute();
    }

    public boolean isDone() {
        return !SwapTheSpire.controlShouldBeInLudicrous();
    }
}
