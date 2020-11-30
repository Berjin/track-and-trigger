package com.track.trackandtrigger.Modal;

public class DiaryModel {
    private String title="";
    private String description="";
    public String datetime="";

    public DiaryModel(){
        //empty constructor needed
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public DiaryModel(String title, String description, String datetime){
        this.title=title;
        this.description=description;
        this.datetime=datetime;

    }

}
