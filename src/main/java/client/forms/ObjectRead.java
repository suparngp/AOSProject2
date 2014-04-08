package client.forms;

import common.Globals;
import common.messages.MessageType;
import common.messages.WrapperMessage;
import common.utils.Logger;
import common.utils.MessageParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by Nimisha on 7/4/14.
 */
public class ObjectRead {
    private String id;
    protected Socket socket;

    public ObjectRead(String id)
    {
        this.id=id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    /*
            seekReadPermission to randomly select one server and send read request.
             */
    public boolean seekReadPermission(String action) throws Exception{
        String objId=getId();
        int val=getHash(objId);
        String Message=null;

        for(int i=0; i<3;i++)
        {
            int hashVal=(val+i)%7;
            DataInputStream dis=null;
            DataOutputStream dos=null;
            try{
                socket= new Socket(Globals.serverHostNames.get(hashVal),Globals.serverClientPortNums.get(hashVal));

                if(action.equalsIgnoreCase("read"))
                    Message= MessageParser.createWrapper(this, MessageType.SEEK_READ_PERMISSION);
                if(action.equalsIgnoreCase("delete"))
                    Message=MessageParser.createWrapper(this, MessageType.SEEK_DELETE_PERMISSION);

                 dis = new DataInputStream(socket.getInputStream());
                 dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF(Message);
                dos.flush();

                String recMessage=dis.readUTF();
                WrapperMessage wrapper = MessageParser.parseMessageJSON(recMessage);

                if(wrapper.getMessageType() == MessageType.GRANT_READ_PERMISSION||
                        wrapper.getMessageType() == MessageType.GRANT_DELETE_PERMISSION){
                        break;

                }
                else return false;
            }
            catch (IOException e){
                Logger.log("Error connecting to server");
            }
            finally {
                socket.close();
                dis.close();
                dos.close();

            }

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
