package client;

import client.clientNetwork.ClientNode;
import common.utils.Logger;

import java.util.Arrays;

/**
 * Created by suparngupta on 4/6/14.
 */
public class Main {
    public static void main(String args[])throws Exception
    {
        if(args.length != 1){
            System.out.println(Arrays.toString(args));
            Logger.error("Please specify 1 argument, <nodeId>");
            throw new Exception();
        }

        int nodeId = Integer.parseInt(args[0]);
        ClientNode node = new ClientNode(nodeId);
        InputProcessor inputProcessor = new InputProcessor(node);
        inputProcessor.displayWelcomeScreen();
        inputProcessor.getMainMenuInput();
    }
}
