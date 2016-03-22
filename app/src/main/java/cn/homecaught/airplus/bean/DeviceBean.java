package cn.homecaught.airplus.bean;

import java.util.List;

/**
 * Created by a1 on 16/2/27.
 */

import java.io.Serializable;

public class DeviceBean implements Serializable{

    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }



    private String name;
    private String schoolId;
    private String schoolName;
    private String serial;
    private Boolean isPublic;

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    private String cityKey;

    public List<PM25Bean> getPm25Beans() {
        return pm25Beans;
    }

    public void setPm25Beans(List<PM25Bean> pm25Beans) {
        this.pm25Beans = pm25Beans;
    }

    private List<PM25Bean> pm25Beans;

}
