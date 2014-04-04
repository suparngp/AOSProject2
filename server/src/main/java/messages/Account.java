package messages;

import java.util.Date;

/**
 * Represents an account class. An account represents the Data Object in this system.
 * Created by suparngupta on 4/4/14.
 */
public class Account implements Comparable<Account> {

    private String id;
    private String ownerName;
    private Double openingBalance;
    private Double currentBalance;
    private Date createdAt;
    private Date updatedAt;

    public Account(String id) {
        this.id = id;
    }

    /**
     * Gets createdAt.
     *
     * @return Value of createdAt.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Gets openingBalance.
     *
     * @return Value of openingBalance.
     */
    public Double getOpeningBalance() {
        return openingBalance;
    }

    /**
     * Gets currentBalance.
     *
     * @return Value of currentBalance.
     */
    public Double getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Sets new createdAt.
     *
     * @param createdAt New value of createdAt.
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets updatedAt.
     *
     * @return Value of updatedAt.
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets new ownerName.
     *
     * @param ownerName New value of ownerName.
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * Sets new openingBalance.
     *
     * @param openingBalance New value of openingBalance.
     */
    public void setOpeningBalance(Double openingBalance) {
        this.openingBalance = openingBalance;
    }

    /**
     * Gets id.
     *
     * @return Value of id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets new updatedAt.
     *
     * @param updatedAt New value of updatedAt.
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Sets new id.
     *
     * @param id New value of id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets ownerName.
     *
     * @return Value of ownerName.
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * Sets new currentBalance.
     *
     * @param currentBalance New value of currentBalance.
     */
    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (!id.equals(account.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Account o) {
        return this.id.compareTo(o.id);
    }
}
