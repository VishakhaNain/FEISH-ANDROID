
package com.app.feish.application.modelclassforapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mainhabitpojo {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("habit_name")
    @Expose
    private String habitName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHabitName() {
        return habitName;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

   /* @Override
    public int hashCode() {
        return habitName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Mainhabitpojo)) {
            return false;
        }
        Mainhabitpojo other = (Mainhabitpojo) o;
        return habitName.equalsIgnoreCase(other.getHabitName());
    }*/


}
