package server.network;

import common.Globals;
import common.messages.HeartBeat;
import common.messages.MessageType;
import common.messages.WrapperMessage;
import common.utils.Logger;
import common.utils.MessageParser;

import java.io.DataInputStream;
import java.net.Socket;

/**
 * Heartbeat runner to send the periodic heart beats to the servers.
 * Created by suparngupta on 4/22/14.
 */
@SuppressWarnings("ALL")
public class HeartBeatRunner extends Thread {

    /*
    * the server node
    * */
    private final Node node;

    public HeartBeatRunner(Node node) {
        super("HeartBeatRunner for node " + node.getNodeId() + " created");
        this.node = node;
    }

    /**
     * Sends a heart beat message to all the servers after every few seconds controlled
     * by Globals.heartbeatFrequency parameter.
     */
    @Override
    public void run() {
        Logger.log("HeartBeatRunner started");

        while (true) {
            HeartBeat heartBeat = new HeartBeat(this.node.getNodeId(), -1);
            try {
                for (int serverId : node.getDiscoveredServers()) {
                    heartBeat.setReceiverId(serverId);
                    String toBeSent = MessageParser.createWrapper(heartBeat, MessageType.HEARTBEAT);
                    Socket socket = node.sendMessage(toBeSent, serverId, false);
                    if (socket == null) {
                        node.getAliveServers().remove(serverId);
                    } else {
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        WrapperMessage wrapper = MessageParser.parseMessageJSON(dis.readUTF());
                        if (wrapper.getMessageType() == MessageType.HEARTBEAT_ECHO) {
                            this.node.getAliveServers().add(serverId);
                        } else {
                            this.node.getAliveServers().remove(serverId);
                        }
                    }
                }

                Logger.log("Alive servers", node.getAliveServers());
                Thread.sleep(Globals.heartbeatFrequency);
            } catch (Exception e) {
                Logger.debug(e);
            }


        }
    }
}
