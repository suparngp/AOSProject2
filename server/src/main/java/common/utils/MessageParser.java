package common.utils;

import com.google.gson.Gson;
import common.messages.MessageType;
import common.messages.WrapperMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Parses the communication common.messages to required format POJO <-> Byte Stream
 * Created by suparngupta on 4/5/14.
 */
public class MessageParser {

    /**
     * deserializes the message stream to the object.
     *
     * @param buffer the byte stream buffer
     * @return the deserialized object
     */
    public static Object deserializeObject(byte[] buffer) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer));
            return ois.readObject();
        } catch (Exception e) {
            Logger.error("Unable to deserialize the message buffer", e);
            return null;
        }
    }

    /**
     * Serializes the object to a byte stream.
     *
     * @param o the object to be serialized
     * @return the serialized byte stream buffer.
     */
    public static byte[] serializeObject(Object o) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            return bos.toByteArray();
        } catch (Exception e) {
            Logger.error("Unable to serialize object to byte buffer", e);
            return null;
        }
    }

    /**
     * Creates a message wrapper for the given object and message type
     *
     * @param o    the object to be serialized
     * @param type the message type
     * @return the JSON string of the message wrapper.
     */
    public static String createWrapper(Object o, MessageType type) {
        WrapperMessage message = new WrapperMessage();
        message.setMessageType(type);
        message.setMessageBody(serializeObject(o));
        Gson gson = new Gson();
        return gson.toJson(message);
    }

    /**
     * Parses a string into WrapperMessage POJO.
     *
     * @param json the json string
     * @return the parsed JSON WrapperMessage Object
     */
    public static WrapperMessage parseMessageJSON(String json) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(json, WrapperMessage.class);
    }
}
