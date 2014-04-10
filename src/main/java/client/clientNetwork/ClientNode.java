package client.clientNetwork;

import client.forms.Utils;
import common.Globals;
import common.messages.*;
import common.utils.Logger;
import common.utils.MessageParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Nimisha on 7/4/14.
 */
public class ClientNode {
    private int clientNodeId;

    public ClientNode(int clientNodeId) {
        this.clientNodeId = clientNodeId;
    }

    /**
     * Gets nodeId.
     *
     * @return Value of nodeId.
     */
    public int getNodeId() {
        return clientNodeId;
    }

    /**
     * Sets new nodeId.
     *
     * @param nodeId New value of nodeId.
     */
    public void setNodeId(int nodeId) {
        this.clientNodeId = nodeId;
    }

    public Account create(String id, String ownerName, Double opBal, Double curBal) throws Exception {
        Account acc = new Account(id);
        acc.setId(id);
        acc.setOwnerName(ownerName);
        acc.setOpeningBalance(opBal);
        acc.setCurrentBalance(curBal);

        try {
            ObjectReq req;

            Logger.debug("I am here!");
            String objectId = id;
            int serverId = Utils.computeObjectHash(objectId);
            int clientId = this.getNodeId();

            req = new ObjectReq(objectId, serverId, clientId);
            String toBeSent = MessageParser.createWrapper(req, MessageType.SEEK_CREATE_PERMISSION);

            String hostName = Globals.serverHostNames.get(serverId);
            int portNum = Globals.serverClientPortNums.get(serverId);

            Socket socket = new Socket(hostName, portNum);

            //suppose socket is connected to the server.

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(toBeSent);
            dos.flush();

            //read reply from server
            String recMessage = dis.readUTF();
            WrapperMessage wrapper = MessageParser.parseMessageJSON(recMessage);
            Logger.debug(wrapper.getMessageType());
            Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
            dis.close();
            dos.close();
            if (wrapper.getMessageType() == MessageType.GRANT_CREATE_PERMISSION) {
                //send create object request
                AccountMessage accMsg = new AccountMessage(serverId, clientId, acc);
                toBeSent = MessageParser.createWrapper(accMsg, MessageType.CREATE_OBJ_REQ);

                hostName = Globals.serverHostNames.get(serverId);
                portNum = Globals.serverClientPortNums.get(serverId);

                socket = new Socket(hostName, portNum);

                //suppose socket is connected to the server.

                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(toBeSent);
                dos.flush();

                //receive reply from server.
                //if it is Create success then return the object created
                recMessage = dis.readUTF();
                wrapper = MessageParser.parseMessageJSON(recMessage);
                Logger.debug(wrapper.getMessageType());
                Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
                dis.close();
                dos.close();
                if (wrapper.getMessageType() == MessageType.CREATE_OBJ_SUCCESS) {
                    //if returned message is success then return the object
                    accMsg = (AccountMessage) MessageParser
                            .deserializeObject(wrapper.getMessageBody());
                    return accMsg.getAccount();
                }
                return null;


            }
        } catch (Exception e) {
            Logger.error("Error creating object");
        }

        return acc;
    }

    public Account read(String id) throws Exception {
        try {
            ObjectReq req;

            Logger.debug("I am here!");
            String objectId = id;
            int serverId = Utils.computeObjectHash(objectId);
            int clientId = this.getNodeId();

            req = new ObjectReq(objectId, serverId, clientId);
            String toBeSent = MessageParser.createWrapper(req, MessageType.SEEK_READ_PERMISSION);

            String hostName = Globals.serverHostNames.get(serverId);
            int portNum = Globals.serverClientPortNums.get(serverId);

            Socket socket = new Socket(hostName, portNum);

            //suppose socket is connected to the server.

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(toBeSent);
            dos.flush();

            //read reply from server
            String receivedString = dis.readUTF();
            WrapperMessage wrapper = MessageParser.parseMessageJSON(receivedString);
            Logger.debug(wrapper.getMessageType());
            Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
            dis.close();
            dos.close();

            if (wrapper.getMessageType() == MessageType.GRANT_READ_PERMISSION) {
                //send create object request

                toBeSent = MessageParser.createWrapper(req, MessageType.READ_OBJ_REQ);

                hostName = Globals.serverHostNames.get(serverId);
                portNum = Globals.serverClientPortNums.get(serverId);

                socket = new Socket(hostName, portNum);

                //suppose socket is connected to the server.

                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(toBeSent);
                dos.flush();

                //read reply from server;
                //server returns requested object

                receivedString = dis.readUTF();
                wrapper = MessageParser.parseMessageJSON(receivedString);
                Logger.debug(wrapper.getMessageType());
                Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
                dis.close();
                dos.close();
                if (wrapper.getMessageType() == MessageType.READ_OBJ_SUCCESS) {
                    //if returned message is success then return the object
                    AccountMessage accMsg = (AccountMessage) MessageParser
                            .deserializeObject(wrapper.getMessageBody());

                    return accMsg.getAccount();
                }
            }
            return null;
        } catch (Exception e) {
            Logger.error("Error reading object");
        }
        return null;
    }

