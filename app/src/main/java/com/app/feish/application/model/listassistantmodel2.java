package com.app.feish.application.model;

public class listassistantmodel2 {
    int id,  user_id,  service_id,  doector_id,  is_deleted;

    public listassistantmodel2(int id, int user_id, int service_id, int doector_id, int is_deleted) {
        this.id = id;
        this.user_id = user_id;
        this.service_id = service_id;
        this.doector_id = doector_id;
        this.is_deleted = is_deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public int getDoector_id() {
        return doector_id;
    }

    public void setDoector_id(int doector_id) {
        this.doector_id = doector_id;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }
}
