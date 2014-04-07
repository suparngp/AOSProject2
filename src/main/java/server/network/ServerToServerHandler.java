package server.network;

import common.messages.InfoMessage;
import common.messages.MessageType;
import common.messages.WrapperMessage;
import server.Globals;
import common.utils.Logger;
import common.utils.MessageParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashSet;

/**
 * Request Handler for Server to Server Communication
 * Created by suparngupta on 4/5/14.
 */
public class ServerToServerHandler extends Thread {

    private Socket socket;
    private Node node;

    private static HashSet<Integer> discoveredNodes = new HashSet<>();

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
                case SERVER_INTRO:
                    InfoMessage recMess = (InfoMessage) MessageParser.deserializeObject(wrapper.getMessageBody());
                    InfoMessage info = new InfoMessage(this.node.getNodeId(), recMess.getSenderId());
                    String toBeSent = MessageParser.createWrapper(info, MessageType.SERVER_INTRO_REPLY);
                    dos.writeUTF(toBeSent);
                    dos.flush();
                    break;

                /**
                 * This case will never happen since Replies are always sent in sync.
                 * */
                case SERVER_INTRO_REPLY:
                    Logger.error("SERVER_INTRO_REPLY received as async");
                    break;
                case DISCOVERY_COMPLETE:
                    recMess = (InfoMessage) MessageParser.deserializeObject(wrapper.getMessageBody());
                    discoveredNodes.add(recMess.getSenderId());
                    if (discoveredNodes.size() == Globals.serverCount - 1) {
                        Logger.log("All other servers are ready to serve clients");
                        //TODO: take the next step.
                    }
                    break;
            }
        } catch (Exception e) {
            Logger.error("Unable to handle the server to server request", e);
        }
    }


}
