package server.network;

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

/**
 * Request Handler for Server to client Communication
 * Created by suparngupta on 4/5/14.
 */
public class ServerToClientHandler extends Thread {

    private Socket socket;
    private Node node;

    private static HashSet<Integer> discoveredNodes = new HashSet<>();

    public ServerToClientHandler(Node node, Socket socket) {
        super("ServerToClientHandler");
        this.socket = socket;
        this.node = node;
    }


    public void run() {
        try {
            if (!socket.isConnected() || socket.isClosed()) {
                return;
            }
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String messageStr = dis.readUTF();

            WrapperMessage wrapper = MessageParser.parseMessageJSON(messageStr);

            switch (wrapper.getMessageType()) {
                case HEARTBEAT:{
                    HeartBeat beat = (HeartBeat)MessageParser.deserializeObject(wrapper.getMessageBody());
                    Logger.debug(beat);
                    String toBeSent = MessageParser.createWrapper(beat, MessageType.HEARTBEAT_ECHO);
                    dos.writeUTF(toBeSent);
                    break;
                }
                case WHO_IS_PRIMARY: {
                    ObjectReq req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());
                    String objectId = req.getObjectId();
                    int server1 = node.computeServerId(objectId);
                    int server2 = (server1 + 1) % 7;
                    int server3 = (server1 + 2) % 7;
                    int primaryId = -1;

                    if (node.isPeerReachable(server1)) {
                        primaryId = server1;
                    } else if (node.isPeerReachable(server2)) {
                        primaryId = server2;
                    } else if (node.isPeerReachable(server3)) {
                        primaryId = server3;
                    }
                    PrimaryInfo info;
                    if(primaryId == -1){
                        info = new PrimaryInfo(primaryId, "", -1);
                    }
                    else{
                        info = new PrimaryInfo(primaryId,
                                Globals.serverHostNames.get(primaryId),
                                Globals.serverClientPortNums.get(primaryId));
                    }


                    String toBeSent = MessageParser.createWrapper(info, MessageType.PRIMARY_INFO);
                    dos.writeUTF(toBeSent);
                    break;
                }

                case READ_OBJ_REQ:{
                    ObjectReq req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());
                    Account acc = node.getDataAccess().getAccount(req.getObjectId());

                    String toBeSent;
                    if(acc == null){
                        toBeSent = MessageParser.createWrapper(req, MessageType.READ_OBJ_FAILED);
                    }
                    else {
                        AccountMessage accountMessage;
                        accountMessage = new AccountMessage(req.getServerId(), req.getClientId(), acc);
                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.READ_OBJ_SUCCESS);
                    }
                    dos.writeUTF(toBeSent);
                    break;
                }
                case MUTATION_REQ: {
                    MutationReq mutationReq = (MutationReq) MessageParser.deserializeObject(wrapper.getMessageBody());
                    node.addMutationReq(mutationReq);
                    MutationAck ack = new MutationAck(mutationReq, true);
                    String toBeSent = MessageParser.createWrapper(ack, MessageType.MUTATION_ACK);
                    dos.writeUTF(toBeSent);
                    Logger.debug(node.getMutationRequestBuffer());
                    Logger.debug(node.getMutationWriteRequests());
                    break;
                }
                case MUTATION_WRITE_REQ: {
                    //get the write request, assign it a new serial number and add it to the queue.
                    //lock the data structure
                    Logger.log("Received MUTATION_WRITE_REQ");
                    node.getLock().lock();
                    MutationWriteReq req = (MutationWriteReq) MessageParser
                            .deserializeObject(wrapper.getMessageBody());

                    node.addMutationWriteReq(req.getObjectId(), req.getRequestId());

                    Logger.debug(node.getMutationWriteRequests());
                    //get the lock again and try to send the request to other servers.

                    List<String> serialNums = node.getMutationWriteRequests().get(req.getObjectId());
                    String toBeSent;
                    if(serialNums == null || serialNums.isEmpty())
                    {
                        node.getLock().unlock();
                        toBeSent = MessageParser.createWrapper(req, MessageType.MUTATION_WRITE_ACK);
                        dos.writeUTF(toBeSent);
                        break;
                    }
                    req.setSerialNumbers(serialNums);
                    Logger.debug(req);
                    String objectId = req.getObjectId();
                    int server1 = node.computeServerId(objectId);
                    int server2 = (server1 + 1) % 7;
                    int server3 = (server2 + 1) % 7;
                    int[] servers = {server1, server2, server3};
                    List<Integer> successList = new ArrayList<>();
                    DataInputStream dis1;
                    DataOutputStream dos1;
                    Logger.log("Sending Mutation proceed message to all the servers");
                    for(int serverId: servers){
                        toBeSent = MessageParser.createWrapper(req, MessageType.MUTATION_PROCEED);
                        Socket socket1 = node.sendMessage(toBeSent, serverId, false);
                        if(socket1 != null){
                            dis1 = new DataInputStream(socket1.getInputStream());
                            dos1  = new DataOutputStream(socket1.getOutputStream());
                            dos1.writeUTF(toBeSent);
                            Logger.log("Sent Mutation Proceed to server " + serverId + ", waiting for the ack");
                            String received = dis1.readUTF();
                            Logger.log("Message received from server " + serverId);
                            WrapperMessage message = MessageParser.parseMessageJSON(received);
                            if (message.getMessageType() == MessageType.MUTATION_PROCEED_ACK) {
                                Logger.log("MUTATION_PROCEED_ACK received from server " + serverId);
                                successList.add(serverId);
                            }
                            dis1.close();
                            dos1.close();
                            socket1.close();
                        }
                    }
                    node.getMutationRequestBuffer().get(req.getObjectId()).clear();
                    node.getMutationWriteRequests().get(req.getObjectId()).clear();
                    node.getMutationRequestBuffer().remove(req.getObjectId());
                    node.getMutationWriteRequests().remove(req.getObjectId());
                    node.getLock().unlock();
                    if(successList.size() > 1){
                        toBeSent = MessageParser.createWrapper(req, MessageType.MUTATION_WRITE_ACK);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(req, MessageType.MUTATION_WRITE_FAILED);
                    }
                    dos.writeUTF(toBeSent);
                    break;
                }
            }
            dos.flush();
            dos.close();
            dis.close();
        } catch (Exception e) {
            Logger.error("Unable to handle the Server to client request", e);
            e.printStackTrace();
        }
    }


    /**
     * Checks the account structure if all the fields are set.
     *
     * @param acc the account object
     * @return true if the the account object is valid, otherwise false.
     */
    private boolean checkAccountStructure(Account acc) {
        try {
            if (acc == null || acc.getId() == null
                    || acc.getId().isEmpty()
                    || acc.getCurrentBalance() == null
                    || acc.getCurrentBalance().doubleValue() == 0.0
                    || acc.getOpeningBalance() == null
                    || acc.getOpeningBalance().doubleValue() == 0
                    || acc.getOwnerName() == null || acc.getOwnerName().isEmpty()) {
                return false;
            }

            return true;
        } catch (Exception e) {
            Logger.log("Invalid account structure", e);
            return false;
        }


    }
}
