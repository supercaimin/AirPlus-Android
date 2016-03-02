package cn.homecaught.airplus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cn.homecaught.airplus.MyApplication;

public class SharedPreferenceManager {
	public static final String PREFERENCE_FILE = "FILE";

	public static final String SP_NAME = "negotiation";
	public static final String PM_DATA = "pmdata";
	public static final String USER_DATA = "userdata";

	private SharedPreferences sp;
	private Editor editor;

	public SharedPreferenceManager(Context context, String file) {
		sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
		editor = sp.edit();
	}


	public void setPMData(String data) {
		editor.putString(PM_DATA, data);
		editor.commit();
	}

	public String getPMData() {
		return getString(PM_DATA, "");
	}


	public void setUserData(String data) {
		editor.putString(USER_DATA, data);
		editor.commit();
	}

	public String getUserData() {
		return getString(USER_DATA, "");
	}

	private Boolean getBoolean(String tag, Boolean defaultValue) {
		if (sp == null) {
			sp = getSharedPreferences(MyApplication.instance);
		}

		return sp.getBoolean(tag, defaultValue);
	}
	public void putString(String tag,String value){
		editor.putString(tag, value);
		editor.commit();
	}
	public String getString(String tag, String defaultValue) {

//	private String getString(String tag, String defaultValue) {
		if (sp == null) {
			sp = getSharedPreferences(MyApplication.instance);
		}

		return sp.getString(tag, defaultValue);
	}
	public Editor getEdit() {
		return editor;
	}

	public static SharedPreferences getSharedPreferences(Context context) {
		return context.getSharedPreferences(SP_NAME, 0);
	}

}
