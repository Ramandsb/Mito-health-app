package in.tagbin.mitohealthapp;


import java.util.ArrayList;

public class CustomPojo {
    private String name;
    private String messages;
    private String time_mess;
    private String time,content;
    private String source;


    public CustomPojo() {

    }
    //getting content value
    public String getContent(){return content;}
    //setting content value
    public void setContent(String content){this.content=content;}

    public String getTime(){return time;}
    public void setTime(String time){this.time=time;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessages() {
        return messages;
    }

    public String getSource() {

        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime_mess() {
        return time_mess;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public void setTime_mess(String time_mess) {
        this.time_mess = time_mess;
    }
}
