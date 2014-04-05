package messages;

/**
 * Represents the message types of all the messages exchanged in the system.
 * Created by suparngupta on 4/5/14.
 */
public enum MessageType {
    /**
     * Inter-server communication messages
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
    DICOVERY_COMPLETE
}
