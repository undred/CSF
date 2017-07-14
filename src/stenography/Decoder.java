package stenography;

import exceptions.DecodingMessageException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * @author André Ribeiro
 */
public class Decoder 
{
    private static int MESSAGE_OFFSET = 32;
    private static int BITS_IN_A_BYTE = 8;
    
    public String decode(InputStream input) throws DecodingMessageException
    {
        byte[] inputBytes = null;
        byte[] messageBytes = null;
        
        String message = null;
        try
        {
            inputBytes = IOUtils.toByteArray(input);
            messageBytes = retrieveText(inputBytes);
            message = new String(messageBytes, "UTF-8");
        }
        catch(IOException  exc)
        {
            throw new DecodingMessageException(exc);
        }
        
        return message;
    }
    
    private int getMessageLength(byte[] inputBytes)
    {
        int length = 0;
        for(int i = 0; i < MESSAGE_OFFSET; ++i) 
        {
            length = (length << 1) | (inputBytes[i] & 1);
        }
        return length;
    }
    
    private byte[] retrieveText(byte[] inputBytes)
    {
        int offset = MESSAGE_OFFSET;
        byte[] messageBytes = new byte[getMessageLength(inputBytes)];
        
        for(int b = 0; b < messageBytes.length; ++b )
        {
            for(int i = 0; i < BITS_IN_A_BYTE; ++i, ++offset)
            {
                    messageBytes[b] = (byte)((messageBytes[b] << 1) | (inputBytes[offset] & 1));
            }
        }
        return messageBytes;
    }
}
