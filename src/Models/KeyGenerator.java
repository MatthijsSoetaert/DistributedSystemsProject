package Models;

import javax.crypto.*;
import java.security.*;
public class KeyGenerator {

    public static SecretKey generateSymmetricKey(String type, int length) throws NoSuchAlgorithmException {
        javax.crypto.KeyGenerator symKeyGenerator = javax.crypto.KeyGenerator.getInstance(type);
        symKeyGenerator.init(length);
        return symKeyGenerator.generateKey();
    }
}
