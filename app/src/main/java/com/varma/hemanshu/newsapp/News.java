package com.varma.hemanshu.newsapp;

/**
 * Public class for News Data
 * Setters for the data
 * Has an constructor for News class
 */
public class News {

    private String mTitle;
    private String mAuthor;
    private String mUrl;
    private String mCategory;
    private String mTimestamp;

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getCategory() {
        return mCategory;
    }

    public String getTimestamp() {
        return mTimestamp;
    }


    /**
     * Constructor for News Class
     *
     * @param title     Sets the Headline of Current News
     * @param url       Sets the Url of Current News
     * @param category  Sets the Category of Current News
     * @param timestamp Sets the Time of Current News
     */
    public News(String title, String author, String url, String category, String timestamp) {
        this.mTitle = title;
        this.mAuthor = author;
        this.mUrl = url;
        this.mCategory = category;
        this.mTimestamp = timestamp;


    }

}
