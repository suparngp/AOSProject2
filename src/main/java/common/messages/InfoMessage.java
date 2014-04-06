package common.messages;

/**
 * A general Info message
 * Created by suparngupta on 4/5/14.
 */
public class InfoMessage {
    private int senderId;
    private int receiverId;

    public InfoMessage(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    /**
     * Sets new senderId.
     *
     * @param senderId New value of senderId.
     */
    public void setSenderId(int senderId) {
        this.senderId = senderId;
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
     * Gets senderId.
     *
     * @return Value of senderId.
     */
    public int getSenderId() {
        return senderId;
    }
}
