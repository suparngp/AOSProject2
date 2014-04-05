package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the configuration parameters of the server
 * Created by suparngupta on 4/4/14.
 */
public class Globals {
    public static String dataFileName = "data.txt";
    public static int serverCount = 1;
    public static int serverChannelPort = 9999;
    public static int discoveredServers = 0;
    public static List<Integer> discoveryMessages = new ArrayList<Integer>();
}
