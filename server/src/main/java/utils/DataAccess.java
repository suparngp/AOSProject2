package utils;

import com.google.gson.Gson;
import messages.Account;

import java.io.*;
import java.util.HashSet;

/**
 * Data Access API to perform CRUD operations on the data file.
 * Created by suparngupta on 4/4/14.
 */
public class DataAccess extends Thread {
    private File dataFile = new File(Globals.dataFileName);

    private HashSet<Account> accountList = new HashSet<Account>();

    private BufferedReader br;

    /**
     * Constructor to initialize the data access API.
     * Loads all the records in the memory
     */
    public DataAccess() {

        try {
            /**
             * Load all the records in the memory.
             * */
            br = new BufferedReader(new FileReader(dataFile));
            Gson gson = new Gson();

            String input = br.readLine();
            while (input != null) {
                Account acc = gson.fromJson(input, Account.class);
                accountList.add(acc);
            }
        } catch (Exception e) {
            Logger.error("Unable to initialize the Data Access Layer", e);
        }
    }

    /**
     * Creates a new account and writes it to the data file.
     *
     * @param account the new account object to be added
     * @return the new account as passed
     * @throws Exception if creation fails
     */
    public Account createAccount(Account account) throws Exception {
        if (getAccount(account.getId()) == null) {
            Logger.error("Account with a duplicate ID cannot be created", account);
            throw new Exception();
        }

        this.accountList.add(account);
        this.flushLatestAccountData();
        return account;
    }

    /**
     * Updates an existing account
     *
     * @param updatedAccount the update Account POJO
     * @return the update Account POJO as passed
     * @throws Exception if update fails
     */
    public Account updateAccount(Account updatedAccount) throws Exception {
        Account oldAccount = removeAccount(updatedAccount.getId());
        if (oldAccount == null) {
            Logger.error("Trying to update a non-existent account", updatedAccount);
            throw new Exception("Account not found " + updatedAccount.getId());
        }
        this.accountList.add(updatedAccount);
        this.flushLatestAccountData();
        return updatedAccount;
    }


    /**
     * Removes an account with the supplied id
     *
     * @param id the account id
     * @return the removed account with the supplied ID
     */
    public Account removeAccount(String id) throws Exception {
        Account toBeRemoved = null;
        for (Account acc : accountList) {
            if (acc.getId().equals(id)) {
                toBeRemoved = acc;
                break;
            }
        }
        if (toBeRemoved == null) {
            Logger.error("Trying to remove a non-existent account", id);
            throw new Exception("Account not found ");
        }
        this.accountList.remove(toBeRemoved);
        return toBeRemoved;
    }

    /**
     * Gets an account with the supplied id
     *
     * @param id the account id
     * @return the account POJO with the supplied ID
     */
    public Account getAccount(String id) {
        Account found = null;
        for (Account acc : accountList) {
            if (acc.getId().equals(id)) {
                found = acc;
                break;
            }
        }
        return found;
    }


    /**
     * Flushes the lates accounts to the data file.
     * It first removes all the records and then appends the latest records
     * from the accounts list.
     *
     * @throws Exception
     */
    private void flushLatestAccountData() throws Exception {
        try {
            if (dataFile.exists()) {
                dataFile.delete();
            }
            dataFile.createNewFile();

            BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile));
            Gson gson = new Gson();
            for (Account account : accountList) {
                String json = gson.toJson(account);
                bw.write(json);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            Logger.error("Unable to flush the latest accounts data to the file", e);
            throw e;
        }
    }
}
