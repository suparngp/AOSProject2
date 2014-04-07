package utils;

import common.messages.Account;

import java.util.Date;
import java.util.Random;

/**
 * Created by suparngupta on 4/6/14.
 */
public class Commons {
    public static Account createRandomAccount() {
        Account acc = new Account(String.valueOf(new Random().nextLong()));
        acc.setOwnerName("Someone");
        Date date = new Date();
        acc.setUpdatedAt(date);
        acc.setCreatedAt(date);
        acc.setOpeningBalance(100000.0);
        acc.setCurrentBalance(100000.0);
        return acc;
    }
}
