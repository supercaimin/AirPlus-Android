package cn.homecaught.airplus.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IDCard {

	/**
	 * 楠?璇???????绗????韬?浠借??瑙????
	 * 
	 * @param content
	 * @return
	 */
	public static boolean checkIDCard(String content) {
		// java涓????String瀵硅薄???matches??规????ラ??璇?姝ｅ??琛ㄨ揪寮?
		return content.matches("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
	}

	/**
	 * 判断身份证的日期是否正确
	 * 
	 * @param content
	 * @return
	 */
	public static boolean checkIDCardDate(String content) {
		String strYear = content.substring(6, 10);// 年份
		String strMonth = content.substring(10, 12);// 月份
		String strDay = content.substring(12, 14);//
		StringBuffer sBufferDate = new StringBuffer(strYear).append("-").append(strMonth).append("-")
				.append(strDay);
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {

				return false;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String v = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
		Pattern p = Pattern.compile(v);
		Matcher m = p.matcher(sBufferDate);
		return m.matches();

	}

	private String _codeError;

	// wi =2(n-1)(mod 11)
	final int[] wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
	// verify digit
	final int[] vi = { 1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2 };
	private int[] ai = new int[18];
	private static String[] _areaCode = { "11", "12", "13", "14", "15", "21",
			"22", "23", "31", "32", "33", "34", "35", "36", "37", "41", "42",
			"43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62",
			"63", "64", "65", "71", "81", "82", "91" };
	private static HashMap<String, Integer> dateMap;
	private static HashMap<String, String> areaCodeMap;
	static {
		dateMap = new HashMap<String, Integer>();
		dateMap.put("01", 31);
		dateMap.put("02", null);
		dateMap.put("03", 31);
		dateMap.put("04", 30);
		dateMap.put("05", 31);
		dateMap.put("06", 30);
		dateMap.put("07", 31);
		dateMap.put("08", 31);
		dateMap.put("09", 30);
		dateMap.put("10", 31);
		dateMap.put("11", 30);
		dateMap.put("12", 31);
		areaCodeMap = new HashMap<String, String>();
		for (String code : _areaCode) {
			areaCodeMap.put(code, null);
		}
	}

	// 楠?璇?韬?浠借??浣????,15浣????18浣?韬?浠借??
	public boolean verifyLength(String code) {
		int length = code.length();
		if (length == 15 || length == 18) {
			return true;
		} else {
			_codeError = "???璇?锛?杈???ョ??韬?浠借????蜂?????15浣????18浣????";
			return false;
		}
	}

	// ??ゆ????板?虹??
	public boolean verifyAreaCode(String code) {
		String areaCode = code.substring(0, 2);
		// Element child= _areaCodeElement.getChild("_"+areaCode);
		if (areaCodeMap.containsKey(areaCode)) {
			return true;
		} else {
			_codeError = "???璇?锛?杈???ョ??韬?浠借????风????板?虹??(1-2浣?)[" + areaCode
					+ "]涓?绗????涓???借????垮?哄?????浠ｇ??瑙?瀹?(GB/T2260-1999)";
			return false;
		}
	}

	// ??ゆ?????浠藉????ユ??
	public boolean verifyBirthdayCode(String code) {
		// 楠?璇????浠?
		String month = code.substring(10, 12);
		boolean isEighteenCode = (18 == code.length());
		if (!dateMap.containsKey(month)) {
			_codeError = "???璇?锛?杈???ョ??韬?浠借?????"
					+ (isEighteenCode ? "(11-12浣?)" : "(9-10浣?)") + "涓?瀛????["
					+ month + "]???浠?,涓?绗????瑕?姹?(GB/T7408)";
			return false;
		}
		// 楠?璇???ユ??
		String dayCode = code.substring(12, 14);
		Integer day = dateMap.get(month);
		String yearCode = code.substring(6, 10);
		Integer year = Integer.valueOf(yearCode);

		// ???2????????????
		if (day != null) {
			if (Integer.valueOf(dayCode) > day || Integer.valueOf(dayCode) < 1) {
				_codeError = "???璇?锛?杈???ョ??韬?浠借?????"
						+ (isEighteenCode ? "(13-14浣?)" : "(11-13浣?)") + "["
						+ dayCode
						+ "]??蜂??绗????灏????1-30澶╁ぇ???1-31澶╃??瑙?瀹?(GB/T7408)";
				return false;
			}
		}
		// 2????????????
		else {
			// ??版???????????
			if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
				if (Integer.valueOf(dayCode) > 29
						|| Integer.valueOf(dayCode) < 1) {
					_codeError = "???璇?锛?杈???ョ??韬?浠借?????"
							+ (isEighteenCode ? "(13-14浣?)" : "(11-13浣?)")
							+ "[" + dayCode + "]??峰??" + year
							+ "??板勾????????典?????绗????1-29??风??瑙?瀹?(GB/T7408)";
					return false;
				}
			}
			// ?????版???????????
			else {
				if (Integer.valueOf(dayCode) > 28
						|| Integer.valueOf(dayCode) < 1) {
					_codeError = "???璇?锛?杈???ョ??韬?浠借?????"
							+ (isEighteenCode ? "(13-14浣?)" : "(11-13浣?)")
							+ "[" + dayCode + "]??峰??" + year
							+ "骞冲勾????????典?????绗????1-28??风??瑙?瀹?(GB/T7408)";
					return false;
				}
			}
		}
		return true;
	}

	// 楠?璇?韬?浠介?や????????浣???朵?????????????????瀛?姣?
	public boolean containsAllNumber(String code) {
		String str = "";
		if (code.length() == 15) {
			str = code.substring(0, 15);
		} else if (code.length() == 18) {
			str = code.substring(0, 17);
		}
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			if (!(ch[i] >= '0' && ch[i] <= '9')) {
				_codeError = "???璇?锛?杈???ョ??韬?浠借????风??" + (i + 1)
						+ "浣???????瀛?姣?";
				return false;
			}
		}
		return true;
	}

	public String getCodeError() {
		return _codeError;
	}

	// 楠?璇?韬?浠借??
	public boolean verify(String idcard) {
		_codeError = "";
		// 楠?璇?韬?浠借??浣????,15浣????18浣?韬?浠借??
		if (!verifyLength(idcard)) {
			return false;
		}
		// 楠?璇?韬?浠介?や????????浣???朵?????????????????瀛?姣?
		if (!containsAllNumber(idcard)) {
			return false;
		}

		// 濡???????15浣????灏辫浆???18浣????韬?浠借??
		String eifhteencard = "";
		if (idcard.length() == 15) {
			eifhteencard = uptoeighteen(idcard);
		} else {
			eifhteencard = idcard;
		}
		// 楠?璇?韬?浠借???????板?虹??
		if (!verifyAreaCode(eifhteencard)) {
			return false;
		}
		// ??ゆ?????浠藉????ユ??
		if (!verifyBirthdayCode(eifhteencard)) {
			return false;
		}
		// 楠?璇?18浣???￠?????,??￠???????????ISO 7064锛?1983锛?MOD 11-2 ??￠?????绯荤??
		if (!verifyMOD(eifhteencard)) {
			return false;
		}
		return true;
	}

	// 楠?璇?18浣???￠?????,??￠???????????ISO 7064锛?1983锛?MOD 11-2 ??￠?????绯荤??
	public boolean verifyMOD(String code) {
		String verify = code.substring(17, 18);
		if ("x".equals(verify)) {
			code = code.replaceAll("x", "X");
			verify = "X";
		}
		String verifyIndex = getVerify(code);
		if (verify.equals(verifyIndex)) {
			return true;
		}
		// int x=17;
		// if(code.length()==15){
		// x=14;
		// }
		_codeError = "???璇?锛?杈???ョ??韬?浠借????锋?????灏剧????板??楠?璇???????璇?";
		return false;
	}

	// ??峰????￠??浣?
	public String getVerify(String eightcardid) {
		int remaining = 0;

		if (eightcardid.length() == 18) {
			eightcardid = eightcardid.substring(0, 17);
		}

		if (eightcardid.length() == 17) {
			int sum = 0;
			for (int i = 0; i < 17; i++) {
				String k = eightcardid.substring(i, i + 1);
				ai[i] = Integer.parseInt(k);
			}

			for (int i = 0; i < 17; i++) {
				sum = sum + wi[i] * ai[i];
			}
			remaining = sum % 11;
		}

		return remaining == 2 ? "X" : String.valueOf(vi[remaining]);
	}

	// 15浣?杞?18浣?韬?浠借??
	public String uptoeighteen(String fifteencardid) {
		String eightcardid = fifteencardid.substring(0, 6);
		eightcardid = eightcardid + "19";
		eightcardid = eightcardid + fifteencardid.substring(6, 15);
		eightcardid = eightcardid + getVerify(eightcardid);
		return eightcardid;
	}
}

