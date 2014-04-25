package server.services;

import com.google.gson.Gson;
import common.messages.Account;
import common.utils.Logger;

import java.io.*;
import java.util.*;

/**
 * Data Access API to perform CRUD operations on the services file.
 * Created by suparngupta on 4/4/14.
 */
public class DataAccess extends Thread {
    private File dataFile;

    private final HashSet<Account> accountList = new HashSet<>();

    /**
     * Constructor to initialize the services access API.
     * Loads all the records in the memory
     */
    public DataAccess(String fileName) {
        try {
            dataFile = new File(fileName);
            /**
             * Load all the records in the memory.
             * */
            if (!dataFile.exists())
                dataFile.createNewFile();

            BufferedReader br = new BufferedReader(new FileReader(dataFile));
            Gson gson = new Gson();

            String input = br.readLine();
            while (input != null) {
                Account acc = gson.fromJson(input, Account.class);
                accountList.add(acc);
                input = br.readLine();
            }
        } catch (Exception e) {
            Logger.error("Unable to initialize the Data Access Layer", e);
        }
    }

    /**
     * Creates a new account and writes it to the services file.
     *
     * @param account the new account object to be added
     * @return the new account as passed
     * @throws Exception if creation fails
     */
    public Account createAccount(Account account) throws Exception {
        if (getAccount(account.getId()) != null) {
            Logger.error("Account with a duplicate ID cannot be created", account);
            throw new Exception();
        }

//        Date date = new Date();
//        account.setCreatedAt(date);
//        account.setUpdatedAt(date);
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
        Date date = new Date();
        oldAccount.setUpdatedAt(date);
        oldAccount.setCurrentBalance(updatedAccount.getCurrentBalance());
        oldAccount.setOpeningBalance(updatedAccount.getOpeningBalance());
        oldAccount.setOwnerName(updatedAccount.getOwnerName());

        this.accountList.add(oldAccount);
        this.flushLatestAccountData();
        return oldAccount;
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
        this.flushLatestAccountData();
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
     * Flushes the latest accounts to the services file.
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

            List<Account> list = new ArrayList<>();
            list.addAll(accountList);
            Collections.sort(list);
            BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile));
            Gson gson = new Gson();
            for (Account account : list) {
                String json = gson.toJson(account);
                bw.write(json);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (Exception e) {
            Logger.error("Unable to flush the latest accounts services to the file", e);
            throw e;
        }
    }

    public HashSet<Account> getAllAccounts(){
        return this.accountList;
    }

    public void clearAllData() throws Exception{
        this.accountList.clear();
        flushLatestAccountData();
    }
}
