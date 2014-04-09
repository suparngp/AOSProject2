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

/**
 * @author Nimisha
 *
 */

public class UpdateForm{
//
    String id, ownerName;
    Double currentBalance;
    private Gui parent;


    public UpdateForm(Gui gui) {
        this.parent=gui;

        gui.lblMessage.setText("Enter details to seek permission to update object");
        gui.lblAccountId.setVisible(true);
        gui.lblName.setVisible(true);
        gui.txtId.setVisible(true);
        gui.txtName.setVisible(true);
        gui.btnSubmit.setVisible(true);
        gui.btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to read the object name and update it

                //updateObject();
            }});

    }
    /**
     *
     */
    protected void updateObject() throws Exception{
        // TODO Auto-generated method stub
        //seek permission to update object.
        try {
            ObjectReq req;

            Logger.debug("I am here!");
            String objectId = this.parent.txtId.getText();
            int serverId = Utils.computeObjectHash(objectId);
            int clientId = this.parent.getClientNode().getNodeId();

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

            String receivedString = dis.readUTF();
            WrapperMessage wrapper = MessageParser.parseMessageJSON(receivedString);
            Logger.debug(wrapper.getMessageType());
            Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
            dis.close();
            dos.close();

            //if permission received in above, send read request so that form fields can be set for updating


            toBeSent= MessageParser.createWrapper(req, MessageType.READ_OBJ_REQ);
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

           //the above returns the account object which is requested
            //now set the fields in the form so client can update them.

            AccountMessage accountMessage = (AccountMessage)MessageParser
                    .deserializeObject(wrapper.getMessageBody());
            Account acc=accountMessage.getAccount();

//            this.parent.txtId.setText(acc.getId());
//            this.parent.txtName.setText(acc.getOwnerName());
//            this.parent.txtCurbal.setText(acc.getCurrentBalance().toString());
//            this.parent.btnSubmit.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    //some function to read the object name and update it
//            )}


//});

        } catch (Exception e) {
            Logger.error("Unable to send Create Object Permission", e);
        }



















//        ObjectRead read= new ObjectRead(txtId.getText());
//        if(read.seekReadPermission("read"))
//        {
//
//        }
//
//
//        // once permission is granted, retrieve results and set text in the fields
//        lblMessage.setText("Enters details to update");
//        lblAccountId.setVisible(true);
//        lblName.setVisible(true);
//        lblOpeningBalance.setVisible(true);
//        lblCurrentBalance.setVisible(true);
//        lblCreatedAt.setVisible(true);
//        lblUpdatedAt.setVisible(true);
//        txtId.setVisible(true);
//        txtName.setVisible(true);
//        txtOpbal.setVisible(true);
//        txtCurbal.setVisible(true);
//        txtCreated.setVisible(true);
//        txtUpdated.setVisible(true);
//        btnSubmit.setVisible(true);
//
//        //get the account details where id=object id from update form
//        //and set all its details in the fields
//
//
//
//        btnSubmit.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                //some function to send updated details to the server
//
//                updateSend();
//            }});
//
//    }
//    /**
//     *
//     */
//    protected void updateSend() {
//        // TODO Auto-generated method stub
//        //method to send the updated values
//        id=txtId.getText();
//        ownerName=txtName.getText();
//        String bal=txtOpbal.getText();
//        openingBalance=Double.parseDouble(bal);
//        Account account=new Account(id);
//        account.setId(id);
//        account.setOwnerName(ownerName);
//        account.setOpeningBalance(openingBalance);
//        account.setCurrentBalance(openingBalance);
//        createdAt= new Date();
//        account.setCreatedAt(createdAt);
//        account.setUpdatedAt(createdAt);
//        //seek permission and update object
//
//        ObjectCreate obj= new ObjectCreate(id,ownerName, account, "update");
//        if(obj.seekPermission(id)){
//            lblMessage.setText("Object Updated");
//            lblAccountId.setVisible(false);
//            lblName.setVisible(false);
//            lblOpeningBalance.setVisible(false);
//            txtId.setVisible(false);
//            txtName.setVisible(false);
//            txtOpbal.setVisible(false);
//            btnSubmit.setVisible(false);
//
//        }
 }

}
