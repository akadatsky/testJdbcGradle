package com.akadatsky.model;

public class User {

    private String id;
    private String name;
    private int age;
    private String groupId;

    public User(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(String id, String name, int age, String groupId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

}
