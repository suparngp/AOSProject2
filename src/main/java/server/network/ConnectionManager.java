package server.network;

import common.Globals;
import common.messages.InfoMessage;
import common.messages.MessageType;
import common.messages.WrapperMessage;
import common.utils.Logger;
import common.utils.MessageParser;
import server.services.InputProcessor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the network discovery protocol for the servers.
 * Once the network is discovered, the servers become available to all the clients.
 * Created by suparngupta on 4/5/14.
 */
public class ConnectionManager {

    private Node node;


    public ConnectionManager(Node node) {
        this.node = node;
    }

    public void initializeServerChannel() throws  Exception{
        this.node.getServerToServerChannel().init().start();
    }

    public void discoverNetwork() throws Exception{

        try{
            Set<Integer> serverIds = Globals.serverHostNames.keySet();
            HashSet<Integer> discoveredNodes = new HashSet<>();
            Logger.debug(serverIds);

            while(true){
                if(Globals.serverCount < 2){
                    break;
                }
                for(int serverId: serverIds){
                    //if the serverId is same as this node's id, skip it or this node has already been discovered.

                    if(node.getNodeId() == serverId || discoveredNodes.contains(serverId)){
                        continue;
                    }

                    //create a new socket with the server
                    Socket socket;
                    try{
//                        socket = new Socket(Globals.serverHostNames.get(serverId)
//                                , Globals.serverPortNums.get(serverId));
//                        Logger.log("Socket connected to ", socket.getLocalAddress().getHostAddress(), socket.getPort());
                        InfoMessage info = new InfoMessage(node.getNodeId(), serverId);
                        String message = MessageParser.createWrapper(info, MessageType.SERVER_INTRO);
                        socket = node.sendMessage(message, serverId, false);
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                        dos.writeUTF(message);
                        dos.flush();

                        String recMessage = dis.readUTF();
                        WrapperMessage wrapper = MessageParser.parseMessageJSON(recMessage);
                        if(wrapper.getMessageType() == MessageType.SERVER_INTRO_REPLY){
                            InfoMessage reply = (InfoMessage)(MessageParser.deserializeObject(wrapper.getMessageBody()));
                            discoveredNodes.add(reply.getSenderId());
                        }
                        else{
                            Logger.error("Server sent an unknown reply to INTRO", wrapper);
                            throw new Exception();
                        }
                        Logger.debug("Discovered Nodes", discoveredNodes);
                    }
                    catch(Exception e){
                        Logger.debug("Node is not yet up", serverId, e);
                    }
                }

                //all the nodes have been discovered.
                if(discoveredNodes.size() == Globals.serverCount - 1){
                    break;
                }

                Thread.sleep(2000);
            }


            //if everyone has been discovered, then send the discovery complete message to everyone else.

//            HashMap<String, Integer> receivers = new HashMap<>();
//            for(int serverId: discoveredNodes){
//                receivers.put(Globals.serverHostNames.get(serverId)
//                        , Globals.serverPortNums.get(serverId));
//            }
//
//            if(!receivers.isEmpty()){
//                Logger.log("Node Discovery is complete, sending the DISCOVERY_COMPLETE message to everyone else.");
//                InfoMessage info = new InfoMessage(this.node.getNodeId(), -1);
//                String toBeSent = MessageParser.createWrapper(info, MessageType.DISCOVERY_COMPLETE);
//                this.node.broadcastMessage(receivers, toBeSent);
//                Logger.log("DISCOVERY_COMPLETE message broadcast completed successfully.");
//            }

            //if everyone has been discovered, then send the discovery complete message to everyone else.
            Logger.log("Node Discovery is complete, sending the DISCOVERY_COMPLETE message to everyone else.");
            for(int serverId: discoveredNodes){
                InfoMessage info = new InfoMessage(this.node.getNodeId(), -1);
                String toBeSent = MessageParser.createWrapper(info, MessageType.DISCOVERY_COMPLETE);
                node.sendMessage(toBeSent, serverId, true);
            }
            Logger.log("DISCOVERY_COMPLETE message broadcast completed successfully.");
            this.node.getDiscoveredServers().addAll(discoveredNodes);

            new HeartBeatRunner(node).start();
            node.getServerToClientChannel().init().start();
            new InputProcessor(node).processInput();
        }

        catch (Exception e){
            Logger.error("Unable to complete the network discovery process", e);
            throw e;
        }
    }
}
