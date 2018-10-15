package shard.ai;
import shard.event.*;
import shard.object.*;
import java.util.Random;
import java.util.ArrayList;

public class Action {

    private Action() {   }  // make Action a static class


    public static Event generateAction(Guest g) {
        ArrayList<Command> possibleActions = new ArrayList<Command>();

        if (Command.ENTER.buildCandidateList(g).size() > 0) {
            possibleActions.add(Command.ENTER);
        }

        if (Command.TAKE.buildCandidateList(g).size() > 0) {
            possibleActions.add(Command.TAKE);
        }

        if (Command.DROP.buildCandidateList(g).size() > 0) {
            possibleActions.add(Command.DROP);
        }

        if (Command.TALK_TO_PLAYER.buildCandidateList(g).size() > 0) {
            possibleActions.add(Command.TALK_TO_PLAYER);
        }

        Random rand = new Random();
        int actionChoice = rand.nextInt(possibleActions.size());
        Command action = possibleActions.get(actionChoice);

        ArrayList<ShardObject> candidates = action.buildCandidateList(g);

        int objectChoice = rand.nextInt(candidates.size());
        ShardObject object = candidates.get(objectChoice);

        return new Event(action, g, object);
    }
}