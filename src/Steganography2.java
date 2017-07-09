/**falta imports o eclipse trata disso**/
public class Steganography2{
	//ainda em duvida em relação ao AudioInputStream
	public void encode(AudioInputStream audio, Byte[] key){
		
		//cifrar os dados utilizando o AES
		Cipher c = Cipher.getInstance("AES");
		SecretKeySpec k = new SecretKeySpec(key, "AES");
		c.init(Cipher.ENCRYPT_MODE, k);
		
		byte[] encryptedData = c.doFinal(audio); //alterar audio para Byte[]
		
		audio.delete();//apagar o audio utilizado ,verificar se é seguro
		
		/**steganography aqui**/
		//fazer primeiro BlindHide , se conseguir fazer battleSteg
	}
	
	public void decode(Byte[] encryptedData, Byte[] key){
		
		/**steganography aqui**/
		//fazer primeiro BlindHide , se conseguir fazer battleSteg
		
		//decifar os dados utilizando o AES
		Cipher c = Cipher.getInstance("AES");
		SecretKeySpec k = new SecretKeySpec(key, "AES");
		c.init(Cipher.DECRYPT_MODE, k);
		byte[] data = c.doFinal(encryptedData);
		

	}
	
}
}