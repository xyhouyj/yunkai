package com.hyj.zookeeper.queue;

import java.io.Serializable;

/**
 * Created by houyunjuan on 2018/6/21.
 */
public class User implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
