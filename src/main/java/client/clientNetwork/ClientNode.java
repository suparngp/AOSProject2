package client.clientNetwork;

import common.Globals;
import common.messages.*;
import common.utils.Logger;
import common.utils.MessageParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Represents a client node. A client has these properties nodeId, a counter to compute unique
 * objectIds and a blocking list to block communication with servers.
 * Created by Nimisha on 7/4/14.
 */
public class ClientNode {

    /**
     * The client's id, usually different from server's ids.
     */
    private int nodeId;

    /**
     * objectIdCounter to compute the objectIds
     */
    private int objectIdCounter = 1;

    /**
     * the blocking list to block the communication with the server.
     */
    private HashSet<Integer> blockingList;

    /**
     * Creates a client node with the specified client id
     *
     * @param clientId the client's id.
     */
    public ClientNode(int clientId) {
        this.nodeId = clientId;
        this.blockingList = new HashSet<>();
    }

    /**
     * Gets blockingList.
     *
     * @return Value of blockingList.
     */
    public HashSet<Integer> getBlockingList() {
        return blockingList;
    }

    /**
     * Checks if the server is reachable. Sends a Heart beat message which is an instance of
     * {@link common.messages.HeartBeat} with message type HEARTBEAT.
     * If HEARTBEAT_ECHO is received in response, the server is reachable, otherwise not.
     *
     * @param serverId the server id
     * @return true if the server is reachable otherwise false.
     */
    public boolean isServerReachable(int serverId) {
        try {
            boolean result = false;
            String toBeSent = MessageParser.createWrapper(new HeartBeat(this.nodeId, serverId), MessageType.HEARTBEAT);
            Socket socket = sendMessage(toBeSent, serverId, false);
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
            socket.close();
            return result;
        } catch (Exception e) {
            Logger.log("Server unreachable " + serverId);
            return false;
        }
    }

    /**
     * Generates the object id of the new object.
     * Each object id is a combination of current system time - nodeId - and a counter value.
     *
     * @return
     */
    public synchronized String generateObjectId() {
        if (objectIdCounter == 10) {
            objectIdCounter = 1;
        }

        String objectId = "";
        objectId += System.currentTimeMillis() + "-" + this.nodeId + "-" + this.objectIdCounter;
        this.objectIdCounter++;
        return objectId;
    }

    /**
     * Computes the server id based on the object's id.
     * The ascii sum of all the characters in the object id is
     * calculated and a modulus 7 operation is performed.
     *
     * @param objectId the objectId
     * @return the server id
     */
    //TODO update the code.
    synchronized int computeServerId(String objectId) {
        return 0;

//        if(objectId == null || objectId.isEmpty()){
//            return 0;
//        }
//
//        int sum = 0;
//        for(int i = 0; i < objectId.length(); i++){
//            sum += (int)objectId.charAt(i);
//        }
//        return sum % 7;
    }


