package clientNetwork;

import client.clientNetwork.ClientNode;
import common.messages.Account;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

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
//TODO: Test goes here... 
    }

    /**
     * Method: createMultiServer(String id, String ownerName, Double opBal, Double curBal)
     */
    @Test
    public void testCreateMultiServer() throws Exception {
        ClientNode node = new ClientNode(0);
        String objectId = node.generateObjectId();
        String name = "Suparn";
        double openingBal = 100000;
        double currentBal = 100000;

        Account acc = node.createMultiServer(objectId, name, openingBal, currentBal);
    }

    /**
     * Method: updateMultiServer(String id, String ownerName, Double opBal, Double curBal)
     */
    @Test
    public void testUpdateMultiServer() throws Exception {
        ClientNode node = new ClientNode(0);
        String objectId = "1397191512400-0-1";
        String name = "Suprn";
        double openingBal = 98989897;
        double currentBal = 124;

        Account acc = node.updateMultiServer(objectId, name, openingBal, currentBal);
        System.out.println(acc);
    }

} 
