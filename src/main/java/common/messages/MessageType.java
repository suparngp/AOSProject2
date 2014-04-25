package common.messages;

import java.io.Serializable;

/**
 * Represents the message types of all the common.messages exchanged in the system.
 * Created by suparngupta on 4/5/14.
 */
public enum MessageType implements Serializable{
    /**
     * Inter-server communication common.messages
     * */

    /**
     * s1 introduces itself to s2.
     */
    SERVER_INTRO,

    /**
     * s2 replies to SERVER_INTRO_REPLY
     */
    SERVER_INTRO_REPLY,

    /**
     * server broadcasts this message to every one else that
     * it has successfully learnt about all the other servers.
     */
    DISCOVERY_COMPLETE,


    /**
     * Client-Server Message Types
     * */

    /***
     * New protocol
     */
    READ_OBJ_REQ,
    READ_OBJ_SUCCESS,
    READ_OBJ_FAILED,
    HEARTBEAT,
    HEARTBEAT_ECHO,
    WHO_IS_PRIMARY,
    PRIMARY_INFO,
    MUTATION_REQ,
    MUTATION_ACK,
    MUTATION_REQ_FAILED,
    MUTATION_WRITE_REQ,
    MUTATION_WRITE_ACK,
    MUTATION_WRITE_FAILED,
    MUTATION_PROCEED,
    MUTATION_PROCEED_ACK,
    MUTATION_PROCEED_FAILED

 }