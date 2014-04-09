package client.clientNetwork;

/**
 * Created by Nimisha on 7/4/14.
 */
public class ClientNode  {
    private int nodeId;
    private int objectIdCounter = 1;

    public ClientNode(int nodeId){
        this.nodeId = nodeId;
    }

    /**
     * Gets nodeId.
     *
     * @return Value of nodeId.
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * Sets new nodeId.
     *
     * @param nodeId New value of nodeId.
     */
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }


    /**
     * Generates the bject id of the new object.
     * Each object id is a combination of current system time - nodeId - and a counter value.
     * @return
     */
    public synchronized String generateObjectId(){
        if(objectIdCounter == 10){
            objectIdCounter = 1;
        }

        String objectId = "";
        objectId +=  System.currentTimeMillis() + "-" + this.nodeId + "-" + this.objectIdCounter;
        this.objectIdCounter++;
        return objectId;
    }

}
