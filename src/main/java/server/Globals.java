package server;

import common.utils.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Contains the configuration parameters of the server
 * Created by suparngupta on 4/4/14.
 */
public class Globals {
    public static String dataFileName = "data.txt";
    public static int serverCount = 2;
    public static int serverChannelPort = 9999;
    public static int discoveredServers = 0;
    public static List<Integer> discoveryMessages = new ArrayList<Integer>();

    public static HashMap<Integer, String> serverHostNames = new HashMap<>();
    public static HashMap<Integer, Integer> serverPortNums = new HashMap<>();

    public static HashMap<Integer, Integer>serverClientPortNums = new HashMap<>();

    static{
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("server.config")));
            String input = br.readLine();
            while(input != null){
                String[] tokens = input.split(",");

                if(tokens.length != 3){
                    Logger.error("Unknown server configuration file format", input);
                    System.exit(-1);
                }

                int nodeId = Integer.parseInt(tokens[0]);
                String hostName = tokens[1];
                int portNum = Integer.parseInt(tokens[2]);
                serverHostNames.put(nodeId, hostName);
                serverPortNums.put(nodeId, portNum);
                input = br.readLine();
            }

            br.close();

            br = new BufferedReader(new FileReader(new File("server-client.config")));
             input = br.readLine();
            while(input != null){
                String[] tokens = input.split(",");

                if(tokens.length != 3){
                    Logger.error("Unknown server configuration file format", input);
                    System.exit(-1);
                }

                int nodeId = Integer.parseInt(tokens[0]);
                int portNum = Integer.parseInt(tokens[2]);
                serverClientPortNums.put(nodeId, portNum);
                input = br.readLine();
            }

        }

        catch(Exception e){
            Logger.error("Unable to read the server configuration file.");
        }
    }
}
