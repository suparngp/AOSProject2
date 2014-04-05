package server;

import common.messages.Account;
import common.utils.DataAccess;

import java.util.Date;

/**
 * Created by suparngupta on 4/4/14.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        DataAccess access = new DataAccess();
        Account acc = new Account("1111");
        Date date = new Date();
        acc.setCreatedAt(date);
        acc.setCurrentBalance(10000.0);
        acc.setOpeningBalance(10000.0);
        acc.setOwnerName("Suparn");
        acc.setUpdatedAt(date);
        access.createAccount(acc);
        System.out.println(access.getAllAccounts());
    }
}
