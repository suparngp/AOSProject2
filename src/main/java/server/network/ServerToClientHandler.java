package server.network;

import common.messages.WrapperMessage;
import common.utils.Logger;
import common.utils.MessageParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashSet;

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
                case SEEK_CREATE_PERMISSION:
                    Logger.debug("SEEK_CREATE_PERMISSION Received");
                    break;
                case SEEK_UPDATE_PERMISSION:
                    break;
                case SEEK_READ_PERMISSION:
                    break;
                case SEEK_DELETE_PERMISSION:
                    break;
                case GRANT_CREATE_PERMISSION:
                    break;
                case GRANT_UPDATE_PERMISSION:
                    break;
                case GRANT_READ_PERMISSION:
                    break;
                case GRANT_DELETE_PERMISSION:
                    break;
                case FAILED_CREATE_PERMISSION:
                    break;
                case FAILED_UPDATE_PERMISSION:
                    break;
                case FAILED_READ_PERMISSION:
                    break;
                case FAILED_DELETE_PERMISSION:
                    break;
                case CREATE_OBJ_REQ:
                    break;
                case CREATE_OBJ_SUCCESS:
                    break;
                case CREATE_OBJ_FAILED:
                    break;
                case UPDATE_OBJ_REQ:
                    break;
                case UPDATE_OBJ_SUCCESS:
                    break;
                case UPDATE_OBJ_FAILED:
                    break;
                case READ_OBJ_REQ:
                    break;
                case READ_OBJ_SUCCESS:
                    break;
                case READ_OBJ_FAILED:
                    break;
                case DELETE_OBJ_REQ:
                    break;
                case DELETE_OBJ_SUCCESS:
                    break;
                case DELETE_OBJ_FAILED:
                    break;
            }
        } catch (Exception e) {
            Logger.error("Unable to handle the Server to client request", e);
        }
    }


}
