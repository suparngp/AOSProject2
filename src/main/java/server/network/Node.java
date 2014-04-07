package server.network;

import common.utils.Logger;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 * Represents the server node
 * Created by suparngupta on 4/5/14.
 */
public class Node extends Thread{
    private int nodeId;
    private String hostName;
    private int portNum;
    private ServerToServerChannel serverToServerChannel;

    /**
     * Creates a new node
     * @param nodeId the server's id 0-6
     * @param hostName the host name
     * @param portNum the port number
     */
    public Node(int nodeId, String hostName, int portNum) {
        this.nodeId = nodeId;
        this.hostName = hostName;
        this.portNum = portNum;
        this.serverToServerChannel = new ServerToServerChannel(this.portNum);
        Logger.log("Created Node ", this);
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
     * Sets new serverToServerChannel.
     *
     * @param serverToServerChannel New value of serverToServerChannel.
     */
    public void setServerToServerChannel(ServerToServerChannel serverToServerChannel) {
        this.serverToServerChannel = serverToServerChannel;
    }

    /**
     * Gets serverToServerChannel.
     *
     * @return Value of serverToServerChannel.
     */
    public ServerToServerChannel getServerToServerChannel() {
        return serverToServerChannel;
    }

    /**
     * Gets portNum.
     *
     * @return Value of portNum.
     */
    public int getPortNum() {
        return portNum;
    }

    /**
     * Sets new portNum.
     *
     * @param portNum New value of portNum.
     */
    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

    /**
     * Sets new hostName.
     *
     * @param hostName New value of hostName.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * Gets hostName.
     *
     * @return Value of hostName.
     */
    public String getHostName() {
        return hostName;
    }


    /**
     * Sends a message to a node with the specified host name on the given port number
     * @param hostName the remote host's name, ip address
     * @param portNum the port number
     * @param message the message
     * @throws Exception
     */
    public void sendMessage(String hostName, int portNum, String message) throws Exception{
        Socket socket = new Socket(hostName, portNum);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(message);
        dos.flush();
    }

    /**
     * Broadcasts the message tp the given hosts-ports map.
     * @param hostPortMap the host port map hostName <-> portNum
     * @param message the message
     * @throws Exception
     */
    public void broadcastMessage(HashMap<String, Integer> hostPortMap, String message) throws Exception{
        for(String host: hostPortMap.keySet()){
            if(host.equals(this.hostName)){
                continue;
            }
            int port = hostPortMap.get(host);
            sendMessage(host, port, message);
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeId=" + nodeId +
                ", hostName='" + hostName + '\'' +
                ", portNum=" + portNum + '}';
    }
}
