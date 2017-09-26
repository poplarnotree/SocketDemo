package org.socket.entity;

import java.io.Serializable;

public class TbUser implements Serializable {
    private int id;
    private String username;
    private String password;

    public TbUser() {
        super();
    }

    public TbUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
