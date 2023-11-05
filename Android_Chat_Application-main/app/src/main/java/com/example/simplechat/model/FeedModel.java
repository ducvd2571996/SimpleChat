package com.example.simplechat.model;


public class FeedModel {
    private String title;
    private String description;
    private String image;
    private String newId;
    private String date;
    private String author;
    private String short_description;
    private String extra_image;

    public FeedModel() {
    }

    public FeedModel(String title, String short_description, String description, String image, String newId, String date, String author, String extra_image) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.newId = newId;
        this.date = date;
        this.author = author;
        this.short_description = short_description;
        this.extra_image = extra_image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getExtra_image() {
        return extra_image;
    }

    public void setExtra_image(String extra_image) {
        this.extra_image = extra_image;
    }
}
