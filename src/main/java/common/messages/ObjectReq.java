package common.messages;

import java.io.Serializable;

/**
 * Represents a request to operate on an object
 * Created by suparngupta on 4/6/14.
 */
public class ObjectReq  implements Serializable{
    private String objectId;
    private int serverId;
    private int clientId;

    public ObjectReq(String objectId, int serverId, int clientId) {
        this.objectId = objectId;
        this.serverId = serverId;
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
     * Sets new objectId.
     *
     * @param objectId New value of objectId.
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
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
     * Gets clientId.
     *
     * @return Value of clientId.
     */
    public int getClientId() {
        return clientId;
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
     * Sets new clientId.
     *
     * @param clientId New value of clientId.
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObjectReq)) return false;

        ObjectReq objectReq = (ObjectReq) o;

        if (clientId != objectReq.clientId) return false;
        if (serverId != objectReq.serverId) return false;
        if (objectId != null ? !objectId.equals(objectReq.objectId) : objectReq.objectId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = objectId != null ? objectId.hashCode() : 0;
        result = 31 * result + serverId;
        result = 31 * result + clientId;
        return result;
    }
}
