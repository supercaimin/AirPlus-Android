package cn.homecaught.airplus.bean;

/**
 * Created by a1 on 16/2/28.
 */
import java.io.Serializable;

public class PM25Bean implements Serializable{

    private String readingDate;
    private String reading;
    private String displayStatus;
    public String getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(String readingDate) {
        this.readingDate = readingDate;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }
}
