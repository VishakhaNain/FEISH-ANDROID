package com.app.feish.application.model;

/**
 * Created by lenovo on 10/27/2017.
 */

public class pojorating {
    private String name, rating, date,review,givenby;
    private int id;

    public pojorating() {
    }

    public pojorating(int id,String name, String rating, String date, String review, String givenby)
    {
        this.name = name;
        this.rating = rating;
        this.date = date;
        this.review=review;
        this.givenby=givenby;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getRating() {

        return rating;
    }

    public String getDate() {
        return date;
    }
    public String getReview() {
        return review;
    }
    public String getgivenby()
    {
        return  givenby;
    }

}
