package shard.user;
import shard.object.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Conversation static class facilitates dialogue between a Memory (Guest)
 * and the user.
 *
 * @author Michael Bianconi
 * @author https://www.github.com/Michael-Bianconi
 * @version 1
 * @since 14 October 2018
 */
public class Conversation {

    private static final ArrayList<String> questions;

    static {
        questions = new ArrayList<String>();
        questions.add("What was the last thing you saw?");
        questions.add("Can you tell me everything you've seen so far?");
        questions.add("Well, I'll let you go now, thanks.");
    }

    private Conversation() {   }

    public static void converse(Memory memory) {
        Scanner in = new Scanner(System.in);

        int selectedQuestion = 0;
        while(selectedQuestion != 2) {
            printQuestions();
            System.out.print(OutputType.USER_INPUT_REQUESTED.prefix());
            selectedQuestion = in.nextInt();
            respond(selectedQuestion, memory);
        }

    }

    private static void printQuestions() {
        for (int i = 0; i < questions.size(); i++)
        {
            System.out.println(
                OutputManager.format(OutputType.CONVERSATION_QUESTION,
                                     i + " " + questions.get(i))
            );
        }
    }

    private static void respond(int index, Memory memory) {

        switch(index) {
            case 0:
                System.out.println(
                    OutputManager.format(OutputType.CONVERSATION_RESPONSE,
                                     memory.getLatestMemory())
                );
                break;
            case 1:
                for (String m : memory.getMemories()) {
                    System.out.println(
                        OutputManager.format(OutputType.CONVERSATION_RESPONSE,
                                             m)
                    );
                }
                break;
            case 2:
                break;
            default:
                System.out.println(
                    OutputManager.format(OutputType.CONVERSATION_RESPONSE,
                                        "I'm not sure what you're saying.")
                );
        }
        return;
    }
}
