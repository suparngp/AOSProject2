package server.network;

import common.messages.*;
import common.Globals;
import common.utils.Logger;
import common.utils.MessageParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

/**
 * Request Handler for Server to Server Communication
 * Created by suparngupta on 4/5/14.
 */
public class ServerToServerHandler extends Thread {

    private static final HashSet<Integer> discoveredNodes = new HashSet<>();
    private final Socket socket;
    private final Node node;

    public ServerToServerHandler(Node node, Socket socket) {
        super("ServerToServerHandler");
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
                case SERVER_INTRO: {
                    InfoMessage recMess = (InfoMessage) MessageParser.deserializeObject(wrapper.getMessageBody());
                    InfoMessage info = new InfoMessage(this.node.getNodeId(), recMess.getSenderId());
                    String toBeSent = MessageParser.createWrapper(info, MessageType.SERVER_INTRO_REPLY);
                    dos.writeUTF(toBeSent);
                    dos.flush();
                    break;
                }

                /**
                 * This case will never happen since Replies are always sent in sync.
                 * */
                case SERVER_INTRO_REPLY: {
                    Logger.error("SERVER_INTRO_REPLY received as async");
                    break;
                }

                case DISCOVERY_COMPLETE: {
                    InfoMessage recMess = (InfoMessage) MessageParser.deserializeObject(wrapper.getMessageBody());
                    discoveredNodes.add(recMess.getSenderId());
                    if (discoveredNodes.size() == Globals.serverCount - 1) {
                        Logger.log("All other servers are ready to serve clients");
                        //TODO: take the next step.
                    }
                    break;
                }

                case HEARTBEAT: {
                    HeartBeat beat = (HeartBeat) MessageParser.deserializeObject(wrapper.getMessageBody());

                    if (beat.getSenderId() != node.getNodeId())
                        node.getDiscoveredServers().add(beat.getSenderId());
                    Logger.debug(beat);
                    String toBeSent = MessageParser.createWrapper(beat, MessageType.HEARTBEAT_ECHO);
                    dos.writeUTF(toBeSent);
                    break;
                }

                case MUTATION_PROCEED: {
                    Logger.log("MUTATION_PROCEED Received");
                    MutationWriteReq req = (MutationWriteReq) MessageParser.deserializeObject(wrapper.getMessageBody());
                    List<String> serialNums = req.getSerialNumbers();
                    Logger.log("Mutation Proceed order", serialNums);
                    if (serialNums == null || serialNums.isEmpty()) {
                        String toBeSent = MessageParser.createWrapper(req, MessageType.MUTATION_PROCEED_ACK);
                        dos.writeUTF(toBeSent);
                        break;
                    }

                    List<MutationReq> requests = node.getMutationRequestBuffer().get(req.getObjectId());
                    Logger.debug("Mutation requests", requests);
                    boolean result = false;
                    for (String serialNum : serialNums) {
                        MutationReq mutationReq = null;
                        for (MutationReq request : requests) {
                            if (request.getRequestId().equals(serialNum)) {
                                mutationReq = request;
                                break;
                            }
                        }

                        if(mutationReq != null){
                            result = node.performAndRemoveMutationRequest(req.getObjectId(), mutationReq);
//                            result = node.performMutation(mutationReq);
//
//                            requests.remove(mutationReq);
                        }

                    }

                    String toBeSent;
                    if (result) {
                        toBeSent = MessageParser.createWrapper(req, MessageType.MUTATION_PROCEED_ACK);
                    } else {
                        toBeSent = MessageParser.createWrapper(req, MessageType.MUTATION_PROCEED_FAILED);
                    }

                    dos.writeUTF(toBeSent);
                    break;
                }

                default: {
                    Logger.error("Unknown message type", wrapper.getMessageType());
                }
            }

            dos.flush();
        } catch (Exception e) {
            Logger.error("Unable to handle the server to server request", e);
            e.printStackTrace();
        }
    }


}
