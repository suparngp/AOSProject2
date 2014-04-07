package network;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.network.Node;
import server.network.ServerToClientChannel;

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

    /**
     * Method: destroyClientChannel()
     */
    @Test
    public void testDestroyClientChannel() throws Exception {
//TODO: Test goes here... 
    }

} 
