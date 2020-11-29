package com.track.trackandtrigger.Modal;

public class ItemsModel {
    public String topic = "";
    public String itemCount ="";
    public String category="";
    public String imageUrl="";

    public String getCategory() {
        return category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ItemsModel() {
    }

    public ItemsModel(String topic, String itemCount,String category,String imageUrl) {
        this.topic = topic;
        this.itemCount = itemCount;
        this.category = category;
        this.imageUrl = imageUrl;
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
