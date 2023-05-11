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

    private ArrayList<String> characters;
    private int character_i;

    public InJavaCommunicationController(AgentI agent){
                    logger.debug(" ijcc init");
        CommunicationMod.subscribeToGameStates(this);
        this.agent = agent;
        this.characters = new ArrayList<String>();
        this.characters.add("ironclad");
        this.characters.add("defect");
        this.character_i = 0;
    }

    // Interface: CommunicationModStateReceiverI
    public void receiveGameState(String gameState) {
        this.receiveAndRespond(gameState);
    }

    public void send(String command){
        CommunicationMod.executeMessage(command);
    }

    /*
     * Receives a game state in JSON string format, and returns the next action as a string.
     */
    public void receiveAndRespond(String gameState) {
        logger.info("Received game state. ");

        if(JsonParser.parseString(gameState).getAsJsonObject().has("error")){
            logger.error("CommMod reports an error! " + gameState);
            return;
        }
        
        GameState gs = new GameState(gameState);

        if(gs.availableCommands.contains("start")){
            this.character_i += 1;
            if (this.character_i >= this.characters.size()){
                this.character_i = 0;
            }
            String character = characters.get(this.character_i);

            send("start " + character);
            return;
        }

        

        if (this.agent == null){
            logger.error("InJavaCommunicationController can't work without an agent!");
        }
        
        String response = this.agent.receiveAndRespond(gs);

        send(response);
    }

}