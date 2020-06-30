public interface Encryption {
	
	/**
	 * Encrypts the passed in string
	 * @param s The string to encrypt
	 * @return  The encrypted string
	 */
	String encrypt(String s);
	
	
	/**
	 * Decrypts the passed in string
	 * @param s The string to decrypt
	 * @return  The (plaintext) decrypted string
	 */
	String decrypt(String s);
}
