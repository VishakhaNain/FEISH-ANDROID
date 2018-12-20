package com.app.feish.application.model;

import java.io.Serializable;

public class vitalsignlist implements Serializable {
   private   String sign, unit, maxo, mino, totalo, remark;
   private   String name, age, relation, disease, status, year,desc,advise,medtype,med_time;
   private  int id;

    public vitalsignlist(int id,String name, String age, String relation, String disease, String status, String year, String desc) {
        this.name = name;
        this.age = age;
        this.relation = relation;
        this.disease = disease;
        this.status = status;
        this.year = year;
        this.desc = desc;
        this.id=id;
    }
    public vitalsignlist(String name, String age, String relation, String disease, String status, String year, String desc,String advise,String medtype,String med_time) {
        this.name = name;
        this.age = age;
        this.relation = relation;
        this.disease = disease;
        this.status = status;
        this.year = year;
        this.desc = desc;
        this.advise=advise;
        this.medtype=medtype;
        this.med_time=med_time;
    }

    public String getMed_time() {
        return med_time;
    }

    public void setMed_time(String med_time) {
        this.med_time = med_time;
    }

    public String getMedtype() {
        return medtype;
    }

    public void setMedtype(String medtype) {
        this.medtype = medtype;
    }

    public String getAdvise() {
        return advise;
    }

    public void setAdvise(String advise) {
        this.advise = advise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public vitalsignlist(int id,String sign, String unit, String maxo, String mino, String totalo, String remark) {
        this.sign = sign;
        this.unit = unit;
        this.maxo = maxo;
        this.mino = mino;
        this.totalo = totalo;
        this.remark = remark;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMaxo() {
        return maxo;
    }

    public void setMaxo(String maxo) {
        this.maxo = maxo;
    }

    public String getMino() {
        return mino;
    }

    public void setMino(String mino) {
        this.mino = mino;
    }

    public String getTotalo() {
        return totalo;
    }

    public void setTotalo(String totalo) {
        this.totalo = totalo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
