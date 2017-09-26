package org.socket.entity;

import java.io.Serializable;

public class TbFile implements Serializable {
    private int fid;
    private String fname;
    private byte[] fcontent;

    public TbFile() {
    }

    public TbFile(String fname, byte[] fcontent) {
        this.fname = fname;
        this.fcontent = fcontent;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public byte[] getFcontent() {
        return fcontent;
    }

    public void setFcontent(byte[] fcontent) {
        this.fcontent = fcontent;
    }
}
