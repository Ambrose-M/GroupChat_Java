import java.util.Random;

/**
 * Encrypts and decrypts strings.
 */
public class CaesarCipher implements Encryption {

	/**
	 * Constructor. Sets shift variable.
	 */
	public CaesarCipher() {
		shift = getShift();
	}

	/**
	 * Encrypts a string.
	 * 
	 * @param s The string to be encrypted
	 */
	@Override
	public String encrypt(String s) {
		return encryptDecrypt(s, true);
	}

	/**
	 * Decrypts a string.
	 * 
	 * @param s The string to be decrypted
	 */
	@Override
	public String decrypt(String s) {
		return encryptDecrypt(s, false);
	}

	/**
	 * Creates shift for encryption and decryption.
	 * 
	 * @return the shift for encryption and decryption
	 */
	private static int getShift() {
		Random r = new Random();
		int low = 1;
		int high = OFFSET_MAX - OFFSET_MIN;
		return r.nextInt(high - low) + low;
	}

	/**
	 * Creates an encrypted or decrypted string.
	 * 
	 * @param s       String being encrypted or decrypted
	 * @param encrypt True if encrypting, false if decrypting
	 * @return String after encryption or decryption
	 * @throws IllegalArgumentException
	 */
	private String encryptDecrypt(String s, boolean encrypt)  
								  throws IllegalArgumentException {
		StringBuilder sb = new StringBuilder();
		for (char c : s.toCharArray()) {
			int indx = c, cpos;
			if (!isPositionInRange(indx))
				throw new IllegalArgumentException("String to be encrypted " + 
												   "has unrecognized character " 
												   + c);

			if (encrypt) {
				cpos = indx + shift;
				if (cpos > OFFSET_MAX)
					cpos = OFFSET_MIN + (cpos - OFFSET_MAX);
			} else {
				cpos = indx - shift;
				if (cpos < OFFSET_MIN)
					cpos = OFFSET_MAX - (OFFSET_MIN - cpos);
			}
			sb.append((char) cpos);
		}
		return sb.toString();
	}

	/**
	 * Checks if a character in a string is within the min/max range.
	 * 
	 * @param indx The index of the character in the string.
	 * @return True if position is in range, false if not
	 */
	private boolean isPositionInRange(int indx) {
		return indx >= OFFSET_MIN && indx <= OFFSET_MAX;
	}

	private int shift;
	private static final int OFFSET_MIN = 32;
	private static final int OFFSET_MAX = 126;
}
