package cn.homecaught.airplus.util.base64;

/**
 * Base64加密解密
 * @author 吴海军
 * 2014-11-14
 */
public class Base64Encrypt {
	/**
	 * 对字符串进行Base64加密
	 * @param s
	 * @return
	 */
	public static String getBASE64(String s) {
		if (s == null)
			return null;
		return Base64Coder.encodeString(s);
	}

	/**
	 * Base64加密
	 * @param s byte数组
	 * @return
	 */
	public static String getBASE64_byte(byte[] s) {
		if (s == null)
			return null;
		return new String(Base64Coder.encode(s));
	}
	
	/**
	 * Base64解密
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public static byte[] getByteArrFromBase64(String s) throws Exception {
		if (s == null)
			return null;
		return Base64Coder.decode(s);
	}
}
