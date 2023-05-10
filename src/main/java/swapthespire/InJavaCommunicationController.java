/*
 * An implementation of an API for a program that uses CommunicationMod from within Java, and therefore, does not use the
 * stdin/stdout that CommunicationMod ships out with.
 */
package swapthespire;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import communicationmod.CommunicationMod;
import communicationmod.CommunicationModStateReceiverI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class InJavaCommunicationController implements CommunicationModStateReceiverI{
    
    private static final Logger logger = LogManager.getLogger(InJavaCommunicationController.class.getName());
    private String wantToPlay = null;

    private AgentI agent;

    public InJavaCommunicationController(AgentI agent){
        CommunicationMod.subscribeToGameStates(this);
        this.agent = agent;
    }

    // Interface: CommunicationModStateReceiverI
    public void receiveGameState(String gameState) {
        this.receiveAndRespond(gameState);
    }

    public void send(String command){
        CommunicationMod.executeMessage(command);
    }

    public void playOneGameWithCharacter(String character) {
        wantToPlay = character;
    }

    /*
     * Receives a game state in JSON string format, and returns the next action as a string.
     */
    public void receiveAndRespond(String gameState) {
        logger.debug("Received game state, computing response...");
        
        GameState gs = new GameState(gameState);

        if(gs.availableCommands.contains("start") && wantToPlay != null){
            String temp = wantToPlay;
            wantToPlay = null;
            send("start " + temp);
            return;
        }

        if(gs.communicationState.has("error")) {
            logger.error("IN JAVA COMMUNICATION CONTROLLER WAS INFORMED OF AN ERROR, NOT SENDING ANYTHING");
            return;
        }

        if (this.agent == null){
            logger.error("InJavaCommunicationController can't work without an agent!");
        }
        
        String response = this.agent.receiveAndRespond(gs);

        send(response);
    }

}