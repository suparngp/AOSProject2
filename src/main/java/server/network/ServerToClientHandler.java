package server.network;

import common.messages.*;
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

                //grant permission if object doesn't exist. No need to check for lock since the object doesn't exist.
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

                //grant permission if object exists and is not locked by anyone else.
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
                    boolean locked = node.lockObject(req.getObjectId(), req.getClientId());
                    toBeSent = locked
                            ? MessageParser.createWrapper(req, MessageType.GRANT_UPDATE_PERMISSION)
                            : MessageParser.createWrapper(req, MessageType.FAILED_UPDATE_PERMISSION);
                    dos.writeUTF(toBeSent);
                    break;

                //grant permission is object exists and is not locked by someone else.
                case SEEK_READ_PERMISSION:
                    Logger.debug("SEEK_READ_PERMISSION Received");
                    req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());

                    locked = node.isObjectLocked(req.getObjectId(), req.getClientId());
                    toBeSent = !locked
                            ? MessageParser.createWrapper(req, MessageType.GRANT_READ_PERMISSION)
                            : MessageParser.createWrapper(req, MessageType.FAILED_READ_PERMISSION);
                    dos.writeUTF(toBeSent);

                    break;

                //grant permission if the object exists and is not locked by anyone else.
                case SEEK_DELETE_PERMISSION:
                    Logger.debug("SEEK_DELETE_PERMISSION Received");
                    req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());
                    locked = node.lockObject(req.getObjectId(), req.getClientId());
                    toBeSent = locked
                            ? MessageParser.createWrapper(req, MessageType.GRANT_DELETE_PERMISSION)
                            : MessageParser.createWrapper(req, MessageType.FAILED_DELETE_PERMISSION);
                    dos.writeUTF(toBeSent);
                    break;


                //create an object is object is valid and doesn't exist in the file
                case CREATE_OBJ_REQ:
                    Logger.debug("CREATE_OBJ_REQ Received");
                    AccountMessage accountMessage = (AccountMessage)MessageParser
                            .deserializeObject(wrapper.getMessageBody());

                    Account acc = accountMessage.getAccount();
                    if(checkAccountStructure(acc)
                            && node.getDataAccess().getAccount(acc.getId()) == null){

                        acc = node.getDataAccess().createAccount(acc);
                        accountMessage.setAccount(acc);
                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.CREATE_OBJ_SUCCESS);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.CREATE_OBJ_FAILED);
                    }
                    dos.writeUTF(toBeSent);
                    break;

                //update object if the structure is valid and exists and object is locked by the client
                case UPDATE_OBJ_REQ:
                    Logger.debug("UPDATE_OBJ_REQ Received");
                    accountMessage = (AccountMessage)MessageParser
                            .deserializeObject(wrapper.getMessageBody());

                    acc = accountMessage.getAccount();
                    if(checkAccountStructure(acc)
                            && node.isObjectLocked(acc.getId(), accountMessage.getClientId())
                            && node.getDataAccess().getAccount(acc.getId()) != null){

                        acc = node.getDataAccess().createAccount(acc);
                        toBeSent = MessageParser.createWrapper(acc, MessageType.CREATE_OBJ_SUCCESS);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(acc, MessageType.CREATE_OBJ_FAILED);
                    }
                    dos.writeUTF(toBeSent);
                    break;

                //read the object if it exists and is not locked by someone else.
                case READ_OBJ_REQ:
                    Logger.debug("CREATE_OBJ_REQ Received");
                    req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());

                    locked = node.isObjectLocked(req.getObjectId(), req.getClientId());
                    acc = node.getDataAccess().getAccount(req.getObjectId());

                    if(!locked && acc != null){
                        toBeSent = MessageParser.createWrapper(acc, MessageType.CREATE_OBJ_SUCCESS);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(acc, MessageType.CREATE_OBJ_FAILED);
                    }
                    dos.writeUTF(toBeSent);
                    break;

                //delete the object if it exists and is locked someone else.
                case DELETE_OBJ_REQ:
                    Logger.debug("CREATE_OBJ_REQ Received");
                    req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());
                    if(node.isObjectLocked(req.getObjectId(), req.getClientId())
                            && node.getDataAccess().getAccount(req.getObjectId()) != null){

                        acc = node.getDataAccess().removeAccount(req.getObjectId());
                        toBeSent = MessageParser.createWrapper(acc, MessageType.DELETE_OBJ_SUCCESS);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(req, MessageType.DELETE_OBJ_FAILED);
                    }
                    dos.writeUTF(toBeSent);
                    break;
            }

            dos.flush();
            dos.close();
            dis.close();
        } catch (Exception e) {
            Logger.error("Unable to handle the Server to client request", e);
        }
    }


    /**
     * Checks the account structure if all the fields are set.
     * @param acc the account object
     * @return true if the the account object is valid, otherwise false.
     */
    private boolean checkAccountStructure(Account acc){
        try{
            if(acc == null || acc.getId() == null
                    || acc.getId().isEmpty()
                    || acc.getCurrentBalance() == null
                    || acc.getCurrentBalance().doubleValue() == 0.0
                    || acc.getOpeningBalance() == null
                    || acc.getOpeningBalance().doubleValue() == 0
                    || acc.getOwnerName() == null || acc.getOwnerName().isEmpty()){
                return false;
            }

            return true;
        }
        catch(Exception e){
            Logger.log("Invalid account structure", e);
            return false;
        }


    }
}
