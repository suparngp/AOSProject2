package common.messages;

/**
 * A wrapper for the communication common.messages.
 * All the comm common.messages are converted to the byte stream and sent along with the message type.
 * Created by suparngupta on 4/5/14.
 */
public class WrapperMessage {
    private MessageType messageType;
    private byte[] messageBody;


    /**
     * Sets new messageType.
     *
     * @param messageType New value of messageType.
     */
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * Gets messageType.
     *
     * @return Value of messageType.
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Gets messageBody.
     *
     * @return Value of messageBody.
     */
    public byte[] getMessageBody() {
        return messageBody;
    }

    /**
     * Sets new messageBody.
     *
     * @param messageBody New value of messageBody.
     */
    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }
}
