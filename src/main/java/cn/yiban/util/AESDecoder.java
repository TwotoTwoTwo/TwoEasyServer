package cn.yiban.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.lang.IllegalArgumentException;
import org.apache.commons.codec.binary.Base64;

/**
 * 实现站内应用中128位AES解密
 *
 * 在开放平台中提供的AppID与AppSecret中为32字节
 * 使用时取AppSecret值前 16位作为KEY
 * 取AppID值的前16位为为IV，使用 AES/CBC/NoPadding 方式解密
 */
public class AESDecoder
{
	final static String ALGORITHM = "AES";
	final static String TRANSFORM = "AES/CBC/NoPadding";
	final static int    KEYLENGTH = 16;

	/**
	 * 128位AES解密方法
	 *
	 * KEY 与 IV 使用前16位
	 *
	 * @param String 需要解密的字符串
	 * @param String KEY（对应AppSecret值）
	 * @param String IV （对应AppID值）
	 * @param String 解密后的字符串，正常情况下为JSON串
	 */
	public static String dec(String text, String key, String iv)
		throws Exception
	{
		SecretKeySpec   keyval;
		if (iv.length() == 16)
		{
			keyval = new SecretKeySpec(key.getBytes(), ALGORITHM);
		}
		else
		{
			keyval = new SecretKeySpec(key.substring(0, 16).getBytes(), ALGORITHM);
		}
		IvParameterSpec ivspec = new IvParameterSpec(iv.substring(0, 16).getBytes());
		Cipher          cipher = Cipher.getInstance(TRANSFORM);
		
		cipher.init(Cipher.DECRYPT_MODE, keyval, ivspec);
		byte[] buffer = hexToBin(text);
		byte[] decode = cipher.doFinal(buffer);
		return new String(decode);
	}

	/**
	 * 十六进制串转换为二进制串
	 *
	 * @param  String 十六进制字符串
	 * @return byte[] 二进制数据数组
	 */
	public static byte[] hexToBin(String text)
	{
		if(text.length() < 1)
		{
			return null;
		}
		int len = text.length() / 2;
		byte[] result = new byte[text.length()/2];
		for (int i = 0; i < len; i++)
		{
			result[i] = (byte)(0xff & Integer.parseInt(text.substring(i * 2, i * 2 + 2), 16));
		}
		return result;
	}

}
