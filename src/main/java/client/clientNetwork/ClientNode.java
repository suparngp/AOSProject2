package client.clientNetwork;

/**
 * Created by Nimisha on 7/4/14.
 */
public class ClientNode  {
    private int clientNodeId;

    public ClientNode(int clientNodeId){
        this.clientNodeId=clientNodeId;
    }

    /**
     * Gets nodeId.
     *
     * @return Value of nodeId.
     */
    public int getNodeId() {
        return clientNodeId;
    }

    /**
     * Sets new nodeId.
     *
     * @param nodeId New value of nodeId.
     */
    public void setNodeId(int nodeId) {
        this.clientNodeId = nodeId;
    }




}
