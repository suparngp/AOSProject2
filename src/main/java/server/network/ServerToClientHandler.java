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
                    Logger.debug(this.node.getLockedObjects());
                    boolean locked = node.lockObject(req.getObjectId(), req.getClientId());
                    Logger.debug("Locked", locked);
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
                    toBeSent = !locked && node.getDataAccess().getAccount(req.getObjectId()) != null
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
                //store this object in the temporary file only.
                case CREATE_OBJ_REQ:
                    Logger.debug("CREATE_OBJ_REQ Received");
                    AccountMessage accountMessage = (AccountMessage)MessageParser
                            .deserializeObject(wrapper.getMessageBody());

                    Account acc = accountMessage.getAccount();
                    if(checkAccountStructure(acc)
                            && node.getDataAccess().getAccount(acc.getId()) == null){

                        node.getTemporaryData().add(acc);

//                        acc = node.getDataAccess().createAccount(acc);
                        accountMessage.setAccount(acc);
                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.CREATE_OBJ_SUCCESS);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.CREATE_OBJ_FAILED);
                    }
                    dos.writeUTF(toBeSent);
                    break;


                case CREATE_OBJECT_PREPARE:
                    Logger.debug("CREATE_OBJECT_PREPARE Received");
                    req = (ObjectReq)MessageParser.deserializeObject(wrapper.getMessageBody());

                    Account temp = node.getTemporaryAccount(req.getObjectId());

                    if(temp == null){
                        toBeSent = MessageParser.createWrapper(req, MessageType.CREATE_OBJ_FAILED);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(req, MessageType.CREATE_OBJECT_PREPARE_ACK);
                        node.getTemporaryData().remove(temp);
                        node.addToPreparedData(temp);
                    }
                    dos.writeUTF(toBeSent);

                    break;

                case CREATE_OBJECT_ABORT:
                    Logger.debug("CREATE_OBJECT_ABORT received");
                    Logger.debug(node.getTemporaryData());
                    req = (ObjectReq)MessageParser.deserializeObject(wrapper.getMessageBody());
                    temp = node.getTemporaryAccount(req.getObjectId());
                    if(temp != null){
                        node.getTemporaryData().remove(temp);
                    }
                    temp = node.getPreparedAccount(req.getObjectId());
                    if(temp != null){
                        node.getPreparedAccess().removeAccount(temp.getId());
                    }
                    Logger.debug(node.getTemporaryData());
                    break;

                //TODO add the commit
                case CREATE_OBJECT_COMMIT:
                    Logger.debug("CREATE_OBJECT_COMMIT received");
                    req = (ObjectReq)MessageParser.deserializeObject(wrapper.getMessageBody());
                    acc = node.getPreparedAccount(req.getObjectId());
                    node.getDataAccess().createAccount(acc);
                    node.getPreparedData().remove(acc);
                    node.getPreparedAccess().removeAccount(acc.getId());
                    break;

                //update object if the structure is valid and exists and object is locked by the client
                case UPDATE_OBJ_REQ:
                    Logger.debug("UPDATE_OBJ_REQ Received");

                    accountMessage = (AccountMessage)MessageParser
                            .deserializeObject(wrapper.getMessageBody());

                    acc = accountMessage.getAccount();
                    Account oldAccount = node.getDataAccess().getAccount(acc.getId());
                    if(checkAccountStructure(acc)
                            && node.isObjectLocked(acc.getId(), accountMessage.getClientId())
                            && oldAccount != null){

                        oldAccount.setOpeningBalance(acc.getOpeningBalance());
                        oldAccount.setCurrentBalance(acc.getCurrentBalance());
                        oldAccount.setOwnerName(acc.getOwnerName());
                        oldAccount.setUpdatedAt(acc.getUpdatedAt());
                        node.getTemporaryData().add(oldAccount);

//                        acc = node.getDataAccess().createAccount(acc);
                        accountMessage.setAccount(oldAccount);
                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.UPDATE_OBJ_SUCCESS);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.UPDATE_OBJ_FAILED);
                    }

                    dos.writeUTF(toBeSent);
                    break;

                case UPDATE_OBJECT_PREPARE:
                    Logger.debug("UPDATE_OBJECT_PREPARE Received");
                    req = (ObjectReq)MessageParser.deserializeObject(wrapper.getMessageBody());

                    temp = node.getTemporaryAccount(req.getObjectId());

                    if(temp == null){
                        toBeSent = MessageParser.createWrapper(req, MessageType.UPDATE_OBJ_FAILED);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(req, MessageType.UPDATE_OBJECT_PREPARE_ACK);
                        node.getTemporaryData().remove(temp);
                        node.addToPreparedData(temp);
                    }
                    dos.writeUTF(toBeSent);
                    node.freeObject(req.getObjectId(), req.getClientId());
                    break;

                case UPDATE_OBJECT_ABORT:
                    Logger.debug("UPDATE_OBJECT_ABORT received");
                    Logger.debug(node.getTemporaryData());
                    req = (ObjectReq)MessageParser.deserializeObject(wrapper.getMessageBody());
                    temp = node.getTemporaryAccount(req.getObjectId());
                    if(temp != null){
                        node.getTemporaryData().remove(temp);
                    }
                    temp = node.getPreparedAccount(req.getObjectId());
                    if(temp != null){
                        node.getPreparedAccess().removeAccount(temp.getId());
                    }
                    Logger.debug(node.getTemporaryData());
                    node.freeObject(req.getObjectId(), req.getClientId());
                    break;

                //TODO add the commit
                case UPDATE_OBJECT_COMMIT:
                    Logger.debug("UPDATE_OBJECT_COMMIT received");
                    req = (ObjectReq)MessageParser.deserializeObject(wrapper.getMessageBody());
                    acc = node.getPreparedAccount(req.getObjectId());

                    node.getDataAccess().updateAccount(acc);
                    node.getPreparedData().remove(acc);
                    node.getPreparedAccess().removeAccount(acc.getId());
                    node.freeObject(acc.getId(), req.getClientId());
                    break;

                //read the object if it exists and is not locked by someone else.
                case READ_OBJ_REQ:
                    Logger.debug("READ_OBJ_REQ Received");
                    req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());

                    locked = node.isObjectLocked(req.getObjectId(), req.getClientId());
                    acc = node.getDataAccess().getAccount(req.getObjectId());

                    accountMessage = new AccountMessage();
                    accountMessage.setServerId(req.getServerId());
                    accountMessage.setClientId(req.getClientId());
                    accountMessage.setAccount(acc);

                    if(!locked && acc != null){
                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.READ_OBJ_SUCCESS);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.READ_OBJ_FAILED);
                    }
                    dos.writeUTF(toBeSent);
                    break;

                //delete the object if it exists and is locked someone else.
                case DELETE_OBJ_REQ:
                    Logger.debug("DELETE_OBJ_REQ Received");
                    req = (ObjectReq) MessageParser.deserializeObject(wrapper.getMessageBody());

                    oldAccount = node.getDataAccess().getAccount(req.getObjectId());

                    if(node.isObjectLocked(req.getObjectId(), req.getClientId())
                            && oldAccount != null){

                        //add this account to the temporary account list.
                        node.getTemporaryData().add(oldAccount);

//                        acc = node.getDataAccess().removeAccount(req.getObjectId());
                        accountMessage = new AccountMessage();
                        accountMessage.setServerId(req.getServerId());
                        accountMessage.setClientId(req.getClientId());
                        accountMessage.setAccount(oldAccount);

                        toBeSent = MessageParser.createWrapper(accountMessage, MessageType.DELETE_OBJ_SUCCESS);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(req, MessageType.DELETE_OBJ_FAILED);
                    }
                    dos.writeUTF(toBeSent);

                    break;
                case DELETE_OBJECT_PREPARE:
                    Logger.debug("DELETE_OBJECT_PREPARE Received");
                    req = (ObjectReq)MessageParser.deserializeObject(wrapper.getMessageBody());

                    temp = node.getTemporaryAccount(req.getObjectId());

                    if(temp == null){
                        toBeSent = MessageParser.createWrapper(req, MessageType.DELETE_OBJ_FAILED);
                    }
                    else{
                        toBeSent = MessageParser.createWrapper(req, MessageType.DELETE_OBJECT_PREPARE_ACK);
                        node.getTemporaryData().remove(temp);
                        node.addToPreparedData(temp);
                    }
                    dos.writeUTF(toBeSent);
                    node.freeObject(req.getObjectId(), req.getClientId());
                    break;

                case DELETE_OBJECT_ABORT:
                    Logger.debug("DELETE_OBJECT_ABORT received");
                    Logger.debug(node.getTemporaryData());
                    req = (ObjectReq)MessageParser.deserializeObject(wrapper.getMessageBody());
                    temp = node.getTemporaryAccount(req.getObjectId());
                    if(temp != null){
                        node.getTemporaryData().remove(temp);
                    }
                    temp = node.getPreparedAccount(req.getObjectId());
                    if(temp != null){
                        node.getPreparedAccess().removeAccount(temp.getId());
                    }
                    Logger.debug(node.getTemporaryData());
                    node.freeObject(req.getObjectId(), req.getClientId());
                    break;

                //TODO add the commit
                case DELETE_OBJECT_COMMIT:
                    Logger.debug("DELETE_OBJECT_COMMIT received");
                    req = (ObjectReq)MessageParser.deserializeObject(wrapper.getMessageBody());
                    acc = node.getPreparedAccount(req.getObjectId());

                    node.getDataAccess().removeAccount(acc.getId());
                    node.getPreparedData().remove(acc);
                    node.getPreparedAccess().removeAccount(acc.getId());
                    node.freeObject(req.getObjectId(), req.getClientId());
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
