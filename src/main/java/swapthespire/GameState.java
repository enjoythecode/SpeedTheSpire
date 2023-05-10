package swapthespire;


import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
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
        for (JsonElement el : this.communicationState.getAsJsonArray("available_commands").asList()){
            this.availableCommands.add(el.getAsString());
        }
       
        if (this.inGame){
            this.gameState = this.communicationState.getAsJsonObject("game_state");
        }
        //this.potions = 
    }
    
    // Calculates all possible valid commands, including logic for playable cards,
    // targetable cards, etc.
    public ArrayList<String> getValidCommands() {
        ArrayList<String> validCommands = new ArrayList<String>();
        
        if (this.availableCommands.contains("choose")){
            for (JsonElement el : this.gameState.get("choice_list").getAsJsonArray()) {
                validCommands.add("choose " + el.getAsString());
            }
        }
        
        if (this.availableCommands.contains("end")) {
            validCommands.add("end");
        }

        if (this.availableCommands.contains("proceed")) {
            validCommands.add("proceed");
        }

        if (this.availableCommands.contains("confirm")) {
            validCommands.add("confirm");
        }

        if (this.availableCommands.contains("skip")) {
            validCommands.add("skip");
        }

        if (this.availableCommands.contains("cancel")) {
            validCommands.add("cancel");
        }

        if (this.availableCommands.contains("leave")) {
            validCommands.add("leave");
        }

        if (this.availableCommands.contains("play") && this.gameState.has("combat_state")) {
            ArrayList<Integer> targetableMonsterIndices = new ArrayList<Integer>();

            JsonArray monsters = this.gameState.getAsJsonObject("combat_state").getAsJsonArray("monsters");
            for (int i = 0; i < monsters.size(); i++){
                JsonElement monsterEl = monsters.get(i);
                JsonObject monsterObj = monsterEl.getAsJsonObject();
                int currentHp = monsterObj.getAsJsonPrimitive("current_hp").getAsInt();
                boolean halfDead = monsterObj.getAsJsonPrimitive("half_dead").getAsBoolean();
                boolean isGone = monsterObj.getAsJsonPrimitive("is_gone").getAsBoolean();
                if (currentHp > 0 && !halfDead && !isGone) {
                    targetableMonsterIndices.add(i);
                }
            }

            JsonArray hand = this.gameState.getAsJsonObject("combat_state").getAsJsonArray("hand");
            for (int i = 0; i < hand.size(); i++){
                // logic from spirecomm:
                // available_monsters = [monster for monster in self.game.monsters if monster.current_hp > 0 and not monster.half_dead and not monster.is_gone]
                JsonObject cardObj = hand.get(i).getAsJsonObject();
                boolean isPlayable = cardObj.getAsJsonPrimitive("is_playable").getAsBoolean();
                boolean hasTarget = cardObj.getAsJsonPrimitive("has_target").getAsBoolean();
                if (isPlayable) {
                    // Cards are 1-indexed, but monsters are 0-indexed!
                    // See documentation at: https://github.com/ForgottenArbiter/CommunicationMod
                    if(hasTarget) {
                        for(int target_i : targetableMonsterIndices) {
                            validCommands.add("play " + Integer.toString(i+1) + " " +  Integer.toString(target_i));
                        }
                    } else {
                        validCommands.add("play " +  Integer.toString(i+1));
                    }
                }
            }   
        }

        // TODO: potion

        return validCommands;
    }
}