package cn.homecaught.airplus.bean;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;

import cn.homecaught.airplus.MyApplication;

/**
 * Created by a1 on 16/2/26.
 */

import java.io.Serializable;

public class UserBean implements Serializable{
    private static UserBean instance;

    private final String KEY_UID = "id";
    private final String KEY_EMAIL = "email";
    private final String KEY_SCHOOLID = "school_id";

    private SharedPreferences pref;

    public String getUid() {
        uid = pref.getString(KEY_UID, "");
        return uid;
    }

    public void setUid(String uid) {
        if (!(this.uid == uid)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(KEY_UID, uid).commit();
        }
        this.uid = uid;
    }

    private String uid;
    private String email;

    public String getEmail() {
        email = pref.getString(KEY_EMAIL, "");
        return email;
    }

    public void setEmail(String email) {
        if (!(this.email == email)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(KEY_EMAIL, email).commit();
        }
        this.email = email;
    }

    public String getSchoolId() {
        schoolId = pref.getString(KEY_SCHOOLID, "");
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        if (!(this.schoolId == schoolId)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(KEY_SCHOOLID, schoolId).commit();
        }
        this.schoolId = schoolId;
    }

    private String schoolId;



    private UserBean() {
        pref = MyApplication.getContext().getSharedPreferences("userinfo",
                Context.MODE_PRIVATE);

        uid = pref.getString(KEY_UID, "");
        email = pref.getString(KEY_EMAIL, "");
        schoolId = pref.getString(KEY_SCHOOLID, "");
    }
    public void setLoginUserInfo(JSONObject userDict) {
        if (userDict != null) {
            try{
                setUid(userDict.getString(KEY_UID));
                setEmail(userDict.getString(KEY_EMAIL));
                setSchoolId(userDict.getString(KEY_SCHOOLID));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void clear() {

        uid = null;
        email = null;
        schoolId = null;
        pref.edit().clear().commit();
    }

    public static UserBean getInstance() {
        if (instance == null) {
            instance = new UserBean();
        }
        return instance;
    }
}
