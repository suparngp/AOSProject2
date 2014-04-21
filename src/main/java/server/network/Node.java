package server.network;

import common.Globals;
import common.messages.Account;
import common.messages.HeartBeat;
import common.messages.MessageType;
import common.messages.MutationReq;
import common.utils.Logger;
import common.utils.MessageParser;
import server.data.DataAccess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents the server node
 * Created by suparngupta on 4/5/14.
 */
public class Node extends Thread {
    private int nodeId;
    private String hostName;
    private int serverPortNum;
    private int clientPortNum;
    private ServerToServerChannel serverToServerChannel;
    private ServerToClientChannel serverToClientChannel;
    private HashMap<String, Integer> lockedObjects;
    private boolean inProcess;
    private DataAccess dataAccess;
    private DataAccess preparedAccess;
    private HashSet<Account> temporaryData;
    private HashSet<Account> preparedData;
    private HashMap<String, List<MutationReq>> mutationRequestBuffer;
    private HashMap<String, List<String>> mutationWriteRequests;
    private ReentrantLock lock;

    /**
     * Creates a new node
     *
     * @param nodeId        the server's id 0-6
     * @param hostName      the host name
     * @param serverPortNum the port number
     */
    public Node(int nodeId, String hostName, int serverPortNum, int clientPortNum) {
        this.nodeId = nodeId;
        this.hostName = hostName;
        this.serverPortNum = serverPortNum;
        this.clientPortNum = clientPortNum;
        this.serverToServerChannel = new ServerToServerChannel(this);
        this.serverToClientChannel = new ServerToClientChannel(this);
        this.lockedObjects = new HashMap<>();
        this.inProcess = false;
        this.dataAccess = new DataAccess(this.nodeId + "_data.txt");
        this.temporaryData = new HashSet<>();
        this.preparedData = new HashSet<>();
        this.preparedAccess = new DataAccess(this.nodeId + "_prepared_data.txt");
        this.mutationRequestBuffer = new HashMap<>();
        this.mutationWriteRequests = new HashMap<>();
        this.lock = new ReentrantLock();
        init();
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
     * Gets serverToServerChannel.
     *
     * @return Value of serverToServerChannel.
     */
    public ServerToServerChannel getServerToServerChannel() {
        return serverToServerChannel;
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
     * Gets hostName.
     *
     * @return Value of hostName.
     */
    public String getHostName() {
        return hostName;
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
     * Sends a message to a node with the specified host name on the given port number
     *
     * @param hostName the remote host's name, ip address
     * @param portNum  the port number
     * @param message  the message
     * @throws Exception
     */
    public void sendMessage(String hostName, int portNum, String message) throws Exception {
        Socket socket = new Socket(hostName, portNum);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(message);
        dos.flush();
    }

    /**
     * Broadcasts the message tp the given hosts-ports map.
     *
     * @param hostPortMap the host port map hostName <-> serverPortNum
     * @param message     the message
     * @throws Exception
     */
    public void broadcastMessage(HashMap<String, Integer> hostPortMap, String message) throws Exception {
        for (String host : hostPortMap.keySet()) {
            if (host.equals(this.hostName)) {
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
     * @param objectId the object id
     * @return true if object is locked, else false.
     */
    public synchronized boolean lockObject(String objectId, int clientId) {
        Account acc = this.dataAccess.getAccount(objectId);
        Logger.debug("Account found", acc);
        if (acc == null
                || (this.lockedObjects.get(objectId) != null
                && this.lockedObjects.get(objectId) != clientId)) {
            return false;
        } else if (this.lockedObjects.get(objectId) == null) {
            lockedObjects.put(objectId, clientId);
            return true;
        } else if (this.lockedObjects.get(objectId) == clientId) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean freeObject(String objectId, int clientId) {
        if (this.lockedObjects.get(objectId) == null
                || this.lockedObjects.get(objectId) != clientId) {
            return false;
        }
        this.lockedObjects.remove(objectId);
        return true;
    }

    /**
     * Checks if the required object is locked by the specified client.
     *
     * @param objectId the object id
     * @param clientId the client id.
     * @return
     */
    public boolean isObjectLocked(String objectId, int clientId) {
        return !(this.lockedObjects.get(objectId) == null || this.lockedObjects.get(objectId) != clientId);
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
     * Sets new inProcess.
     *
     * @param inProcess New value of inProcess.
     */
    public void setInProcess(boolean inProcess) {
        this.inProcess = inProcess;
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
     * Sets new dataAccess.
     *
     * @param dataAccess New value of dataAccess.
     */
    public void setDataAccess(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    /**
     * Gets lockedObjects.
     *
     * @return Value of lockedObjects.
     */
    public HashMap<String, Integer> getLockedObjects() {
        return lockedObjects;
    }

    /**
     * Sets new lockedObjects.
     *
     * @param lockedObjects New value of lockedObjects.
     */
    public void setLockedObjects(HashMap<String, Integer> lockedObjects) {
        this.lockedObjects = lockedObjects;
    }

    /**
     * Gets temporaryData.
     *
     * @return Value of temporaryData.
     */
    public HashSet<Account> getTemporaryData() {
        return temporaryData;
    }

    /**
     * Sets new temporaryData.
     *
     * @param temporaryData New value of temporaryData.
     */
    public void setTemporaryData(HashSet<Account> temporaryData) {
        this.temporaryData = temporaryData;
    }

    public Account getTemporaryAccount(String objectId) {
        for (Account acc : temporaryData) {
            if (acc.getId().equals(objectId)) {
                return acc;
            }
        }
        return null;
    }

    public Account getPreparedAccount(String objectId) {
        for (Account acc : preparedData) {
            if (acc.getId().equals(objectId)) {
                return acc;
            }
        }
        return null;
    }

    /**
     * Gets preparedData.
     *
     * @return Value of preparedData.
     */
    public HashSet<Account> getPreparedData() {
        return preparedData;
    }

    /**
     * Sets new preparedData.
     *
     * @param preparedData New value of preparedData.
     */
    public void setPreparedData(HashSet<Account> preparedData) {
        this.preparedData = preparedData;
    }

    public void addToPreparedData(Account acc) throws Exception {
        this.preparedAccess.createAccount(acc);
        this.preparedData.add(acc);
    }

    public void init() {
        this.preparedData.addAll(this.preparedAccess.getAllAccounts());
    }

    /**
     * Gets preparedAccess.
     *
     * @return Value of preparedAccess.
     */
    public DataAccess getPreparedAccess() {
        return preparedAccess;
    }

    /**
     * Sets new preparedAccess.
     *
     * @param preparedAccess New value of preparedAccess.
     */
    public void setPreparedAccess(DataAccess preparedAccess) {
        this.preparedAccess = preparedAccess;
    }

    /**
     * Checks if the peer server is reachable or not.
     *
     * @param peerId
     * @return
     */
    public boolean isPeerReachable(int peerId) {
        try {
            boolean result = false;
            String toBeSent = MessageParser.createWrapper(new HeartBeat(this.nodeId, peerId), MessageType.HEARTBEAT);
            Socket socket = new Socket(Globals.serverHostNames.get(peerId), Globals.serverPortNums.get(peerId));
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            dos.writeUTF(toBeSent);
            dos.flush();

            String received = dis.readUTF();
            if(MessageParser.parseMessageJSON(received).getMessageType() == MessageType.HEARTBEAT_ECHO){
                result = true;
            }
            dis.close();
            dos.close();
            socket.close();
            return result;
        } catch (Exception e) {
            Logger.log("Peer unreachable " + peerId, e);
            return false;
        }
    }

    /**
     * Computes the primary server's id
     *
     * @param objectId the object id
     * @return the primary server's id
     */
    public synchronized int computeServerId(String objectId) {
        return 0;

//        if (objectId == null || objectId.isEmpty()) {
//            return 0;
//        }
//
//        int sum = 0;
//        for (int i = 0; i < objectId.length(); i++) {
//            sum += (int) objectId.charAt(i);
//        }
//        return sum % 7;
    }

    /**
     * Gets mutationRequestBuffer.
     *
     * @return Value of mutationRequestBuffer.
     */
    public HashMap<String, List<MutationReq>> getMutationRequestBuffer() {
        return mutationRequestBuffer;
    }

    /**
     * Sets new mutationRequestBuffer.
     *
     * @param mutationRequestBuffer New value of mutationRequestBuffer.
     */
    public void setMutationRequestBuffer(HashMap<String, List<MutationReq>> mutationRequestBuffer) {
        this.mutationRequestBuffer = mutationRequestBuffer;
    }

    /**
     * Gets mutationWriteRequests.
     *
     * @return Value of mutationWriteRequests.
     */
    public HashMap<String, List<String>> getMutationWriteRequests() {
        return mutationWriteRequests;
    }

    /**
     * Sets new mutationWriteRequests.
     *
     * @param mutationWriteRequests New value of mutationWriteRequests.
     */
    public void setMutationWriteRequests(HashMap<String, List<String>> mutationWriteRequests) {
        this.mutationWriteRequests = mutationWriteRequests;
    }

    /**
     * Assigns a serial number to the request.
     *
     * @param objectId the request id
     */
    public synchronized void addMutationWriteReq(String objectId, String requestId) {
        List<String> serialNums = this.getMutationWriteRequests().get(objectId);
        if (serialNums == null) {
            serialNums = new ArrayList<>();
            this.mutationWriteRequests.put(objectId, serialNums);
        }
        serialNums.add(requestId);
    }

    /**
     * Adds the mutation request to the buffer.
     *
     * @param mutationReq
     */
    public synchronized void addMutationReq(MutationReq mutationReq) {
        List<MutationReq> mutationReqs = mutationRequestBuffer.get(mutationReq.getRequestId());
        if (mutationReqs == null) {
            mutationReqs = new ArrayList<>();
        }
        mutationReqs.add(mutationReq);
        mutationRequestBuffer.put(mutationReq.getObjectId(), mutationReqs);
    }

    /**
     * Gets lock.
     *
     * @return Value of lock.
     */
    public ReentrantLock getLock() {
        return lock;
    }

    /**
     * Sets new lock.
     *
     * @param lock New value of lock.
     */
    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    /**
     * Performs Mutation
     *
     * @param mutationReq the mutation request
     * @return true if the mutation was success else false.
     * @throws Exception
     */
    public boolean performMutation(MutationReq mutationReq) throws Exception {
        Logger.log("Performing mutation", mutationReq);

        switch (mutationReq.getRequestType()) {

            case CREATE: {
                Account existing = dataAccess.getAccount(mutationReq.getData().getId());
                if (existing != null) {
                    return false;
                }
                dataAccess.createAccount(mutationReq.getData());
                return true;
            }

            case UPDATE: {
                Account existing = dataAccess.getAccount(mutationReq.getData().getId());
                if (existing == null) {
                    return false;
                }
                dataAccess.updateAccount(mutationReq.getData());
                return true;
            }

            case DELETE: {
                Account existing = dataAccess.getAccount(mutationReq.getData().getId());
                if (existing == null) {
                    return false;
                }
                dataAccess.removeAccount(mutationReq.getData().getId());
                return true;
            }
        }
        return true;
    }

    /**
     * Sends a message to the given server
     *
     * @param toBeSent the message to be sent.
     * @param serverId the server id
     * @return the socket used to send the message
     */
    public Socket sendMessage(String toBeSent, int serverId, boolean close) {
        String hostName = Globals.serverHostNames.get(serverId);
        int portNum = Globals.serverPortNums.get(serverId);

        Logger.debug("Sending to ", serverId);
        DataOutputStream dos;
        try {
            Socket socket = new Socket(hostName, portNum);

            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(toBeSent);
            dos.flush();
            if (close) {
                dos.close();
                socket.close();
                return null;
            }

            return socket;
        } catch (Exception e) {
            Logger.log("Unable to send message to " + serverId, e);
            return null;
        }
    }

}
