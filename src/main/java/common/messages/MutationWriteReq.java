package common.messages;

import java.io.Serializable;
import java.util.List;

/**
 * Created by suparngupta on 4/19/14.
 */
public class MutationWriteReq implements Serializable{
    private String requestId;
    private String objectId;
    private int clientId;
    private List<String> serialNumbers;

    public MutationWriteReq(String requestId, int clientId, List<String> serialNumbers, String objectId) {
        this.requestId = requestId;
        this.clientId = clientId;
        this.serialNumbers = serialNumbers;
        this.objectId = objectId;
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
     * Gets requestId.
     *
     * @return Value of requestId.
     */
    public String getRequestId() {
        return requestId;
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
     * Sets new requestId.
     *
     * @param requestId New value of requestId.
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Gets serialNumbers.
     *
     * @return Value of serialNumbers.
     */
    public List<String> getSerialNumbers() {
        return serialNumbers;
    }

    /**
     * Sets new serialNumbers.
     *
     * @param serialNumbers New value of serialNumbers.
     */
    public void setSerialNumbers(List<String> serialNumbers) {
        this.serialNumbers = serialNumbers;
    }

    /**
     * Sets new objectId.
     *
     * @param objectId New value of objectId.
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Gets objectId.
     *
     * @return Value of objectId.
     */
    public String getObjectId() {
        return objectId;
    }

    @Override
    public String toString() {
        return "MutationWriteReq{" +
                "requestId='" + requestId + '\'' +
                ", objectId='" + objectId + '\'' +
                ", clientId=" + clientId +
                ", serialNumbers=" + serialNumbers +
                '}';
    }
}
