package server;

import common.utils.Logger;
import server.network.ConnectionManager;
import server.network.Node;

import java.util.Arrays;

/**
 * Created by suparngupta on 4/4/14.
 */
public class Main {

    /**
     * Read the network configuration from the file.
     * Then start sending out the server intro messages.
     * Once all the network discovery is complete, start the client sockets.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        if(args.length != 1){
            System.out.println(Arrays.toString(args));
            Logger.error("Please specify 1 argument, <nodeId>");
            throw new Exception();
        }

        int nodeId = Integer.parseInt(args[0]);
        int port = Globals.serverPortNums.get(nodeId);
        Node node = new Node(nodeId, Globals.serverHostNames.get(nodeId), port);
        ConnectionManager connectionManager = new ConnectionManager(node);
        connectionManager.initializeServerChannel();
        connectionManager.discoverNetwork();
    }
}
