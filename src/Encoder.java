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
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;


import sun.misc.IOUtils;

/**falta imports o eclipse trata disso**/

public class Encoder{
	
	//ainda em duvida em relaÃ§Ã£o ao AudioInputStream
	public boolean encode(String msg, byte[] key,String path,String fileName) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		//cifrar os dados utilizando o AES
		
		/*Cipher c = Cipher.getInstance("AES");
		SecretKeySpec k = new SecretKeySpec(key, "AES");
		c.init(Cipher.ENCRYPT_MODE, k);
		
		byte[] encryptedData = c.doFinal(msg); //alterar audio para Byte[]
		*/
		//apagar o audio utilizado ,verificar se é seguro
		
		String			file_name 	= audio_path(path,fileName,"wav");
		AudioInputStream 	audio_orig	= getAudio(file_name);
		InputStream input = add_text(audio_orig,msg);
		
		//transformar InputStream para AudioInputStream
		AudioInputStream result = new AudioInputStream(input,audio_orig.getFormat(),audio_orig.getFrameLength());
		
		
		return(setAudio(result,new File(audio_path(path,"steg","wav"))));
		

		//fazer primeiro BlindHide , se conseguir fazer battleSteg
	}
	
	private String audio_path(String path, String name, String ext)
	{
		return path + "/" + name + "." + ext;
	}
	
	//metodo para gravar uma musica
	private boolean setAudio(AudioInputStream aud, File file)
	{
		try
		{
			file.delete(); //delete resources used by the File
			AudioSystem.write(aud,AudioFileFormat.Type.WAVE,new File("D:\\wavAppended.wav"));
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
	private AudioInputStream getAudio(String f)
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

	private InputStream add_text(InputStream aud, String text)
	{
		//convert all items to byte arrays: image, message, message length
		byte audio[] = IOUtils.toByteArray(aud); //n percebo o porquê do erro , o import devia ser este import apache.commons.io;
	
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
	
	private byte[] bit_conversion(int i)
	{
		byte byte3 = (byte)((i & 0xFF000000) >>> 24);
		byte byte2 = (byte)((i & 0x00FF0000) >>> 16); 
		byte byte1 = (byte)((i & 0x0000FF00) >>> 8 ); 
		byte byte0 = (byte)((i & 0x000000FF)       );
		return(new byte[]{byte3,byte2,byte1,byte0});
	}

	private byte[] encode_text(byte[] audio, byte[] addition, int offset)
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
}
