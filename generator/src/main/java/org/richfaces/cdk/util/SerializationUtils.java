package org.richfaces.cdk.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * The utility methods for serializatio of {@link Serializable} objects.
 *
 * @author Lukas Fryc
 *
 */
public class SerializationUtils {

    public static byte[] serializeToBytes(Serializable object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserializeFromBytes(byte[] serializedObject) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(serializedObject);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public static String serializeToBase64(Serializable object) {
        byte[] serialized = serializeToBytes(object);
        return new String(Base64.encodeBase64(serialized));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserializeFromBase64(String serializedObject) {
        byte[] bytes = Base64.decodeBase64(serializedObject);
        return (T) deserializeFromBytes(bytes);
    }
}