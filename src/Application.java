
import exceptions.DecodingMessageException;
import exceptions.DecriptingMessageExcepetion;
import exceptions.EncodingMessageException;
import exceptions.EncriptingMessageExcepetion;
import exceptions.GeneratingKeyException;
import security.SecurityLibrary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import stenography.Encoder;
import stenography.Decoder;
/**
 * @author André Ribeiro
 */
public class Application 
{
    private static String WAV_EXTENSION = ".wav";
    /*
    Podes se quiser meter isto pela a consola com um scanner e assim não tens hardcoded
    */
    public static void main(String[] args) throws EncodingMessageException, DecodingMessageException, GeneratingKeyException, EncriptingMessageExcepetion, DecriptingMessageExcepetion 
    {
        String path = "C:\\Users\\ribeiroad\\Documents\\GitHub\\CSF\\";
        String originalName = "cartoon001";
        String steggedName = "cartoon001Stegged";
         
        File file = getFile(buildPath(path, originalName));
        
        //inicializar cifra
        SecurityLibrary sec = new SecurityLibrary();
        SecretKey key = sec.generateKey("12345");//valor aleatorio
        byte[] salt = sec.getSalt();
        System.out.println("This is the Salt: " + new String(Base64.getEncoder().encode(salt)));
        
        //cifrar mensagem
        byte[] cipherText = sec.encriptMessage("ola", key);
        System.out.println("This is the CipherText: " + new String(Base64.getEncoder().encode(cipherText)));
        
        //esconder mensagem
        Encoder encoder = new Encoder();
        
        AudioInputStream originalStream = getAudio(file);
       
        InputStream stteggedInput = encoder.encode(originalStream, new String(Base64.getEncoder().encode(cipherText)));
        setAudio(inputStreamToAudioStream(stteggedInput, originalStream), getFile(buildPath(path, steggedName)));
        
        //descobrir a mensagem
        Decoder decoder = new Decoder();
        
        file = getFile(buildPath(path, steggedName));
        AudioInputStream stteggedStream = getAudio(file);
        byte[] hiddenMessage = decoder.decode(stteggedStream);
        
        System.out.println("This is the CipherText: " + new String(Base64.getEncoder().encode(hiddenMessage)));
        String plainText = sec.decriptMessage(hiddenMessage, key, salt);
        
        System.out.println(plainText);
       
    }
    
    private static AudioInputStream inputStreamToAudioStream(InputStream steggedInput, AudioInputStream originalStream)
    {
        return new AudioInputStream(steggedInput, originalStream.getFormat(), originalStream.getFrameLength());
    }
    
    private static String buildPath(String path, String fileName)
    {
        return path + fileName + WAV_EXTENSION;
    }
    
    private static File getFile(String path)
    {
        return new File(path);
    }
    
    private static AudioInputStream getAudio(File file)
    {
        AudioInputStream audioStream = null;
        try
        {
            audioStream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException | IOException exc) 
        {
  
        }
        return audioStream;
    }
    
    private static boolean setAudio(AudioInputStream audioStream, File file)
    {
        boolean result = false;
        try 
        {    
            AudioSystem.write(audioStream ,AudioFileFormat.Type.WAVE,file);
            result = true;
        } 
        catch (IOException exc) 
        {
         
        }
        
        return result;
    }
}
