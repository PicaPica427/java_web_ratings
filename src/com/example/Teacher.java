package com.example;

public class Teacher {
    private int id;
    private String name;

    // 构造函数
    public Teacher(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters 和 Setters
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
}
