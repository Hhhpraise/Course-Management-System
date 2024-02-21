package com.depiro.courseselect.Teacher.Adapter;

public class Course {
    int id;
    String name;
    String creditHour;

    public Course(int id, String name, String creditHour) {
        this.id = id;
        this.name = name;
        this.creditHour = creditHour;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getCreditHour() {
        return creditHour;
    }

    public void setCreditHour(String creditHour) {
        this.creditHour = creditHour;
    }
}
