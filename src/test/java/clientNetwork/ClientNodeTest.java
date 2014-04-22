package clientNetwork;

import client.clientNetwork.ClientNode;
import common.messages.Account;
import common.messages.MutationType;
import common.utils.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.Commons;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * ClientNode Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 9, 2014</pre>
 */
public class ClientNodeTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getNodeId()
     */
    @Test
    public void testGetNodeId() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: setNodeId(int nodeId)
     */
    @Test
    public void testSetNodeId() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: create(String id, String ownerName, Double opBal, Double curBal)
     */
    @Test
    public void testCreate() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: read(String id)
     */
    @Test
    public void testRead() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: update(String id, String ownerName, Double opBal, Double curBal)
     */
    @Test
    public void testUpdate() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: delete(String id)
     */
    @Test
    public void testDelete() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: generateObjectId()
     */
    @Test
    public void testGenerateObjectId() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: computeServerId(String serverId)
     */
    @Test
    public void testComputeServerId() throws Exception {
        Socket socket = new Socket("127.0.0.1", 10000);
    }

    @Test
    public void testGetPrimaryInfo() throws Exception{
        ClientNode node = new ClientNode(0);
        String objectId = node.generateObjectId();
        int server1 = 0;
        int server2 = (server1 + 1) % 7;
        int server3 = (server2 + 1) % 7;
        List<Integer> servers = new ArrayList<>();
        servers.add(server1);
//        servers.add(server2);
//        servers.add(server3);
        node.getPrimaryServer(servers, objectId);
    }

    @Test
    public void testGetAvailableServers() throws Exception{
        ClientNode node = new ClientNode(0);
        String objectId = node.generateObjectId();
        Logger.debug(node.getAvailableServers(objectId));
    }

    @Test
    public void testSendMutationRequestCreate() throws Exception{
        long startTime = System.currentTimeMillis();
        ClientNode node = new ClientNode(90);
        Account acc = Commons.createRandomAccount();
        for(int i = 0; i < 100; i++){
            acc.setId(node.generateObjectId());
            node.sendMutationRequest(acc, MutationType.CREATE, acc.getId());
        }
        long end = System.currentTimeMillis();
        System.out.println("Time for 100 create requests" + (end - startTime));
    }
    @Test
    public void testSendMutationRequestUpdate() throws Exception{
        Account acc = Commons.createRandomAccount();
        ClientNode node = new ClientNode(90);
        acc.setId(node.generateObjectId());
        node.sendMutationRequest(acc, MutationType.CREATE, acc.getId());
        acc.setOwnerName("Suparn Random");
        node.sendMutationRequest(acc, MutationType.UPDATE, acc.getId());
    }
    @Test
    public void testSendMutationRequestDelete() throws Exception{
        Account acc = Commons.createRandomAccount();
        ClientNode node = new ClientNode(90);
        acc.setId("1398068100269-90-1");
        //node.sendMutationRequest(acc, MutationType.CREATE, acc.getId());
        node.sendMutationRequest(acc, MutationType.DELETE, acc.getId());
    }


    @Test
    public void testSendReadRequest() throws Exception{
        Account acc = Commons.createRandomAccount();
        ClientNode node = new ClientNode(90);
        acc.setId(node.generateObjectId());
        //node.sendMutationRequest(acc, MutationType.CREATE, acc.getId());
        node.sendReadRequest("1398073245660-90-1");
    }
} 
