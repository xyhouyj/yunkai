package com.hyj.zookeeper.queue;

import java.io.Serializable;

/**
 * Created by houyunjuan on 2018/6/21.
 */
public class User implements Serializable {
    String name;
    String id;

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
}
