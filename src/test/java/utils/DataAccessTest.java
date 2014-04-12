package utils;

import common.messages.Account;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.data.DataAccess;

/**
 * DataAccess Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Apr 4, 2014</pre>
 */
public class DataAccessTest {

    private String testName = "";

    @Before
    public void before() throws Exception {
        new DataAccess("data.txt").clearAllData();
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: createAccount(Account account)
     */
    @Test
    public void testCreateAccount() throws Exception {

        Account acc = Commons.createRandomAccount();
        DataAccess access = new DataAccess("data.txt");
        acc = access.createAccount(acc);
        Account stored = access.getAccount(acc.getId());

        Assert.assertTrue(stored != null);
        Assert.assertEquals(stored.getCreatedAt(), acc.getCreatedAt());
        Assert.assertEquals(stored.getOwnerName(), acc.getOwnerName());
        Assert.assertEquals(stored.getCurrentBalance(), acc.getCurrentBalance());
        Assert.assertEquals(stored.getOpeningBalance(), acc.getOpeningBalance());
        Assert.assertEquals(stored.getUpdatedAt(), acc.getUpdatedAt());
        Assert.assertEquals(stored.getId(), acc.getId());

        access.removeAccount(acc.getId());
    }

    /**
     * Method: updateAccount(Account updatedAccount)
     */
    @Test
    public void testUpdateAccount() throws Exception {

        Account acc = Commons.createRandomAccount();

        DataAccess access = new DataAccess("data.txt");
        access.createAccount(acc);
        double prev = acc.getCurrentBalance();
        acc.setCurrentBalance(20000.0);
        access.updateAccount(acc);

        Account stored = access.getAccount(acc.getId());
        Assert.assertTrue(stored != null);
        Assert.assertTrue(prev != stored.getCurrentBalance());
        Assert.assertEquals(acc.getCurrentBalance(), stored.getCurrentBalance());
        access.removeAccount(acc.getId());
    }

    /**
     * Method: removeAccount(String id)
     */
    @Test
    public void testRemoveAccount() throws Exception {

        Account acc = Commons.createRandomAccount();
        DataAccess access = new DataAccess("data.txt");
        access.createAccount(acc);
        access.removeAccount(acc.getId());

        Assert.assertEquals(access.getAccount(acc.getId()), null);

        Assert.assertEquals(access.getAllAccounts().size(), 0);

        access.clearAllData();
    }

    /**
     * Method: getAccount(String id)
     */
    @Test
    public void testGetAccount() throws Exception {

        Account acc = Commons.createRandomAccount();
        DataAccess access = new DataAccess("data.txt");
        access.createAccount(acc);
        Account stored = access.getAccount(acc.getId());

        Assert.assertTrue(stored != null);
        Assert.assertEquals(stored.getCreatedAt(), acc.getCreatedAt());
        Assert.assertEquals(stored.getOwnerName(), acc.getOwnerName());
        Assert.assertEquals(stored.getCurrentBalance(), acc.getCurrentBalance());
        Assert.assertEquals(stored.getOpeningBalance(), acc.getOpeningBalance());
        Assert.assertEquals(stored.getUpdatedAt(), acc.getUpdatedAt());
        Assert.assertEquals(stored.getId(), acc.getId());
        access.clearAllData();

    }

    /**
     * Method: getAllAccounts()
     */
    @Test
    public void testGetAllAccounts() throws Exception {
        Account[] accounts = {Commons.createRandomAccount(), Commons.createRandomAccount(), Commons.createRandomAccount()};

        DataAccess access = new DataAccess("data.txt");
        for (Account acc : accounts) {
            access.createAccount(acc);
        }
        Assert.assertEquals(3, access.getAllAccounts().size());
    }

    @Test
    public void testClearAllData() throws Exception {
        DataAccess access = new DataAccess("data.txt");
        access.clearAllData();
        Assert.assertEquals(access.getAllAccounts().isEmpty(), true);
    }

    
}
