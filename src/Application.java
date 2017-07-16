
import exceptions.DecodingMessageException;
import exceptions.EncodingMessageException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
    public static void main(String[] args) throws EncodingMessageException, DecodingMessageException 
    {
        String path = "C:\\Users\\Undred\\Documents\\GitHub\\CSF\\";
        String originalName = "cartoon001";
        String steggedName = "cartoon001Stegged";
         
        File file = getFile(buildPath(path, originalName));
        
        Encoder encoder = new Encoder();
        
        AudioInputStream originalStream = getAudio(file);
       
        InputStream stteggedInput = encoder.encode(originalStream, "ola");
        setAudio(inputStreamToAudioStream(stteggedInput, originalStream), getFile(buildPath(path, steggedName)));
        
        Decoder decoder = new Decoder();
        
        file = getFile(buildPath(path, steggedName));
        AudioInputStream stteggedStream = getAudio(file);
        String hiddedMessage = decoder.decode(stteggedStream);
        System.out.println(hiddedMessage);
       
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
