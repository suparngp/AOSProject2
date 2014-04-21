package common.messages;

import java.io.Serializable;

/**
 * Created by suparngupta on 4/20/14.
 */
public class HeartBeat implements Serializable{
    private int senderId;
    private int receiverId;

    public HeartBeat(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    /**
     * Gets senderId.
     *
     * @return Value of senderId.
     */
    public int getSenderId() {
        return senderId;
    }

    /**
     * Sets new receiverId.
     *
     * @param receiverId New value of receiverId.
     */
    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * Gets receiverId.
     *
     * @return Value of receiverId.
     */
    public int getReceiverId() {
        return receiverId;
    }

    /**
     * Sets new senderId.
     *
     * @param senderId New value of senderId.
     */
    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    @Override
    public String toString() {
        return "HeartBeat{" +
                "senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
