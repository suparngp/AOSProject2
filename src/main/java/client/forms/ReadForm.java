package client.forms; /**
 *
 */

import common.Globals;
import common.messages.MessageType;
import common.messages.ObjectReq;
import common.messages.WrapperMessage;
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

public class ReadForm{

    String id, ownerName;
    private Gui parent;

    public ReadForm(Gui gui) {

        this.parent=gui;
        gui.lblAccountId.setVisible(true);
        gui.lblName.setVisible(true);
        gui.txtId.setVisible(true);
        gui.txtName.setVisible(true);
        gui.btnSubmit.setVisible(true);
        gui.btnSubmit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //some function to read the object and display result
                //readObject();
            }});
        gui.lblMessage.setText("Enter details to read object values.");

    }
    /**
     *
     */
    protected void readObject() throws Exception{
        try {
            ObjectReq req;

            Logger.debug("I am here!");
            String objectId = this.parent.txtId.getText();
            int serverId = Utils.computeObjectHash(objectId);
            int clientId = this.parent.getClientNode().getNodeId();

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

            String receivedString = dis.readUTF();
            WrapperMessage wrapper = MessageParser.parseMessageJSON(receivedString);
            Logger.debug(wrapper.getMessageType());
            Logger.debug(MessageParser.deserializeObject(wrapper.getMessageBody()));
            dis.close();
            dos.close();
            //in the above permission granted to read object.

            //create new socket and send request to read
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


        }
        catch (Exception e)
        {

        }



    }

//    protected void readObject() {
//        // TODO Auto-generated method stub
//        //sends object Id to server to seek permission to read and retrieve data
//        //once permission is granted and information is retrieved then the fields become visible
//
//
//
//
//        lblMessage.setText("Object values are: ");
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
//        btnSubmit.setVisible(false);
//
//    }

}