    /**
     * Sends a message to the given server and leaves the socket open
     * if the closed flag is false.
     *
     * @param toBeSent the message to be sent.
     * @param serverId the server id
     * @param close    close the socket or not
     * @return the socket used to send the message, if close flag is false, otherwise null.
     */
    Socket sendMessage(String toBeSent, int serverId, boolean close) {

        //check if the channel is blocked?
        if (this.blockingList.contains(serverId)) {
            return null;
        }
        String hostName = Globals.serverHostNames.get(serverId);
        int portNum = Globals.serverClientPortNums.get(serverId);

        Logger.log("Sending message to ", serverId);
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
     * Gets the list of available (alive) servers for an objectId.
     * The first server's (usually the primary) is calculated
     * using {@link client.clientNetwork.ClientNode#computeServerId(String)} method
     * and the next two servers ids are calculated by incrementing the
     * first server's id and taking a modulus 7. Next each server is sent a
     * {@link common.messages.HeartBeat} message to check if it is alive.
     *
     * @param objectId the object id
     * @return the list of alive servers
     */
    public List<Integer> getAvailableServers(String objectId) {
        int server1 = computeServerId(objectId);
        int server2 = (server1 + 1) % 7;
        int server3 = (server2 + 1) % 7;

        List<Integer> successServers = new ArrayList<>();

        if (isServerReachable(server1)) {
            successServers.add(server1);
        }
        if (isServerReachable(server2)) {
            successServers.add(server2);
        }
        if (isServerReachable(server3)) {
            successServers.add(server3);
        }
        return successServers;
    }

    /**
     * Gets the information about the primary server for an object id which will
     * coordinate the writes on the replica servers. It picks up the majority of replies.
     *
     * @param servers  the list of server ids which are available.
     * @param objectId the object id
     * @return the {@link common.messages.PrimaryInfo} instance which contains the
     * information about primary or null
     * @throws Exception
     */
    public PrimaryInfo getPrimaryServer(List<Integer> servers, String objectId) throws Exception {
        List<PrimaryInfo> info = new ArrayList<>();
        for (int serverId : servers) {
            ObjectReq req = new ObjectReq(objectId, serverId, this.nodeId);
            String toBeSent = MessageParser.createWrapper(req, MessageType.WHO_IS_PRIMARY);
            Socket socket = sendMessage(toBeSent, serverId, false);
            if (socket == null) {
                continue;
            }
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            WrapperMessage wrapper = MessageParser.parseMessageJSON(dis.readUTF());

            if (wrapper.getMessageType() == MessageType.PRIMARY_INFO) {
                info.add((PrimaryInfo) MessageParser.deserializeObject(wrapper.getMessageBody()));
            }
            dis.close();
            socket.close();
        }

        if (info.isEmpty()) {
            return null;
        }
        return info.get(0);
    }

    /**
     * Sends a mutation request for the object. A mutation request has a type, which is an instance of
     * {@link common.messages.MutationType}. First , it gets the list of available servers using
     * {@link client.clientNetwork.ClientNode#getAvailableServers(String)}. Next the primary
     * server's information is found using
     * {@link client.clientNetwork.ClientNode#getPrimaryServer(java.util.List, String)}
     * Mutation request is pushed to all the reachable servers. At the end,
     * a  mutation write request which is an instance of MutationWriteReq is sent to the primary.
     * The primary server coordinates with the replica servers and commits the mutation.
     * If the mutation is successful, it replies the client with an message type MUTATION_WRITE_ACK
     * otherwise, MUTATION_WRITE_FAILED
     *
     * @param account      the instance of {@link common.messages.Account} which acts as the data
     * @param mutationType the instance of {@link common.messages.MutationType}
     * @param objectId     the object id
     * @return true if the mutation was successful otherwise false.
     * @throws Exception
     */
    public boolean sendMutationRequest(Account account,
                                       MutationType mutationType, String objectId) throws Exception {
        List<Integer> servers = getAvailableServers(objectId);
        if (servers.size() < 2) {
            Logger.log("Unable to send mutation request because less than 2 servers are available", servers);
            return false;
        }

        PrimaryInfo info = getPrimaryServer(servers, objectId);
        if (info == null) {
            Logger.log("Unable to find the primary server", servers);
            return false;
        }


        //create a mutation request object
        String requestId = UUID.randomUUID().toString();
        MutationReq mutationReq = new MutationReq();
        mutationReq.setClientId(this.nodeId);
        mutationReq.setData(account);
        mutationReq.setObjectId(account.getId());
        mutationReq.setRequestId(requestId);
        mutationReq.setRequestType(mutationType);


        //send the mutation request to all the servers.
        List<Integer> successServers = new ArrayList<>();
        for (int serverId : servers) {
            mutationReq.setServerId(serverId);
            String toBeSent = MessageParser.createWrapper(mutationReq, MessageType.MUTATION_REQ);
            Socket socket = sendMessage(toBeSent, serverId, false);
            if (socket == null) {
                continue;
            }
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            if (MessageParser.parseMessageJSON(dis.readUTF()).getMessageType() == MessageType.MUTATION_ACK) {
                successServers.add(serverId);
            }
        }

        if (successServers.size() < 2) {
            Logger.log("Less than 2 servers acknowledged the mutation request", successServers);
            return false;
        }

        //send mutation write req to the primary.
        MutationWriteReq writeReq = new MutationWriteReq(requestId,
                this.nodeId, new ArrayList<String>(), account.getId());
        String toBeSent = MessageParser.createWrapper(writeReq, MessageType.MUTATION_WRITE_REQ);
        Socket socket = sendMessage(toBeSent, info.getPrimaryId(), false);
        if (socket == null) {
            Logger.log("Unable to complete mutation write");
            return false;
        }

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        WrapperMessage wrapper = MessageParser.parseMessageJSON(dis.readUTF());
        if (wrapper.getMessageType() == MessageType.MUTATION_WRITE_ACK) {
            Logger.log("Mutation process completed");
            return true;
        } else {
            Logger.log("Mutation process failed");
            return false;
        }
    }

    /**
     * Sends a read request to the servers based on the objectId.
     * First the server ids are computed for the specified object id.
     * Next each server is queried for the data until a positive response
     * is received or the list of servers exhausts.
     *
     * @param objectId the object id
     * @return the instance of {@link common.messages.Account} if found, otherwise null.
     * @throws Exception
     */
    public Account sendReadRequest(String objectId) throws Exception {

        int root = computeServerId(objectId);
        int[] servers = {root, (root + 1) % 7, (root + 2) % 7};
        Account account = null;
        for (int serverId : servers) {
            ObjectReq req = new ObjectReq(objectId, serverId, this.nodeId);
            String toBeSent = MessageParser.createWrapper(req, MessageType.READ_OBJ_REQ);
            Socket socket = sendMessage(toBeSent, serverId, false);
            DataInputStream dis;
            if (socket != null) {
                dis = new DataInputStream(socket.getInputStream());
                WrapperMessage wrapper = MessageParser.parseMessageJSON(dis.readUTF());
                if (wrapper.getMessageType() == MessageType.READ_OBJ_SUCCESS) {
                    AccountMessage accountMessage = (AccountMessage) MessageParser.deserializeObject(wrapper.getMessageBody());
                    Logger.debug("Account found", accountMessage.getAccount());
                    account = accountMessage.getAccount();
                    break;
                }
            }
        }
        return account;
    }
}
