package client.forms;

/**
 * Created by suparngupta on 4/7/14.
 */
public class Utils {

    public static int computeObjectHash(String objectId){

        int serverId = 0;
        //computes the hash

        int sum = 0;
        for(int i = 0; i < objectId.length(); i++){
            sum += (int)objectId.charAt(i);
        }

        serverId = (sum / 4) % 7;
        return serverId;
    }
}
