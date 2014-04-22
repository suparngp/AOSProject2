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
 * Created by Nimisha on 7/4/14.
 */
public class ClientNode {
    private int nodeId;
    private int objectIdCounter = 1;
    private HashSet<Integer> blockingList;
    public ClientNode(int nodeId) {
        this.nodeId = nodeId;
        this.blockingList = new HashSet<>();
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

    public Account create(String id, String ownerName, Double opBal, Double curBal) throws Exception {
        int server1 = computeServerId(id);
        int server2 = (server1 + 1) % 7;
        int server3 = (server2 + 1) % 7;

        return null;
    }

    /**
     * Checks if the server is reachable.
     * @param serverId the server id
     * @return true if the server is reachable or not.
     */
    public boolean isServerReachable(int serverId){
        try{
            boolean result = false;
            String toBeSent = MessageParser.createWrapper(new HeartBeat(this.nodeId, serverId), MessageType.HEARTBEAT);
            Socket socket = sendMessage(toBeSent, serverId, false);
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
            socket.close();
            return result;
        }

        catch (Exception e){
            Logger.log("Server unreachable " + serverId);
            return false;
        }
    }
    /**
     * Generates the bject id of the new object.
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
     * Computes the server id
     *
     * @param objectId the objectId
     * @return the server id
     */
    //TODO update the code.
    public synchronized int computeServerId(String objectId) {
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
     * Sends a message to the given server
     *
     * @param toBeSent the message to be sent.
     * @param serverId the server id
     * @return the socket used to send the message
     */
    public Socket sendMessage(String toBeSent, int serverId, boolean close) {

        //check if the channel is blocked?
        if(this.blockingList.contains(serverId)){
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


    public List<Integer> getAvailableServers(String objectId){
        int server1 = computeServerId(objectId);
        int server2 = (server1 + 1) % 7;
        int server3 = (server2 + 1) % 7;

        List<Integer> successServers = new ArrayList<>();

        if(isServerReachable(server1)){
            successServers.add(server1);
        }
        if(isServerReachable(server2)){
            successServers.add(server2);
        }
        if(isServerReachable(server3)){
            successServers.add(server3);
        }
        return successServers;
    }

    public PrimaryInfo getPrimaryServer(List<Integer> servers, String objectId) throws Exception{
        List<PrimaryInfo> info = new ArrayList<>();
        for(int serverId: servers){
            ObjectReq req = new ObjectReq(objectId, serverId, this.nodeId);
            String toBeSent = MessageParser.createWrapper(req, MessageType.WHO_IS_PRIMARY);
            Socket socket = sendMessage(toBeSent, serverId, false);
            if(socket == null){
                continue;
            }
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            WrapperMessage wrapper = MessageParser.parseMessageJSON(dis.readUTF());

            if(wrapper.getMessageType() == MessageType.PRIMARY_INFO){
                info.add((PrimaryInfo)MessageParser.deserializeObject(wrapper.getMessageBody()));
            }
            dis.close();
            socket.close();
        }

        if(info.isEmpty()){
            return null;
        }
        return info.get(0);
    }

    public boolean sendMutationRequest(Account account, MutationType mutationType, String objectId) throws Exception{
        List<Integer> servers = getAvailableServers(objectId);
        if(servers.size() < 2){
            Logger.log("Unable to send mutation request because less than 2 servers are available", servers);
            return false;
        }

        PrimaryInfo info = getPrimaryServer(servers, objectId);
        if(info == null){
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
        for(int serverId: servers){
            mutationReq.setServerId(serverId);
            String toBeSent = MessageParser.createWrapper(mutationReq, MessageType.MUTATION_REQ);
            Socket socket = sendMessage(toBeSent, serverId, false);
            if(socket == null){
                continue;
            }
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            if(MessageParser.parseMessageJSON(dis.readUTF()).getMessageType() == MessageType.MUTATION_ACK){
                successServers.add(serverId);
            }
        }

        if(successServers.size() < 2){
            Logger.log("Less than 2 servers acknowledged the mutation request", successServers);
            return false;
        }

        //send mutation write req to the primary.
        MutationWriteReq writeReq = new MutationWriteReq(requestId,
                this.nodeId, new ArrayList<String>(), account.getId());
        String toBeSent = MessageParser.createWrapper(writeReq, MessageType.MUTATION_WRITE_REQ);
        Socket socket = sendMessage(toBeSent, info.getPrimaryId(), false);
        if(socket == null){
            Logger.log("Unable to complete mutation write");
            return false;
        }

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        WrapperMessage wrapper = MessageParser.parseMessageJSON(dis.readUTF());
        if(wrapper.getMessageType() == MessageType.MUTATION_WRITE_ACK){
            Logger.log("Mutation process completed");
            return true;
        }
        else{
            Logger.log("Mutation process failed");
            return false;
        }
    }

    public Account sendReadRequest(String objectId) throws Exception{

        int root = computeServerId(objectId);
        int[] servers = {root, (root + 1) % 7, (root + 2) % 7};
        Account account = null;
        for(int serverId: servers){
            ObjectReq req = new ObjectReq(objectId, serverId, this.nodeId);
            String toBeSent = MessageParser.createWrapper(req, MessageType.READ_OBJ_REQ);
            Socket socket = sendMessage(toBeSent, serverId, false);
            DataInputStream dis;
            if(socket != null){
                dis = new DataInputStream(socket.getInputStream());
                WrapperMessage wrapper = MessageParser.parseMessageJSON(dis.readUTF());
                if(wrapper.getMessageType() == MessageType.READ_OBJ_SUCCESS){
                    AccountMessage accountMessage = (AccountMessage)MessageParser.deserializeObject(wrapper.getMessageBody());
                    Logger.debug("Account found", accountMessage.getAccount());
                    account = accountMessage.getAccount();
                    break;
                }
            }
        }
        return account;
    }

    /**
     * Sets new blockingList.
     *
     * @param blockingList New value of blockingList.
     */
    public void setBlockingList(HashSet<Integer> blockingList) {
        this.blockingList = blockingList;
    }

    /**
     * Gets blockingList.
     *
     * @return Value of blockingList.
     */
    public HashSet<Integer> getBlockingList() {
        return blockingList;
    }
}
