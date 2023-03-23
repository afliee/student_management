package app.midleware;

import com.sun.management.GcInfo;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class DES {
    private KeyGenerator keyGenerator;
    private SecretKey secretKey;
    private Cipher cipher;
    private static DES instance = null;

    private DES() throws Exception {
        keyGenerator = KeyGenerator.getInstance("DES");
        secretKey = keyGenerator.generateKey();
        cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    }

    public static synchronized DES getInstance() throws Exception {
        if (instance == null) {
            instance = new DES();
        }
        return instance;
    }

//    public byte[] encrypt(String text) throws Exception {
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        byte[] textByte = text.getBytes();
//        byte[] textEncrypted = cipher.doFinal(textByte);
//        return textEncrypted;
//    }
//
//
//    public String decrypt(byte[] text) throws Exception {
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        System.out.println("text: "+ new String(text));
//        byte[] textDecrypted = cipher.doFinal(text);
//        return new String(textDecrypted);
//    }

    public String encrypt(String text) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] textByte = text.getBytes();
        byte[] textEncrypted = cipher.doFinal(textByte);
        return Base64.getEncoder().encodeToString(textEncrypted);
    }

    public String decrypt(String text) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] textDecrypted = cipher.doFinal(Base64.getDecoder().decode(text));
        return new String(textDecrypted);
    }
}

