package je.digital.kevin_pickmeup.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import je.digital.kevin_pickmeup.constants.Status;

public class Pickup {
    String id;
    Date timestamp;
    String location;
    int status;
    String driver;

    public Pickup() {
    }

    public Pickup(String location) {
        this.location = location;
        this.timestamp = new Date();
        this.status = Status.ACTIVE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
