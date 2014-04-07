package server.network;

import common.messages.MessageType;
import common.messages.ObjectReq;
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
                    ObjectReq req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());


                    /**
                     * Check if the requested object already exists in the file or not.
                     * If its a duplicate, send failed response.
                     * Else send Grant.
                     * */
                    String toBeSent = node.getDataAccess().getAccount(req.getObjectId()) != null
                            ? MessageParser.createWrapper(req, MessageType.FAILED_CREATE_PERMISSION)
                            : MessageParser.createWrapper(req, MessageType.GRANT_CREATE_PERMISSION);
                    dos.writeUTF(toBeSent);
                    break;

                case SEEK_UPDATE_PERMISSION:
                    /**
                     * Check if the object exists in the data file or not.
                     * If not, send error
                     * Else  start a synchronized routine
                     * check if it is already locked. If not, put it in locked queue, return true.
                     * else return false.
                     *
                     * If routine returns false, return error,
                     * else grant the permission.
                     * */

                    Logger.debug("SEEK_UPDATE_PERMISSION Received");
                    req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());
                    boolean locked = node.lockObject(req.getObjectId());
                    toBeSent = locked
                            ? MessageParser.createWrapper(req, MessageType.GRANT_UPDATE_PERMISSION)
                            : MessageParser.createWrapper(req, MessageType.FAILED_UPDATE_PERMISSION);
                    dos.writeUTF(toBeSent);
                    break;
                case SEEK_READ_PERMISSION:
                    Logger.debug("SEEK_READ_PERMISSION Received");
                    req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());
                    locked = node.lockObject(req.getObjectId());
                    toBeSent = locked
                            ? MessageParser.createWrapper(req, MessageType.GRANT_READ_PERMISSION)
                            : MessageParser.createWrapper(req, MessageType.FAILED_READ_PERMISSION);
                    dos.writeUTF(toBeSent);

                    break;
                case SEEK_DELETE_PERMISSION:
                    Logger.debug("SEEK_DELETE_PERMISSION Received");
                    req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());
                    locked = node.lockObject(req.getObjectId());
                    toBeSent = locked
                            ? MessageParser.createWrapper(req, MessageType.GRANT_DELETE_PERMISSION)
                            : MessageParser.createWrapper(req, MessageType.FAILED_DELETE_PERMISSION);
                    dos.writeUTF(toBeSent);
                    break;
                case CREATE_OBJ_REQ:

                    break;
                case UPDATE_OBJ_REQ:
                    break;
                case READ_OBJ_REQ:
                    break;
                case DELETE_OBJ_REQ:
                    break;
            }
            dos.flush();
            dos.close();
            dis.close();
        } catch (Exception e) {
            Logger.error("Unable to handle the Server to client request", e);
        }
    }


}
