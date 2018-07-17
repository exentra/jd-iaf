package com.deere.tools.iaf;

import java.util.StringTokenizer;

/**
 * Open Source implementation of RC4 encryption / decryption.
 */
public class Rc4Key {
	private static final int SIZE = 256;
	private static final String ENCODE_STRING = "@RC4Encoded@";
	private final int[] state = new int[SIZE];
	private int[] initialState = null;
	private int x;
	private int y;
	private int[] key;

	public Rc4Key(final String key) {
		// build an array of ints that corresponds to the key
		this.setKey(stringToIntArray(key));
	}

	private String commaDeliminatedStringToCharacterString(final String input) {
		final StringBuffer returnVal = new StringBuffer("");

		if (input != null) {
			final StringTokenizer tempTokenizer = new StringTokenizer(input, ",");
			while (tempTokenizer.hasMoreTokens()) {
				try {
					// A NumberFormatException is thrown here if the String is not encoded correctly
					returnVal.append((char) Integer.valueOf(tempTokenizer.nextToken()).intValue());
				} catch (final NumberFormatException e) {
					final RuntimeException cre = new RuntimeException(
							"The supplied string is not a valid encryption string.  Unalbe to decrypt", e);
					throw cre;
				}
			}
		}
		return returnVal.toString();
	}

	private int[] getKey() {
		return this.key;
	}

	private String intArrayToCharacterString(final int in[]) {
		final StringBuffer ret = new StringBuffer("");
		final int tmpLen = in.length;

		for (int i = 0; i < tmpLen; i++) {
			ret.append((char) in[i]);
		}

		return ret.toString();
	}

	private String intArrayToCommaDeliminatedString(final int in[]) {
		final StringBuffer ret = new StringBuffer(ENCODE_STRING);
		final int tmpLen = in.length;

		for (int i = 0; i < tmpLen; i++) {
			if (i != 0) {
				ret.append(",");
			}
			ret.append(in[i]);
		}

		return ret.toString();
	}

	/**
	 * Insert the method's description here. Creation date: (7/26/00 4:47:01 PM)
	 *
	 * @return boolean
	 * @param inputString
	 *            java.lang.String
	 */
	public static boolean IsRecognizedEncryption(final String inputString) {
		return inputString != null && inputString.startsWith(ENCODE_STRING);
	}

	public static void main(final String[] args) {
		try {
			if (args.length < 2) {
				throw new IllegalArgumentException("Syntax is: \"java Rc4Key [key] [message]\"");
			}

			final String key = args[0];
			final String message = args[1];

			if (key.length() == 0 || message.length() == 0) {
				throw new IllegalArgumentException("Syntax is: \"java Rc4Key [key] [message]\"");
			}

			final Rc4Key tempKey = new Rc4Key(key);

			System.out.println(tempKey.rc4(message));

		} catch (final Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	private void prepareKey() {
		int tmpIndex1 = 0, tmpIndex2 = 0, tmpSwap;

		final int tmpKeyData[] = getKey();
		final int tmpKeyDataLen = getKey().length;

		// init state values
		for (int i = 0; i < SIZE; i++) {
			initialState[i] = i;
		}

		// calculate state values
		for (int counter = 0; counter < SIZE; counter++) {
			tmpIndex2 = (tmpKeyData[tmpIndex1] + initialState[counter] + tmpIndex2) % SIZE;
			tmpSwap = initialState[counter];
			initialState[counter] = initialState[tmpIndex2];
			initialState[tmpIndex2] = tmpSwap;
			tmpIndex1 = (tmpIndex1 + 1) % tmpKeyDataLen;
		}
	}

	public String rc4(String message) {
		boolean returnString = false;

		if (message != null) {
			if (message.startsWith(ENCODE_STRING)) {
				// message starts with our ENCODE_STRING, so it must be encoded
				message = commaDeliminatedStringToCharacterString(message.substring(ENCODE_STRING.length()));
				returnString = true;
			}

			this.restoreInitialState();
			int tmpX = x, tmpY = y, tmpSwap, tmpXorIndex;

			// build a buffer that is an array of ints that corresponds to the message
			final int tmpBuffer[] = stringToIntArray(message);
			final int tmpBufferLen = message.length();

			// do the encoding
			for (int counter = 0; counter < tmpBufferLen; counter++) {
				tmpX = (tmpX + 1) % SIZE;
				tmpY = (state[tmpX] + tmpY) % SIZE;
				tmpSwap = state[tmpX];
				state[tmpX] = state[tmpY];
				state[tmpY] = tmpSwap;
				tmpXorIndex = (state[tmpX] + state[tmpY]) % SIZE;
				tmpBuffer[counter] ^= state[tmpXorIndex];
			}

			x = tmpX;
			y = tmpY;

			// build return val

			if (!returnString) {
				return intArrayToCommaDeliminatedString(tmpBuffer);
			} else {
				return intArrayToCharacterString(tmpBuffer);
			}
		} else {
			return "";
		}
	}

	private void restoreInitialState() {
		if (initialState == null) {
			initialState = new int[SIZE];
			prepareKey();
		}
		for (int i = 0; i < SIZE; i++) {
			state[i] = initialState[i];
		}

		x = 0;
		y = 0;
	}

	private void setKey(final int[] key) {
		this.key = key;
	}

	private int[] stringToIntArray(final String in) {
		final int tmpLen = in.length();
		final int ret[] = new int[tmpLen];
		for (int i = 0; i < tmpLen; i++) {
			ret[i] = in.charAt(i);
		}
		return ret;
	}
}
