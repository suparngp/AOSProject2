package server.network;

import common.messages.Account;
import common.utils.Logger;
import server.data.DataAccess;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents the server node
 * Created by suparngupta on 4/5/14.
 */
public class Node extends Thread{
    private int nodeId;
    private String hostName;
    private int serverPortNum;
    private int clientPortNum;
    private ServerToServerChannel serverToServerChannel;
    private ServerToClientChannel serverToClientChannel;
    private HashSet<String> lockedObjects;
    private boolean inProcess;
    private DataAccess dataAccess;
    /**
     * Creates a new node
     * @param nodeId the server's id 0-6
     * @param hostName the host name
     * @param serverPortNum the port number
     */
    public Node(int nodeId, String hostName, int serverPortNum, int clientPortNum) {
        this.nodeId = nodeId;
        this.hostName = hostName;
        this.serverPortNum = serverPortNum;
        this.clientPortNum = clientPortNum;
        this.serverToServerChannel = new ServerToServerChannel(this);
        this.serverToClientChannel = new ServerToClientChannel(this);
        this.lockedObjects = new HashSet<>();
        this.inProcess = false;
        this.dataAccess = new DataAccess();
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
     * Gets serverPortNum.
     *
     * @return Value of serverPortNum.
     */
    public int getServerPortNum() {
        return serverPortNum;
    }

    /**
     * Sets new serverPortNum.
     *
     * @param serverPortNum New value of serverPortNum.
     */
    public void setServerPortNum(int serverPortNum) {
        this.serverPortNum = serverPortNum;
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
     * @param hostPortMap the host port map hostName <-> serverPortNum
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
                ", serverPortNum=" + serverPortNum + '}';
    }

    /**
     * Gets serverToClientChannel.
     *
     * @return Value of serverToClientChannel.
     */
    public ServerToClientChannel getServerToClientChannel() {
        return serverToClientChannel;
    }

    /**
     * Sets new serverToClientChannel.
     *
     * @param serverToClientChannel New value of serverToClientChannel.
     */
    public void setServerToClientChannel(ServerToClientChannel serverToClientChannel) {
        this.serverToClientChannel = serverToClientChannel;
    }

    /**
     * Gets clientPortNum.
     *
     * @return Value of clientPortNum.
     */
    public int getClientPortNum() {
        return clientPortNum;
    }

    /**
     * Sets new clientPortNum.
     *
     * @param clientPortNum New value of clientPortNum.
     */
    public void setClientPortNum(int clientPortNum) {
        this.clientPortNum = clientPortNum;
    }


    /**
     * Adds the object to the locked list.
     *
     * @param id the object id
     * @return true if object is locked, else false.
     */
    public synchronized boolean lockObject (String id){
        Account acc = this.dataAccess.getAccount(id);
        if(acc == null || this.lockedObjects.contains(acc)){
            return false;
        }

        lockedObjects.add(acc.getId());
        return false;
    }

    /**
     * Sets new dataAccess.
     *
     * @param dataAccess New value of dataAccess.
     */
    public void setDataAccess(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    /**
     * Sets new inProcess.
     *
     * @param inProcess New value of inProcess.
     */
    public void setInProcess(boolean inProcess) {
        this.inProcess = inProcess;
    }

    /**
     * Gets inProcess.
     *
     * @return Value of inProcess.
     */
    public boolean isInProcess() {
        return inProcess;
    }

    /**
     * Gets dataAccess.
     *
     * @return Value of dataAccess.
     */
    public DataAccess getDataAccess() {
        return dataAccess;
    }

    /**
     * Gets lockedObjects.
     *
     * @return Value of lockedObjects.
     */
    public HashSet<String> getLockedObjects() {
        return lockedObjects;
    }

    /**
     * Sets new lockedObjects.
     *
     * @param lockedObjects New value of lockedObjects.
     */
    public void setLockedObjects(HashSet<String> lockedObjects) {
        this.lockedObjects = lockedObjects;
    }
}
