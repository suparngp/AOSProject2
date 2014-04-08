package client.forms;

import common.messages.Account;
import common.messages.MessageType;
import common.messages.WrapperMessage;
import common.utils.Logger;
import common.utils.MessageParser;
import server.Globals;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Common class for create and read objects.
 * Used to seek permission to create and read objects.
 * Has functions to set object values on creation and
 * get object values on read request
 * Created by Nimisha on 6/4/14.
 */
public class ObjectCreate {
    private String id, ownerName, action;
    protected Socket socket;
    private Account account;

    public ObjectCreate(String id, String ownerName, Account account, String action){
        this.id=id;
        this.ownerName=ownerName;
        this.account=account;
        this.action=action;
    }
    /*
    Function to obtain permission from servers
    returns true if at least 2 grants have been received
     */
    public boolean seekPermission(String id){
        int val= getHash(id);
        ArrayList serverTrack=new ArrayList<Integer>();//to store the server ids that have granted permission

        int count=0;
        for(int i=0;i<3;i++)
        {
            int hashVal=(val+i)%7;
            if(sendSeekPermission(hashVal))
            {
                count++;
                serverTrack.add(hashVal);
            }
        }
        if(count<2)
        {
            Logger.log("Error connecting to servers. Please Try Again.");
        }
        else
        {
            int createSuccessCount=0; String Message=null;
            for(int i=0;i<serverTrack.size();i++){

                try{
                    socket= new Socket(Globals.serverHostNames.get(serverTrack.get(i)),Globals.serverClientPortNums.get(serverTrack.get(i)));

                    if(action.equalsIgnoreCase("create"))
                         Message= MessageParser.createWrapper(account, MessageType.CREATE_OBJ_REQ);
                    if(action.equalsIgnoreCase("update"))
                        Message= MessageParser.createWrapper(account, MessageType.UPDATE_OBJ_REQ);

                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    dos.writeUTF(Message);
                    dos.flush();

                    String received=dis.readUTF();
                    WrapperMessage wrapper = MessageParser.parseMessageJSON(received);
                    if(wrapper.getMessageType() == MessageType.CREATE_OBJ_SUCCESS||
                            wrapper.getMessageType() ==MessageType.UPDATE_OBJ_SUCCESS)
                        createSuccessCount++;
                }
                catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(createSuccessCount<2)
                Logger.log("Could not create object. Error in connection");
            else
                return true;
        }

        return false;
    }

    private boolean sendSeekPermission(int i) {
        /*
        code to connect to server port and send Seek permission
         */
        int serverPort=i;
        String Message=null;
        try {
            socket= new Socket(Globals.serverHostNames.get(serverPort),Globals.serverClientPortNums.get(serverPort));

            if(action.equalsIgnoreCase("create"))
             Message= MessageParser.createWrapper(this, MessageType.SEEK_CREATE_PERMISSION);
            if(action.equalsIgnoreCase("update"))
                Message=MessageParser.createWrapper(this, MessageType.SEEK_UPDATE_PERMISSION);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(Message);
            dos.flush();

            String recMessage=dis.readUTF();
            WrapperMessage wrapper = MessageParser.parseMessageJSON(recMessage);
            if(wrapper.getMessageType() == MessageType.GRANT_CREATE_PERMISSION||wrapper.getMessageType() == MessageType.GRANT_UPDATE_PERMISSION){
                return true;
            }
            else
                return false;
        } catch (IOException e) {
            Logger.error("Error connecting to server");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /*
    Hash function on the object id
    returns int value hash
     */
    private int getHash(String id) {
        int hash= Integer.parseInt(id);
        return hash;
    }

}
