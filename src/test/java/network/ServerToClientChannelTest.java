package network;

import common.messages.InfoMessage;
import common.messages.MessageType;
import common.messages.ObjectReq;
import common.utils.MessageParser;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.network.Node;
import server.network.ServerToClientChannel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * ServerToClientChannel Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 6, 2014</pre>
 */
public class ServerToClientChannelTest {

    @Before
    public void before() throws Exception {
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
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        Assert.assertTrue(socket.isConnected());
        String message = MessageParser.createWrapper(new InfoMessage(1, 2), MessageType.SEEK_DELETE_PERMISSION);
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
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        Assert.assertTrue(socket.isConnected());
        String message = MessageParser.createWrapper(new ObjectReq(), MessageType.SEEK_CREATE_PERMISSION);
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
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        Assert.assertTrue(socket.isConnected());
        String message = MessageParser.createWrapper(new InfoMessage(1, 2), MessageType.SEEK_UPDATE_PERMISSION);
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
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 10000);
        Assert.assertTrue(socket.isConnected());
        String message = MessageParser.createWrapper(new InfoMessage(1, 2), MessageType.SEEK_READ_PERMISSION);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(message);
        dos.flush();
        dos.close();
        socket.close();
        Thread.sleep(1000);
        serverToClientChannel.destroyClientChannel();
    }
} 
