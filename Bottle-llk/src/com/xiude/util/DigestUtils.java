package com.xiude.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Title: XXXX (类或者接口名�?
 * Description: XXXX (�?��对此类或接口的名字进行描�?
 * Copyright: Copyright (c) 2012
 * Company:深圳彩讯科技有限公司
 *
 * @author duminghui
 * @version 1.0
 */
public class DigestUtils {
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String shaHex(String data) {
		return new String(encodeHex(sha(data)));
	}

	public static byte[] sha(String data) {
		return sha(data.getBytes());
	}

	public static byte[] sha(byte[] data) {
		return getShaDigest().digest(data);
	}

	private static MessageDigest getShaDigest() {
		return getDigest("SHA");
	}

	static MessageDigest getDigest(String algorithm) {
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

}
