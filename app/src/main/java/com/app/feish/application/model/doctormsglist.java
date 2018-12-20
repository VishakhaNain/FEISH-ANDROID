package com.app.feish.application.model;

public class doctormsglist {
    String test_name;
    int id;

    public doctormsglist(int id, String test_name) {
        this.test_name = test_name;
        this.id = id;
    }

    public String getName() {
        return test_name;
    }


    public int getId() {
        return id;
    }
}
