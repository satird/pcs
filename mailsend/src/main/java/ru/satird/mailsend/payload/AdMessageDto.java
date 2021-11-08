package ru.satird.mailsend.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdMessageDto {

    private String title;
    private String text;
    private Float price;
    private boolean premium;
    private Object advertiser;
    private Object category;

    public AdMessageDto(@JsonProperty("title") String title,
                        @JsonProperty("text") String text,
                        @JsonProperty("price") Float price,
                        @JsonProperty("premium") boolean premium,
                        @JsonProperty("advertiser") Object advertiser,
                        @JsonProperty("category") Object category) {
        this.title = title;
        this.text = text;
        this.price = price;
        this.advertiser = advertiser;
        this.category = category;
        this.premium = premium;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Object getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(Object advertiser) {
        this.advertiser = advertiser;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }
}
