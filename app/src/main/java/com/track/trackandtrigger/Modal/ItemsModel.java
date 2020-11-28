package com.track.trackandtrigger.Modal;

public class ItemsModel {
    public String topic = "";
    public String itemCount ="";
    public String category="";

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ItemsModel() {
    }

    public ItemsModel(String topic, String itemCount,String category) {
        this.topic = topic;
        this.itemCount = itemCount;
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getItemCount() {
        return itemCount;
    }

    public void setItemCount(String itemCount) {
        this.itemCount = itemCount;
    }
}
