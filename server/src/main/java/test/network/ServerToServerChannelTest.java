package test.network;

import common.utils.Globals;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
        ServerToServerChannel serverToServerChannel = new ServerToServerChannel();
        serverToServerChannel.init();
        Assert.assertTrue(serverToServerChannel.getServerToServerSocket() != null);
        Assert.assertEquals(serverToServerChannel.getServerToServerSocket().getLocalPort(), Globals.serverChannelPort);
        serverToServerChannel.destroyServerChannel();
    }

    /**
     * Method: run()
     */
    @Test
    public void testRun() throws Exception {
        ServerToServerChannel serverToServerChannel = new ServerToServerChannel();
        serverToServerChannel.init();
        serverToServerChannel.start();
        Thread.sleep(1000);
        Socket socket = new Socket("localhost", Globals.serverChannelPort);
        Assert.assertTrue(socket.isConnected());
        socket.close();
        Thread.sleep(1000);
        serverToServerChannel.destroyServerChannel();
    }
} 
