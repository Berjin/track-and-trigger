package com.track.trackandtrigger.Modal;

public class ItemsModel {
    public String topic = "";
    public String itemCount ="";

    public ItemsModel() {
    }

    public ItemsModel(String topic, String itemCount) {
        this.topic = topic;
        this.itemCount = itemCount;
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
