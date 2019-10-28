package Models;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class MessageBuilder {
    public static byte[] buildSendMessage(String message, Session session) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        //Generate next index and tag
        int newIndex = IndexGenerator.generateIndex();
        String newTag  = Base64.getEncoder().encodeToString(HashGenerator.generateRandomHash());

        Message m = new Message(message,newTag,newIndex);
        session.setNextIndex(newIndex);
        session.setNextTag(newTag);

        //Get secretKey from String
        byte[] decodedKey = Base64.getDecoder().decode(session.getSymKey());
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        //encrypt with symmetric key
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,originalKey);

        //Message to byte[]
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] messageBytes = new byte[0];
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(m);
            out.flush();
            messageBytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        byte[] finalString = cipher.doFinal(messageBytes);
        System.out.println("final string length after sym key encryption: " + finalString.length);
        return finalString;
    }

    public static Message buildReceiveMessage(byte[] messageBytes){
        ByteArrayInputStream bis = new ByteArrayInputStream(messageBytes);
        Message m = new Message();
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
             m = (Message) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }

        return m;
    }


}
