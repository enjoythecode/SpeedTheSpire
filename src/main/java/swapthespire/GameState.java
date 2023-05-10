package swapthespire;


import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GameState {

    private static Gson gson = new Gson();

    public String originalString;
    public JsonObject communicationState;
    public JsonObject gameState;
    public boolean inGame;
    public ArrayList<String> availableCommands;

    public GameState(String inputString){
        this.originalString = inputString;
        this.communicationState = JsonParser.parseString(inputString).getAsJsonObject();
        this.inGame = this.communicationState.get("in_game").getAsJsonPrimitive().getAsBoolean();
        this.availableCommands = new ArrayList<String>();
        for(JsonElement el : this.communicationState.getAsJsonArray("available_commands").asList()){
            this.availableCommands.add(el.getAsString());
        }
       
        if(this.inGame){
            this.gameState = this.communicationState.getAsJsonObject("game_state");
        }
        //this.potions = 
    }
    
    // Calculates all possible valid commands, including logic for playable cards,
    // targetable cards, etc.
    public ArrayList<String> getValidCommands() {
        ArrayList<String> validCommands = new ArrayList<String>();
        
        if(this.availableCommands.contains("choose")){
            for (JsonElement el : this.gameState.get("choice_list").getAsJsonArray()) {
                validCommands.add("choose " + el.getAsString());
            }
        }
        
        // TODO: choose
        // TODO: play
        // TODO: end
        // TODO: proceed

        // TODO: potion
        // TODO: return
        return validCommands;
    }
/*
    public ArrayList<Integer> getTargetableMonsterIndices() {

    }
    */
}