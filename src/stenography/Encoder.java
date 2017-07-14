package stenography;

import exceptions.EncodingMessageException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * @author André Ribeiro
 */
public class Encoder 
{
    private static final int INITIAL_OFFSET = 0;
    
    public InputStream encode(InputStream input, String message) throws EncodingMessageException
    {
        byte[] inputBytes = null;
        byte[] steggedInputBytes = null;
        byte[] messageBytes = message.getBytes();
        byte[] messageSize = intToByteArray(messageBytes.length);
        
        InputStream steggedInput = null;
                
        try
        {
            inputBytes = IOUtils.toByteArray(input);
            
            verifyLength(inputBytes, messageBytes, INITIAL_OFFSET);
            steggedInputBytes = hideMessage(inputBytes, messageSize, INITIAL_OFFSET);
            steggedInputBytes = hideMessage(inputBytes, messageBytes, INITIAL_OFFSET + 32);
            
            steggedInput = new ByteArrayInputStream(steggedInputBytes);
        }
        catch(IOException | IllegalArgumentException exc)
        {
            throw new EncodingMessageException(exc);
        }
        return  steggedInput;
    }
    
    private void verifyLength(byte[] inputBytes, byte[] messageBytes, int offset)
    {
        if(messageBytes.length + offset > inputBytes.length)
        {
                throw new IllegalArgumentException("File not long enough!");
        }
    }
    
    private  byte[] hideMessage(byte[] inputBytes, byte[] messageBytes, int offset)
    {
        for(int i=0; i<messageBytes.length; ++i)
        {
                int add = messageBytes[i];
                for(int bit=7; bit>=0; --bit, ++offset) 
                {
                        int b = (add >>> bit) & 1;
                        inputBytes[offset] = (byte)((inputBytes[offset] & 0xFE) | b );
                }
        }
        return inputBytes;
    }
    
    private  byte[] intToByteArray(int i)
    {
        byte byte3 = (byte)((i & 0xFF000000) >>> 24);
        byte byte2 = (byte)((i & 0x00FF0000) >>> 16); 
        byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); 
        byte byte0 = (byte)((i & 0x000000FF)       );
        return(new byte[]{byte3,byte2,byte1,byte0});
    }
   
}
