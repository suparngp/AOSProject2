package server.network;

import common.Globals;
import common.messages.Account;
import common.messages.HeartBeat;
import common.messages.MessageType;
import common.messages.MutationReq;
import common.utils.Logger;
import common.utils.MessageParser;
import server.services.DataAccess;

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
    private final int nodeId;
    private final String hostName;
    private final int serverPortNum;
    private final int clientPortNum;
    private final ServerToServerChannel serverToServerChannel;
    private final ServerToClientChannel serverToClientChannel;
    private final DataAccess dataAccess;
    private final HashMap<String, List<MutationReq>> mutationRequestBuffer;
    private final HashMap<String, List<String>> mutationWriteRequests;
    private final ReentrantLock lock;
    private final HashSet<Integer> discoveredServers;
    private final HashSet<Integer> aliveServers;
    private final HashSet<Integer> blockingList;

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
        this.dataAccess = new DataAccess(this.nodeId + "_data.txt");
        this.mutationRequestBuffer = new HashMap<>();
        this.mutationWriteRequests = new HashMap<>();
        this.discoveredServers = new HashSet<>();
        this.aliveServers = new HashSet<>();
        this.lock = new ReentrantLock();
        this.blockingList = new HashSet<>();
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
     * Gets serverToClientChannel.
     *
     * @return Value of serverToClientChannel.
     */
    public ServerToClientChannel getServerToClientChannel() {
        return serverToClientChannel;
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
     * Gets dataAccess.
     *
     * @return Value of dataAccess.
     */
    public DataAccess getDataAccess() {
        return dataAccess;
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
     * Gets mutationWriteRequests.
     *
     * @return Value of mutationWriteRequests.
     */
    public HashMap<String, List<String>> getMutationWriteRequests() {
        return mutationWriteRequests;
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
     * Gets discoveredServers.
     *
     * @return Value of discoveredServers.
     */
    public HashSet<Integer> getDiscoveredServers() {
        return discoveredServers;
    }

    /**
     * Gets aliveServers.
     *
     * @return Value of aliveServers.
     */
    public HashSet<Integer> getAliveServers() {
        return aliveServers;
    }


    /**
     * Gets blockingList.
     *
     * @return Value of blockingList.
     */
    public HashSet<Integer> getBlockingList() {
        return blockingList;
    }


    @Override
    public String toString() {
        return "Node{" +
                "nodeId=" + nodeId +
                ", hostName='" + hostName + '\'' +
                ", serverPortNum=" + serverPortNum +
                ", clientPortNum=" + clientPortNum +
                ", discoveredServers=" + discoveredServers +
                ", aliveServers=" + aliveServers +
                ", blockingList=" + blockingList +
                '}';
    }

    /**
     * Sends a message to the given server
     *
     * @param toBeSent the message to be sent.
     * @param serverId the server id
     * @return the socket used to send the message
     */
    public Socket sendMessage(String toBeSent, int serverId, boolean close) {
        if (blockingList.contains(serverId)) {
            return null;
        }
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
            Socket socket = sendMessage(toBeSent, peerId, false);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            dos.writeUTF(toBeSent);
            dos.flush();

            String received = dis.readUTF();
            if (MessageParser.parseMessageJSON(received).getMessageType() == MessageType.HEARTBEAT_ECHO) {
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
        List<MutationReq> mutationReqs = mutationRequestBuffer.get(mutationReq.getObjectId());
        if (mutationReqs == null) {
            mutationReqs = new ArrayList<>();
        }
        mutationReqs.add(mutationReq);
        mutationRequestBuffer.put(mutationReq.getObjectId(), mutationReqs);
    }

    /**
     * Resets a node.
     *
     * @throws Exception
     */
    public void reset() throws Exception {
        discoveredServers.clear();
        aliveServers.clear();
        blockingList.clear();
        mutationRequestBuffer.clear();
        mutationWriteRequests.clear();
        dataAccess.clearAllData();
    }

}
