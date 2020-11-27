package com.track.trackandtrigger.Modal;

public class RemindersModel {
    public String title="";
    public String datetime="";
    public boolean isDone = false;
    public int reminder_id = 0;

    public RemindersModel() {
    }

    public RemindersModel(String title, String datetime, boolean isDone,int reminder_id) {
        this.title = title;
        this.datetime = datetime;
        this.isDone = isDone;
        this.reminder_id = reminder_id;
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

    public int getReminder_id() { return reminder_id; }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setReminder_id(int reminder_id) { this.reminder_id = reminder_id; }
}
