package in.tagbin.mitohealthapp.model;

/**
 * Created by Akshay-Pc on 8/7/2016.
 */
public class DataObject {
    public String title,location,etitle,timer,picture,description,event_time;
    public boolean all;
    public int id,capacity,total_request,total_approved;
    public EventType event_type;

    public DataObject(String title,String location,String etitle,int capsty,String tme) {
        this.title =title;
        this.location=location;
        this.etitle=etitle;
        this.capacity=capsty;
        this.timer=tme;
        this.all = false;

    }

    public String getDescription() {
        return description;
    }

    public String getEvent_time() {
        return event_time;
    }

    public String getTime() {
        return this.timer;
    }

    public void setTime(String time) {
        this.timer = time;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public boolean isAll(){
        return this.all;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getEtitle() {
        return this.etitle;
    }

    public void setEtitle(String etitle) {
        this.etitle = etitle;
    }


    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public int getTotal_request(){
        return this.total_request;
    }
    public int getTotal_approved(){
        return this.total_approved;
    }
    public String getPicture(){
        return this.picture;
    }
    public int getId(){
        return this.id;
    }
    public EventType getEvent_type(){
        return this.event_type;
    }

    public class EventType{
        public int id;
        public String title;
        public int getId(){
            return this.id;
        }
        public String getTitle(){
            return this.title;
        }
    }
}
