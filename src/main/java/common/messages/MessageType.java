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

    /**
     * SEEK messages are sent from the client to the server
     * to seek various kinds of permissions
     * */
    SEEK_CREATE_PERMISSION,
    SEEK_UPDATE_PERMISSION,
    SEEK_READ_PERMISSION,
    SEEK_DELETE_PERMISSION,

    /**
     * GRANT messages are sent by the server to the client
     * when the permission is granted.
     * */
    GRANT_CREATE_PERMISSION,
    GRANT_UPDATE_PERMISSION,
    GRANT_READ_PERMISSION,
    GRANT_DELETE_PERMISSION,

    /**
     * FAILED message are sent by the server to the client
     * when the permission is denied.
     * */
    FAILED_CREATE_PERMISSION,
    FAILED_UPDATE_PERMISSION,
    FAILED_READ_PERMISSION,
    FAILED_DELETE_PERMISSION,

    /**
     * All REQ messages are sent by the client to the server
     * to read, write, update and delete objects
     *
     * All SUCCESS messages are sent by the server to the client
     * when the CRUD operations are successful
     *
     * All failed messages are sent by the server to the client
     * when CRUD operations fail.
     * */
    CREATE_OBJ_REQ,
    CREATE_OBJ_SUCCESS,
    CREATE_OBJ_FAILED,
    UPDATE_OBJ_REQ,
    UPDATE_OBJ_SUCCESS,
    UPDATE_OBJ_FAILED,
    READ_OBJ_REQ,
    READ_OBJ_SUCCESS,
    READ_OBJ_FAILED,
    DELETE_OBJ_REQ,
    DELETE_OBJ_SUCCESS,
    DELETE_OBJ_FAILED,

    /**
     * Prepared Messages
     * */
    CREATE_OBJECT_PREPARE,
    CREATE_OBJECT_PREPARE_ACK,
    CREATE_OBJECT_COMMIT,
    CREATE_OBJECT_ABORT,
 }