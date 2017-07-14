package security;

import java.util.Base64;
import exceptions.DecriptingMessageExcepetion;
import exceptions.EncriptingMessageExcepetion;
import exceptions.ExceptionTemplate;
import exceptions.GeneratingKeyException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author André Ribeiro
 */
public class SecurityLibrary 
{
    
    private static final int NUMBER_OF_ITERATIONS = 65536;
    private static final int KEY_SIZE = 128;
    private static final int NUMBER_OF_BYTES = 16;
    private byte[] salt;
    
    public SecurityLibrary()
    {
        SecureRandom secRandom = new SecureRandom();
        salt = secRandom.generateSeed(NUMBER_OF_BYTES);
    }
    
    public SecurityLibrary(byte[] salt)
    {
        this.salt = salt;
    }
    
    public byte[] getSalt()
    {
        return salt;
    }
    
    //gerador de chaves
    public SecretKey generateKey(String password) throws GeneratingKeyException
    {
        SecretKey secret = null;
        try
        {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, NUMBER_OF_ITERATIONS, KEY_SIZE);
            SecretKey tmp = factory.generateSecret(spec);
            secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        }
        catch(InvalidKeySpecException | NoSuchAlgorithmException exc)
        {
            throw new GeneratingKeyException(exc);
        }
        return secret;
    }
    
    //metodo de cifrar
    public byte[] encriptMessage(String plainText, SecretKey key) throws EncriptingMessageExcepetion
    {
        byte[] cipherText = null;
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipher.ENCRYPT_MODE, key,  new IvParameterSpec(salt));
            cipherText = cipher.doFinal(plainText.getBytes("UTF-8"));
        }
        catch(InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException exc)
        {
            throw new EncriptingMessageExcepetion(exc);
        }
        return cipherText;
    }
    
    //metode de decifrar
    public String decriptMessage(byte[] cipherText, SecretKey key, byte[] IV) throws DecriptingMessageExcepetion
    {
        String plainText = null;
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipher.DECRYPT_MODE, key, new IvParameterSpec(salt));
            plainText = new String(cipher.doFinal(cipherText), "UTF-8");
        }
        catch(UnsupportedEncodingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException exc)
        {
            throw new DecriptingMessageExcepetion(exc);
        }
        return plainText;
    }
    
    //teste
    public static void main(String args[])
    {
        SecurityLibrary sec = new SecurityLibrary();
        try
        {
            SecretKey key = sec.generateKey("12345");
          
            byte[] salt = sec.getSalt();
            System.out.println("This is the Salt: " + new String(Base64.getEncoder().encode(salt)));
            
            byte[] cipherText = sec.encriptMessage("ola", key);
            System.out.println("This is the CipherText: " + new String(Base64.getEncoder().encode(cipherText)));
            
            String plainText = sec.decriptMessage(cipherText, key, salt);
            System.out.println("This is the PlainText: " + plainText);
        }
        catch(ExceptionTemplate exc)
        {
            exc.printMessage();
        }
    }
}
