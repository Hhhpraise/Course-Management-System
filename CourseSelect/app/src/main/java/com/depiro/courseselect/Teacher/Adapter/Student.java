package com.depiro.courseselect.Teacher.Adapter;

public class Student {
    String name;
    String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Student(String name, String number) {
        this.name = name;
        this.number = number;
    }
}
