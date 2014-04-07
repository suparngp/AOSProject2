package network;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.network.Node;
import server.network.ServerToServerChannel;

import java.net.Socket;

/**
 * ServerToServerChannel Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 5, 2014</pre>
 */
public class ServerToServerChannelTest {

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
        ServerToServerChannel serverToServerChannel = new ServerToServerChannel(node);
        serverToServerChannel.init();
        Assert.assertTrue(serverToServerChannel.getServerToServerSocket() != null);
        Assert.assertEquals(serverToServerChannel.getServerToServerSocket().getLocalPort(), node.getServerPortNum());
        serverToServerChannel.destroyServerChannel();
    }

    /**
     * Method: run()
     */
    @Test
    public void testRun() throws Exception {
        Node node = new Node(1, "127.0.0.1", 9001, 10000);
        ServerToServerChannel serverToServerChannel = new ServerToServerChannel(node);
        serverToServerChannel.init();
        serverToServerChannel.start();
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", 9001);
        Assert.assertTrue(socket.isConnected());
        socket.close();
        Thread.sleep(1000);
        serverToServerChannel.destroyServerChannel();
    }
} 
