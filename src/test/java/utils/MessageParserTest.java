package utils;

import common.messages.*;
import common.utils.MessageParser;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * MessageParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 6, 2014</pre>
 */
public class MessageParserTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: deserializeObject(byte[] buffer)
     */
    @Test
    public void testDeserializeObject() throws Exception {

        Account acc = Commons.createRandomAccount();
        byte[] buffer = MessageParser.serializeObject(acc);
        Assert.assertTrue(buffer != null);
        Assert.assertTrue(acc.getId().equals(((Account)MessageParser.deserializeObject(buffer)).getId()));

        //infomessage
        InfoMessage info = new InfoMessage(1, 2);
        buffer = MessageParser.serializeObject(info);
        Assert.assertTrue(buffer != null);
        Assert.assertTrue(info.equals((InfoMessage)MessageParser.deserializeObject(buffer)));

        ObjectReq req = new ObjectReq("1234", 1, 2);
        buffer = MessageParser.serializeObject(req);
        Assert.assertTrue(buffer != null);
        Assert.assertTrue(req.equals((ObjectReq)MessageParser.deserializeObject(buffer)));

        AccountMessage msg = new AccountMessage();
        buffer = MessageParser.serializeObject(msg);
        Assert.assertTrue(buffer != null);
        Assert.assertTrue(msg.equals((AccountMessage)MessageParser.deserializeObject(buffer)));
    }

    /**
     * Method: serializeObject(Object o)
     */
    @Test
    public void testSerializeObject() throws Exception {
// account message
        Account acc = Commons.createRandomAccount();
        byte[] buffer = MessageParser.serializeObject(acc);
        Assert.assertTrue(buffer != null);

        //infomessage
        InfoMessage info = new InfoMessage(1, 2);
        buffer = MessageParser.serializeObject(info);
        Assert.assertTrue(buffer != null);
    }

    /**
     * Method: createWrapper(Object o, MessageType type)
     */
    @Test
    public void testCreateWrapper() throws Exception {
        InfoMessage info = new InfoMessage(1, 2);
        String message = MessageParser.createWrapper(info, MessageType.SERVER_INTRO);
        Assert.assertTrue(message != null && !message.isEmpty());
    }

    /**
     * Method: parseMessageJSON(String json)
     */
    @Test
    public void testParseMessageJSON() throws Exception {
        InfoMessage info = new InfoMessage(1, 2);
        String message = MessageParser.createWrapper(info, MessageType.SERVER_INTRO);
        Assert.assertEquals(MessageType.SERVER_INTRO, MessageParser.parseMessageJSON(message).getMessageType());
    }
}
