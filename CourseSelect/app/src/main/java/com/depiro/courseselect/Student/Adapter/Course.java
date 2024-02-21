package com.depiro.courseselect.Student.Adapter;

public class Course {
    int id;
    String cname;
    String tname;
    String creditHour;

    public Course(int id, String cname, String tname, String creditHour) {
        this.id = id;
        this.cname = cname;
        this.tname = tname;
        this.creditHour = creditHour;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getCreditHour() {
        return creditHour;
    }

    public void setCreditHour(String creditHour) {
        this.creditHour = creditHour;
    }
}
