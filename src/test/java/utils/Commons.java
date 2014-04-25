package utils;

import common.messages.Account;

import java.util.Date;
import java.util.Random;

/**
 * Created by suparngupta on 4/6/14.
 */
public class Commons {
    public static int counter = 1;
    public static Account createRandomAccount() {
        Account acc = new Account(String.valueOf(new Random().nextLong()));
        acc.setOwnerName("Object" + counter++);
        Date date = new Date();
        acc.setUpdatedAt(date);
        acc.setCreatedAt(date);
        acc.setOpeningBalance(1000.0 * counter);
        acc.setCurrentBalance(1000.0 * counter);
        return acc;
    }
}
