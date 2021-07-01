package it.veronica.coursemanagement.utility;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesCrypt {
        private static final String ALGORITHM = "AES";
        private static final String KEY = "1Hbfh667adfDEJ78";

        public static String encrypt(String value)
        {
            try {
                Key key = generateKey();
                Cipher cipher = Cipher.getInstance(AesCrypt.ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
                String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
                return encryptedValue64;
            }
            catch (Exception e)
            {
                return "";
            }
        }

        public static String decrypt(String value)
        {
            try{
                Key key = generateKey();
                Cipher cipher = Cipher.getInstance(AesCrypt.ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
                byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
                String decryptedValue = new String(decryptedByteValue,"utf-8");
                return decryptedValue;
            }
            catch (Exception e)
            {
                return "";
            }
        }

        private static Key generateKey()
        {
            Key key = new SecretKeySpec(AesCrypt.KEY.getBytes(),AesCrypt.ALGORITHM);
            return key;
        }

}
