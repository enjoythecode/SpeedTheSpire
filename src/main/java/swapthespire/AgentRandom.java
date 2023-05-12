package swapthespire;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

public class AgentRandom implements AgentI {
    private static Random random = new Random((long)42);
    private static final Logger logger = LogManager.getLogger(AgentRandom.class.getName());

    public String receiveAndRespond(GameState gs) {
        ArrayList<String> pool = gs.getValidCommands();

        if (pool.size() == 0){
            logger.warn("AgentRandom couldn't find any valid actions, so it will stall");
            logger.warn("available commands were: " + gs.availableCommands); 
            return "wait 500000";
        }

        int choice_index = random.nextInt(pool.size());
        String choice = pool.get(choice_index);
        
        logger.debug("Random selection length was: " + pool.size());
        
        return choice;            

    }
}