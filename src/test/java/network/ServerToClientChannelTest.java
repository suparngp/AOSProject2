package network;

import common.messages.*;
import common.utils.Logger;
import common.utils.MessageParser;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.network.Node;
import server.network.ServerToClientChannel;
import utils.Commons;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

/**
 * ServerToClientChannel Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 6, 2014</pre>
 */
public class ServerToClientChannelTest {

    private static HashSet<Socket> sockets = new HashSet<>();
    private static HashSet<ServerSocket> serverSockets = new HashSet<>();
    @Before
    public void before() throws Exception {
        for(Socket socket: sockets){
            if(!socket.isClosed()){
                socket.close();
            }
        }
        sockets.clear();

        for(ServerSocket serverSocket: serverSockets){
            if(!serverSocket.isClosed()){
                serverSocket.close();
            }
        }
        serverSockets.clear();
    }

    @After
    public void after() throws Exception {
    }



    /**
     * Method: init()
     */
    @Test
    public void testInit() throws Exception {
        Node node = new Node(1, "127.0.0.1", 9001, 10000);
        ServerToClientChannel serverToClientChannel = new ServerToClientChannel(node);
        serverToClientChannel.init();
        Assert.assertTrue(serverToClientChannel.getServerToClientSocket() != null);
        Assert.assertEquals(serverToClientChannel.getServerToClientSocket().getLocalPort(), node.getClientPortNum());
        serverToClientChannel.destroyClientChannel();
    }

    /**
     * Method: run()
     */
    @Test
    public void testRun() throws Exception {
        Node node = new Node(1, "127.0.0.1", 9001, 10000);
        ServerToClientChannel serverToClientChannel = new ServerToClientChannel(node);
        serverToClientChannel.init();
        serverToClientChannel.start();
        serverSockets.add(serverToClientChannel.getServerToClientSocket());
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        Assert.assertTrue(socket.isConnected());
        socket.close();
        Thread.sleep(1000);
        serverToClientChannel.destroyClientChannel();
    }

    @Test
    public void testSeekDelete() throws Exception {
        Node node = new Node(1, "127.0.0.1", 9001, 10000);
        ServerToClientChannel serverToClientChannel = new ServerToClientChannel(node);
        serverToClientChannel.init();
        serverToClientChannel.start();
        serverSockets.add(serverToClientChannel.getServerToClientSocket());
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        sockets.add(socket);
        Assert.assertTrue(socket.isConnected());
        String message = MessageParser.createWrapper(new ObjectReq("1234", 1, 2), MessageType.SEEK_DELETE_PERMISSION);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(message);
        dos.flush();
        dos.close();
        socket.close();
        Thread.sleep(1000);
        serverToClientChannel.destroyClientChannel();
    }

    @Test
    public void testSeekCreate() throws Exception {
        Node node = new Node(1, "127.0.0.1", 9001, 10000);
        ServerToClientChannel serverToClientChannel = new ServerToClientChannel(node);
        serverToClientChannel.init();
        serverToClientChannel.start();
        serverSockets.add(serverToClientChannel.getServerToClientSocket());
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        sockets.add(socket);
        Assert.assertTrue(socket.isConnected());
        String message = MessageParser.createWrapper(new ObjectReq("1234", 1, 2), MessageType.SEEK_CREATE_PERMISSION);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(message);
        dos.flush();

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String rec = dis.readUTF();
        Assert.assertEquals(MessageParser.parseMessageJSON(rec).getMessageType(), MessageType.GRANT_CREATE_PERMISSION);
        dos.close();
        socket.close();
        Thread.sleep(1000);
        serverToClientChannel.destroyClientChannel();
    }

    @Test
    public void testSeekUpdate() throws Exception {
        Node node = new Node(1, "127.0.0.1", 9001, 10000);
        ServerToClientChannel serverToClientChannel = new ServerToClientChannel(node);
        serverToClientChannel.init();
        serverToClientChannel.start();
        serverSockets.add(serverToClientChannel.getServerToClientSocket());
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        sockets.add(socket);
        Assert.assertTrue(socket.isConnected());
        String message = MessageParser.createWrapper(new ObjectReq("1234", 1, 2), MessageType.SEEK_UPDATE_PERMISSION);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(message);
        dos.flush();
        dos.close();
        socket.close();
        Thread.sleep(1000);
        serverToClientChannel.destroyClientChannel();
    }

    @Test
    public void testSeekRead() throws Exception {
        Node node = new Node(1, "127.0.0.1", 9001, 10000);
        ServerToClientChannel serverToClientChannel = new ServerToClientChannel(node);
        serverToClientChannel.init();
        serverToClientChannel.start();
        serverSockets.add(serverToClientChannel.getServerToClientSocket());
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        sockets.add(socket);
        Assert.assertTrue(socket.isConnected());
        String message = MessageParser.createWrapper(new ObjectReq("1234", 1, 2), MessageType.SEEK_READ_PERMISSION);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(message);
        dos.flush();
        dos.close();
        socket.close();
        Thread.sleep(1000);
        serverToClientChannel.destroyClientChannel();
    }

    @Test
    public void testCreateAccount() throws Exception {
        Node node = new Node(1, "127.0.0.1", 9001, 10000);
        ServerToClientChannel serverToClientChannel = new ServerToClientChannel(node);
        serverToClientChannel.init();
        serverToClientChannel.start();
        serverSockets.add(serverToClientChannel.getServerToClientSocket());
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        sockets.add(socket);
        Assert.assertTrue(socket.isConnected());
        AccountMessage message = new AccountMessage();
        message.setClientId(1);
        message.setServerId(1);

        Account acc = Commons.createRandomAccount();
        message.setAccount(acc);
        String toBeSent = MessageParser.createWrapper(new ObjectReq(acc.getId(), 1, 1), MessageType.SEEK_CREATE_PERMISSION);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(toBeSent);
        dos.flush();

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String rec = dis.readUTF();
        WrapperMessage wrapper = MessageParser.parseMessageJSON(rec);
        Assert.assertEquals(wrapper.getMessageType(), MessageType.GRANT_CREATE_PERMISSION);
        dis.close();

        Thread.sleep(1000);
        socket = new Socket("localhost", 10000);
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
        toBeSent = MessageParser.createWrapper(message, MessageType.CREATE_OBJ_REQ);
        dos.writeUTF(toBeSent);
        dos.flush();

        rec = dis.readUTF();
        wrapper = MessageParser.parseMessageJSON(rec);
        Assert.assertEquals(wrapper.getMessageType(), MessageType.CREATE_OBJ_SUCCESS);

        AccountMessage accountMessage = (AccountMessage) MessageParser.deserializeObject(wrapper.getMessageBody());
        Account recAcc = accountMessage.getAccount();

        Logger.debug(acc);
        Logger.debug(recAcc);
        Assert.assertTrue(recAcc.equals(acc));
        dos.close();
        dis.close();
        socket.close();
        Thread.sleep(1000);
        serverToClientChannel.destroyClientChannel();
    }
} 
