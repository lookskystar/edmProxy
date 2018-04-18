package com.edmProxy.test;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author Administrator
 * 
 */
public class Base64Test {
	/**
	 * 生成密钥
	 * 
	 * @param s
	 *            用于生成密钥的字符串，必须不少于8位
	 * @return 密钥
	 * @throws Exception
	 */
	private static SecretKey genKey(String s) throws Exception {
		SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
		DESKeySpec ks = new DESKeySpec(s.substring(0, 8).getBytes());
		SecretKey kd = kf.generateSecret(ks);
		return kd;
	}

	/**
	 * 加密
	 * 
	 * @param key
	 *            用于生成密钥的字符串，必须不少于8位
	 * @param src
	 *            要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String getEncryptedString(String key, String src) {
		String base64 = "";
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, genKey(key));
			byte[] inputBytes = src.getBytes("UTF8");
			byte[] outputBytes = cipher.doFinal(inputBytes);
			BASE64Encoder encoder = new BASE64Encoder();
			base64 = encoder.encode(outputBytes);
		} catch (Exception e) {
			base64 = e.getMessage();
		}
		return base64;
	}
	public static String entryPsw(String instr) {
		byte[] cipherText = null;
		sun.misc.BASE64Decoder base64d = new sun.misc.BASE64Decoder();
		sun.misc.BASE64Encoder base64e = new sun.misc.BASE64Encoder();
		String materialKey = "@Zer5QHeCe5r2zrDyk398$*0kjyWcuYt";
		try {
			byte[] mykey = base64d.decodeBuffer(materialKey);
			if (mykey == null || mykey.length != 24) {
				instr = null;
				System.out.println("错误的验证密匙!");
			} else {
				byte[] plainText = instr.getBytes();
				javax.crypto.spec.SecretKeySpec secrekeysp = new javax.crypto.spec.SecretKeySpec(mykey, "DESede");
				java.security.Key key = (java.security.Key) secrekeysp;

				javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DESede/ECB/PKCS5Padding", "SunJCE");

				cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
				cipherText = cipher.doFinal(plainText);
			}
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}
		return base64e.encode(cipherText);
	}
	/**
	 * 解密
	 * 
	 * @param key
	 *            用于生成密钥的字符串，必须不少于8位
	 * @param crypt
	 *            要解密的字符串
	 * @return 解密后的原文
	 */
	public static String getDecryptedString(String key, String crypt) {
		String result = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, genKey(key));
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] raw = decoder.decodeBuffer(crypt);
			byte[] stringBytes = cipher.doFinal(raw);
			result = new String(stringBytes, "UTF8");
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}

	// 加解密实例
	public static void main(String[] args) {
		String key = "@Zer5QHeCe5r2zrDyk398$*0kjyWcuYt";
		String src = "1234567";
		System.out.println("src==" + src);
		String encrypt = Base64Test.getEncryptedString(key, src);
		System.out.println("encrypt==" + encrypt);
		String testEncrypt = Base64Test.entryPsw(src);
		System.out.println("encrypt==" + encrypt);
		String decrypt = Base64Test.getDecryptedString(key, encrypt);
		System.out.println("decrypt==" + decrypt);
	}
} 