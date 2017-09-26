package org.socket.service;

import org.socket.entity.TbFile;
import org.socket.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileService {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    //将文件保存到数据库中
    public int save(TbFile file){
        int count = 0;
        String sql = "insert into tb_file(fname,fcontent) values(?,?)";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareCall(sql);
            pstmt.setString(1,file.getFname());
            pstmt.setBytes(2,file.getFcontent());
            count = pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.closeAll(rs,pstmt,conn);
            return count;
        }
    }
}
