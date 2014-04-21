package common.messages;

import java.io.Serializable;

/**
 * Created by suparngupta on 4/19/14.
 */
public class PrimaryInfo implements Serializable{
    private int primaryId;
    private String hostName;
    private int portNum;

    public PrimaryInfo(int primaryId, String hostName, int portNum) {
        this.primaryId = primaryId;
        this.hostName = hostName;
        this.portNum = portNum;
    }

    /**
     * Gets hostName.
     *
     * @return Value of hostName.
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Gets primaryId.
     *
     * @return Value of primaryId.
     */
    public int getPrimaryId() {
        return primaryId;
    }

    /**
     * Sets new primaryId.
     *
     * @param primaryId New value of primaryId.
     */
    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    /**
     * Gets portNum.
     *
     * @return Value of portNum.
     */
    public int getPortNum() {
        return portNum;
    }

    /**
     * Sets new portNum.
     *
     * @param portNum New value of portNum.
     */
    public void setPortNum(int portNum) {
        this.portNum = portNum;
    }

    /**
     * Sets new hostName.
     *
     * @param hostName New value of hostName.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString() {
        return "PrimaryInfo{" +
                "primaryId=" + primaryId +
                ", hostName='" + hostName + '\'' +
                ", portNum=" + portNum +
                '}';
    }
}
