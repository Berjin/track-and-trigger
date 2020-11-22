package com.track.trackandtrigger.Modal;

public class RemindersModel {
    public String title="";
    public String datetime="";
    public boolean isDone = false;

    public RemindersModel() {
    }

    public RemindersModel(String title, String datetime, boolean isDone) {
        this.title = title;
        this.datetime = datetime;
        this.isDone = isDone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
