package common.messages;

import java.io.Serializable;

/**
 * Created by suparngupta on 4/19/14.
 */
public class MutationAck implements Serializable {
    private MutationReq mutationReq;
    private boolean status;

    public MutationAck(MutationReq mutationReq, boolean status) {
        this.mutationReq = mutationReq;
        this.status = status;
    }

    /**
     * Gets mutationReq.
     *
     * @return Value of mutationReq.
     */
    public MutationReq getMutationReq() {
        return mutationReq;
    }

    /**
     * Sets new status.
     *
     * @param status New value of status.
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Gets status.
     *
     * @return Value of status.
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Sets new mutationReq.
     *
     * @param mutationReq New value of mutationReq.
     */
    public void setMutationReq(MutationReq mutationReq) {
        this.mutationReq = mutationReq;
    }
}
