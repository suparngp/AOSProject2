package common.messages;

import java.io.Serializable;

/**
 * Created by suparngupta on 4/19/14.
 */
public class MutationReq implements Serializable {
    private int clientId;
    private int serverId;
    private String objectId;
    private String requestId;
    private MutationType requestType;
    private Account data;

    /**
     * Sets new data.
     *
     * @param data New value of data.
     */
    public void setData(Account data) {
        this.data = data;
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
     * Sets new requestId.
     *
     * @param requestId New value of requestId.
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
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
     * Gets requestType.
     *
     * @return Value of requestType.
     */
    public MutationType getRequestType() {
        return requestType;
    }

    /**
     * Gets data.
     *
     * @return Value of data.
     */
    public Account getData() {
        return data;
    }

    /**
     * Gets objectId.
     *
     * @return Value of objectId.
     */
    public String getObjectId() {
        return objectId;
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
     * Sets new objectId.
     *
     * @param objectId New value of objectId.
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * Sets new requestType.
     *
     * @param requestType New value of requestType.
     */
    public void setRequestType(MutationType requestType) {
        this.requestType = requestType;
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
     * Sets new serverId.
     *
     * @param serverId New value of serverId.
     */
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    @Override
    public String toString() {
        return "MutationReq{" +
                "clientId=" + clientId +
                ", serverId=" + serverId +
                ", objectId='" + objectId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", requestType=" + requestType +
                ", data=" + data +
                '}';
    }
}
