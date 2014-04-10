package client.clientNetwork;

import common.Globals;
import common.messages.*;
import common.utils.Logger;
import common.utils.MessageParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by Nimisha on 7/4/14.
 */
public class ClientNode {
    private int nodeId;
    private int objectIdCounter = 1;

    public ClientNode(int nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * Gets nodeId.
     *
     * @return Value of nodeId.
     */
    public int getNodeId() {
        return nodeId;
    }

    /**
     * Sets new nodeId.
     *
     * @param nodeId New value of nodeId.
     */
    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public Account create(String id, String ownerName, Double opBal, Double curBal) throws Exception {
        Account acc = new Account(id);
        acc.setId(id);
        acc.setOwnerName(ownerName);
        acc.setOpeningBalance(opBal);
        acc.setCurrentBalance(curBal);

        try {
            ObjectReq req;

            //Logger.debug("I am here!");
            String objectId = id;
            int serverId = this.computeServerId(objectId);
            Logger.debug("Sending request to server Id", serverId);
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
            int serverId = this.computeServerId(objectId);
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
                } else {
                    Logger.error("Account doesn't exist");
                }
            } else {
                Logger.log("Unable to read account");
            }
            return null;
        } catch (Exception e) {
            Logger.error("Error reading object");
        }
        return null;
    }

    public Account update(String id, String ownerName, Double opBal, Double curBal) throws Exception {

        Account acc = new Account(id);
        acc.setId(id);
        acc.setOwnerName(ownerName);
        acc.setOpeningBalance(opBal);
        acc.setCurrentBalance(curBal);

        try {
            ObjectReq req;

            Logger.debug("I am here!");
            String objectId = id;
            int serverId = this.computeServerId(objectId);
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
        } catch (IOException e) {
            Logger.error("Error updating object");
        }

        return null;
    }

    public Account delete(String id) {
        try {
            ObjectReq req;

            Logger.debug("I am here!");
            String objectId = id;
            int serverId = this.computeServerId(objectId);
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
        } catch (Exception e) {
            Logger.error("Error deleting object");
        }
        return null;
    }

    /**
     * Generates the bject id of the new object.
     * Each object id is a combination of current system time - nodeId - and a counter value.
     *
     * @return
     */
    public synchronized String generateObjectId() {
        if (objectIdCounter == 10) {
            objectIdCounter = 1;
        }

        String objectId = "";
        objectId += System.currentTimeMillis() + "-" + this.nodeId + "-" + this.objectIdCounter;
        this.objectIdCounter++;
        return objectId;
    }

    /**
     * Computes the server id
     *
     * @param serverId the objectId
     * @return the server is
     */
    //TODO update the code.
    public synchronized int computeServerId(String serverId) {
        return 0;
//
//        if(serverId == null || serverId.isEmpty()){
//            return 0;
//        }
//
//        int sum = 0;
//        for(int i = 0; i < serverId.length(); i++){
//            sum += i;
//        }
//        return sum % 7;
    }


    public Account createMultiServer(String id, String ownerName, Double opBal, Double curBal) throws Exception {
        Account acc = new Account(id);
        acc.setId(id);
        acc.setOwnerName(ownerName);
        acc.setOpeningBalance(opBal);
        acc.setCurrentBalance(curBal);

        try {
            ObjectReq req;

            //Logger.debug("I am here!");
            String objectId = id;
            int clientId = this.getNodeId();
            int baseServerId = this.computeServerId(objectId);
            int[] servers = {baseServerId, (baseServerId + 1) % 7, (baseServerId + 2) % 7};
            DataInputStream dis;
            DataOutputStream dos;
            HashSet<Integer> successServers = new HashSet<>();
            String toBeSent;
            String hostName;
            int portNum;
            //send the seek create permission to all the servers
            int successCounter = 0;

            for (int serverId : servers) {
                Logger.log("Sending request to server Id", serverId);
                req = new ObjectReq(objectId, serverId, clientId);
                toBeSent = MessageParser.createWrapper(req, MessageType.SEEK_CREATE_PERMISSION);
                hostName = Globals.serverHostNames.get(serverId);
                portNum = Globals.serverClientPortNums.get(serverId);

                //create the socket
                try {
                    Socket socket = new Socket(hostName, portNum);
                    //suppose socket is connected to the server.
                    dis = new DataInputStream(socket.getInputStream());
                    dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF(toBeSent);
                    dos.flush();
                    String recMessage = dis.readUTF();
                    WrapperMessage wrapper = MessageParser.parseMessageJSON(recMessage);
                    Logger.debug(wrapper.getMessageType());
                    Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
                    dis.close();
                    dos.close();

                    if (wrapper.getMessageType() == MessageType.GRANT_CREATE_PERMISSION) {
                        successCounter++;
                        successServers.add(serverId);
                    }
                } catch (Exception e) {
                    Logger.log("Unable to connect to server ", serverId, e);
                }
            }
            //if less than 2 servers are available, flag error and exit.
            if (successCounter < 2) {
                Logger.error("Unable to create object because only " + successCounter + " server(s) is(are) available");
                successCounter = 0;
                successServers.clear();
                return null;
            }

            Account finalAcc = null;
            HashSet<Integer> step2Servers = new HashSet<>();
            for (int serverId : successServers) {
                AccountMessage accMsg = new AccountMessage(serverId, clientId, acc);
                toBeSent = MessageParser.createWrapper(accMsg, MessageType.CREATE_OBJ_REQ);

                hostName = Globals.serverHostNames.get(serverId);
                portNum = Globals.serverClientPortNums.get(serverId);

                try {
                    Socket socket = new Socket(hostName, portNum);
                    dis = new DataInputStream(socket.getInputStream());
                    dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF(toBeSent);
                    dos.flush();

                    //receive reply from server.
                    //if it is Create success then return the object created
                    String recMessage = dis.readUTF();
                    WrapperMessage wrapper = MessageParser.parseMessageJSON(recMessage);
                    Logger.debug(wrapper.getMessageType());

                    Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
                    dis.close();
                    dos.close();

                    //if returned message is success then return the object
                    if (wrapper.getMessageType() == MessageType.CREATE_OBJ_SUCCESS) {
                        accMsg = (AccountMessage) MessageParser
                                .deserializeObject(wrapper.getMessageBody());
                        finalAcc = accMsg.getAccount();
                        successCounter++;
                        step2Servers.add(serverId);
                    }
                } catch (Exception e) {
                    Logger.error("Unable to send create req to a previously working server!", e);
                }

            }

            //check if the #step1 servers is equal to #step2 servers, else, do an abort.
            if(step2Servers.size() != successServers.size()){
                Logger.error("Some of the step 1 servers were unable to create object", successServers, step2Servers);
            }

            return finalAcc;
        } catch (Exception e) {
            Logger.error("Error creating object", e);
        }

        return acc;
    }
}