    public Account update(String id, String ownerName, Double opBal, Double curBal) throws Exception{

        Account acc = new Account(id);
        acc.setId(id);
        acc.setOwnerName(ownerName);
        acc.setOpeningBalance(opBal);
        acc.setCurrentBalance(curBal);

        try {
            ObjectReq req;

            Logger.debug("I am here!");
            String objectId = id;
            int serverId = Utils.computeObjectHash(objectId);
            int clientId = this.getNodeId();

            req = new ObjectReq(objectId, serverId, clientId);
            String toBeSent = MessageParser.createWrapper(req, MessageType.SEEK_UPDATE_PERMISSION);

            String hostName = Globals.serverHostNames.get(serverId);
            int portNum = Globals.serverClientPortNums.get(serverId);

            Socket socket = new Socket(hostName, portNum);

            //suppose socket is connected to the server.

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(toBeSent);
            dos.flush();

            //read reply from server
            String recMessage = dis.readUTF();
            WrapperMessage wrapper = MessageParser.parseMessageJSON(recMessage);
            Logger.debug(wrapper.getMessageType());
            Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
            dis.close();
            dos.close();
            if (wrapper.getMessageType() == MessageType.GRANT_UPDATE_PERMISSION) {
                //send update object request
                AccountMessage accMsg = new AccountMessage(serverId, clientId, acc);
                toBeSent = MessageParser.createWrapper(accMsg, MessageType.UPDATE_OBJ_REQ);

                hostName = Globals.serverHostNames.get(serverId);
                portNum = Globals.serverClientPortNums.get(serverId);

                socket = new Socket(hostName, portNum);

                //suppose socket is connected to the server.

                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(toBeSent);
                dos.flush();

                //receive reply from server.
                //if it is update success then return the object created
                recMessage = dis.readUTF();
                wrapper = MessageParser.parseMessageJSON(recMessage);
                Logger.debug(wrapper.getMessageType());
                Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
                dis.close();
                dos.close();
                if (wrapper.getMessageType() == MessageType.UPDATE_OBJ_SUCCESS) {
                    //if returned message is success then return the object
                    accMsg = (AccountMessage) MessageParser
                            .deserializeObject(wrapper.getMessageBody());
                    return accMsg.getAccount();
                }
            }
        }
        catch (IOException e){
            Logger.error("Error updating object");
        }

        return null;
    }

    public Account delete(String id){
        try{
            ObjectReq req;

            Logger.debug("I am here!");
            String objectId = id;
            int serverId = Utils.computeObjectHash(objectId);
            int clientId = this.getNodeId();

            req = new ObjectReq(objectId, serverId, clientId);
            String toBeSent = MessageParser.createWrapper(req, MessageType.SEEK_DELETE_PERMISSION);

            String hostName = Globals.serverHostNames.get(serverId);
            int portNum = Globals.serverClientPortNums.get(serverId);

            Socket socket = new Socket(hostName, portNum);

            //suppose socket is connected to the server.

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(toBeSent);
            dos.flush();

            //read reply from server
            String receivedString = dis.readUTF();
            WrapperMessage wrapper = MessageParser.parseMessageJSON(receivedString);
            Logger.debug(wrapper.getMessageType());
            Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
            dis.close();
            dos.close();

            if (wrapper.getMessageType() == MessageType.GRANT_DELETE_PERMISSION) {
                //send create object request

                toBeSent = MessageParser.createWrapper(req, MessageType.DELETE_OBJ_REQ);

                hostName = Globals.serverHostNames.get(serverId);
                portNum = Globals.serverClientPortNums.get(serverId);

                socket = new Socket(hostName, portNum);

                //suppose socket is connected to the server.

                dis = new DataInputStream(socket.getInputStream());
                dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(toBeSent);
                dos.flush();

                //read reply from server;
                //server returns requested object

                receivedString = dis.readUTF();
                wrapper = MessageParser.parseMessageJSON(receivedString);
                Logger.debug(wrapper.getMessageType());
                Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
                dis.close();
                dos.close();
                if (wrapper.getMessageType() == MessageType.DELETE_OBJ_SUCCESS) {
                    //if returned message is success then return the object
                    AccountMessage accMsg = (AccountMessage) MessageParser
                            .deserializeObject(wrapper.getMessageBody());

                    return accMsg.getAccount();
                }
            }
            return null;
        }
        catch (Exception e){
            Logger.error("Error deleting object");
        }
        return null;
    }


}
