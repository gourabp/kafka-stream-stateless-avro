package com.magicalpipelines.model;

public class EntitySentiment {

   private long createdAt;
   private long  id;
   private String entity;
   private String text;
    private double sentimentScore;
    private double sentimentMagnitude;
    private double salience;

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public double getSentimentMagnitude() {
        return sentimentMagnitude;
    }

    public void setSentimentMagnitude(double sentimentMagnitude) {
        this.sentimentMagnitude = sentimentMagnitude;
    }



    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }



    public double getSalience() {
        return salience;
    }

    public void setSalience(double salience) {
        this.salience = salience;
    }


}
