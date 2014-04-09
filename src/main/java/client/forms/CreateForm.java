package client.forms; /**
 *
 */

import common.Globals;
import common.messages.*;
import common.utils.Logger;
import common.utils.MessageParser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * @author Nimisha
 *         This displays a form to enter object details so that a new object is created.
 */

class CreateForm {

    String id, ownerName;
    Double openingBalance;
    Date createdAt;
    private Gui parent;

    public CreateForm(Gui gui) {
        this.parent = gui;
        gui.lblAccountId.setVisible(true);
        gui.lblName.setVisible(true);
        gui.lblOpeningBalance.setVisible(true);
        //lblCreatedAt.setVisible(true);
        gui.txtId.setVisible(true);
        gui.txtName.setVisible(true);
        gui.txtOpbal.setVisible(true);
        //txtCreated.setVisible(true);
        gui.btnSubmit.setVisible(true);
        gui.lblMessage.setText("Enter details to create object");


        gui.btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to read the object and display result
                createObject();
            }
        });
    }

    /**
     * 1. The first step is to seek the permission to create the object. (ObjectReq)
     * 2.
     */
    protected void createObject() {
        // TODO Auto-generated method stub

        try {
            ObjectReq req;

            Logger.debug("I am here!");
            String objectId = this.parent.txtId.getText();
            int serverId = Utils.computeObjectHash(objectId);
            int clientId = this.parent.getClientNode().getNodeId();

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

            String receivedString = dis.readUTF();
            WrapperMessage wrapper = MessageParser.parseMessageJSON(receivedString);
            Logger.debug(wrapper.getMessageType());
            Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
            dis.close();
            dos.close();


            //create a fresh socket and get its streams.
            Account account = new Account(objectId);
            id = this.parent.txtId.getText();
            ownerName = this.parent.txtName.getText();
            String bal = this.parent.txtOpbal.getText();
            openingBalance = Double.parseDouble(bal);
            account.setId(id);
            account.setOwnerName(ownerName);
            account.setOpeningBalance(openingBalance);
            account.setCurrentBalance(openingBalance);

            AccountMessage accMsg = new AccountMessage(serverId, clientId, account);
            toBeSent = MessageParser.createWrapper(accMsg, MessageType.CREATE_OBJ_REQ);

            hostName = Globals.serverHostNames.get(serverId);
            portNum = Globals.serverClientPortNums.get(serverId);

            socket = new Socket(hostName, portNum);

            //suppose socket is connected to the server.

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(toBeSent);
            dos.flush();

            receivedString = dis.readUTF();
            wrapper = MessageParser.parseMessageJSON(receivedString);
            Logger.debug(wrapper.getMessageType());
            Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
            dis.close();
            dos.close();

        } catch (Exception e) {
            Logger.error("Unable to send Create Object Permission", e);
        }
    }
}
