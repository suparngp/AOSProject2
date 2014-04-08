package common.messages;

import java.io.Serializable;

/**
 * Represents a message to create or update an account
 * Created by suparngupta on 4/7/14.
 */
public class AccountMessage implements Serializable{
    private int serverId;
    private int clientId;
    private Account account;

    public AccountMessage(){

    }
    public AccountMessage(int serverId, int clientId, Account account) {
        this.serverId = serverId;
        this.clientId = clientId;
        this.account = account;
    }

    /**
     * Sets new account.
     *
     * @param account New value of account.
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Gets clientId.
     *
     * @return Value of clientId.
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Sets new serverId.
     *
     * @param serverId New value of serverId.
     */
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    /**
     * Sets new clientId.
     *
     * @param clientId New value of clientId.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Gets serverId.
     *
     * @return Value of serverId.
     */
    public int getServerId() {
        return serverId;
    }

    /**
     * Gets account.
     *
     * @return Value of account.
     */
    public Account getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountMessage)) return false;

        AccountMessage that = (AccountMessage) o;

        if (clientId != that.clientId) return false;
        if (serverId != that.serverId) return false;
        if (account != null ? !account.equals(that.account) : that.account != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = serverId;
        result = 31 * result + clientId;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AccountMessage{" +
                "serverId=" + serverId +
                ", clientId=" + clientId +
                ", account=" + account +
                '}';
    }
}
