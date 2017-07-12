import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

import org.apache.commons.io.IOUtils;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import exceptions.ExceptionTemplate;
import security.SecurityLibrary;



/**falta imports o eclipse trata disso**/

public class Encoder{
	
	//ainda em duvida em relação ao AudioInputStream
	public static boolean encode(String msg,String path,String fileName) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException{
		//cifrar os dados utilizando o AES
		
		/*Cipher c = Cipher.getInstance("AES");
		SecretKeySpec k = new SecretKeySpec(key, "AES");
		c.init(Cipher.ENCRYPT_MODE, k);
		
		byte[] encryptedData = c.doFinal(msg); //alterar audio para Byte[]
		*/
		//apagar o audio utilizado ,verificar se � seguro
		
		String			file_name 	= audio_path(path,fileName,"wav");
		AudioInputStream 	audio_orig	= getAudio(file_name);
		InputStream input = add_text(audio_orig,msg);
		
		//transformar InputStream para AudioInputStream
		AudioInputStream result = new AudioInputStream(input,audio_orig.getFormat(),audio_orig.getFrameLength());
		
		
		return(setAudio(result,new File(audio_path(path,"steg","wav"))));
		

		//fazer primeiro BlindHide , se conseguir fazer battleSteg
	}
	//metodo de decifrar
	public String decode(String path, String name)
	{
		byte[] decode;
		byte[] aud;
		try
		{
			//user space is necessary for decrypting
			AudioInputStream audio  = getAudio(audio_path(path,name,"wav"));
			decode = decode_text(IOUtils.toByteArray(audio));
			return(new String(decode));
		}
        catch(Exception e)
        {
			JOptionPane.showMessageDialog(null, 
				"There is no hidden message in this image!","Error",
				JOptionPane.ERROR_MESSAGE);
			return "";
        }
    }
	
	//metodo para concatenar o caminho para o ficheiro
	private static String audio_path(String path, String name, String ext)
	{
		return path + "/" + name + "." + ext;
	}
	
	//metodo para gravar uma musica
	private static boolean setAudio(AudioInputStream aud, File file)
	{
		try
		{
			file.delete(); //delete resources used by the File
			AudioSystem.write(aud,AudioFileFormat.Type.WAVE,file);
			return true;
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
				"File could not be saved!","Error",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	//metodo para ir buscar musica
	private static AudioInputStream getAudio(String f)
	{
		File file=new File(f);
		try {
			AudioInputStream clip = AudioSystem.getAudioInputStream(file);
			return clip;
		} catch (UnsupportedAudioFileException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	private static InputStream add_text(InputStream aud, String text) throws IOException
	{
		//convert all items to byte arrays: image, message, message length
                
                //n percebo o porqu� do erro , o import devia ser este import apache.commons.io?;
                
		byte audio[] = IOUtils.toByteArray(aud);
                
	
		byte msg[] = text.getBytes();
		byte len[] = bit_conversion(msg.length);
		try
		{
			encode_text(audio, len,  0); //0 first positiong
			encode_text(audio, msg, 32); //4 bytes of space for length: 4bytes*8bit = 32 bits
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		return aud;
	}
	
	private byte[] decode_text(byte[] audio)
	{
		int length = 0;
		int offset  = 32;
		for(int i=0; i<32; ++i) 
		{
			length = (length << 1) | (audio[i] & 1);
		}
		
		byte[] result = new byte[length];
		
		for(int b=0; b<result.length; ++b )
		{
			for(int i=0; i<8; ++i, ++offset)
			{
				result[b] = (byte)((result[b] << 1) | (audio[offset] & 1));
			}
		}
		return result;
	}
	
	private static byte[] bit_conversion(int i)
	{
		byte byte3 = (byte)((i & 0xFF000000) >>> 24);
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16); 
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); 
		byte byte0 = (byte)((i & 0x000000FF)       );
		return(new byte[]{byte3,byte2,byte1,byte0});
	}
	
	//metodo de cifrar
	private static byte[] encode_text(byte[] audio, byte[] addition, int offset)
	{
		//verificar se os dados + offset cabem no ficheiro
		if(addition.length + offset > audio.length)
		{
			throw new IllegalArgumentException("File not long enough!");
		}
		
		for(int i=0; i<addition.length; ++i)
		{
			int add = addition[i];
			for(int bit=7; bit>=0; --bit, ++offset) 
			{
				int b = (add >>> bit) & 1;
				audio[offset] = (byte)((audio[offset] & 0xFE) | b );
			}
		}
		return audio;
	}

	 //teste
    public static void main(String args[])
    {
    	String path="D:\\";
    	try {
			if(encode("ola",path,"wav1"))
				System.out.println("This was a sucess");
			else
				System.out.println("This was not a sucess");
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
