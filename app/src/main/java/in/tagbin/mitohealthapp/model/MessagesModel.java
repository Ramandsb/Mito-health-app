package in.tagbin.mitohealthapp.model;


import java.util.ArrayList;

public class MessagesModel {
    private String name;
    private String messages;
    private String time;
    private String source;


    public MessagesModel() {

    }
    public MessagesModel(String name,String messages,String time,String source){
        this.name = name;
        this.messages = messages;
        this.time = time;
        this.source = source;
    }

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
    public void setMessages(String messages) {
        this.messages = messages;
    }
}
