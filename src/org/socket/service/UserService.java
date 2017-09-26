package org.socket.service;

import org.socket.entity.TbUser;
import org.socket.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    //用户登录
    public boolean login(TbUser user){
        String sql = "select * from tb_user where username = ? and password = ?";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareCall(sql);
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getPassword());
            rs = pstmt.executeQuery();
            if(rs.next()){
                return true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.closeAll(rs,pstmt,conn);
        }
        return false;
    }
    //用户注册
    public int register(TbUser user) {
        int count = 0;
        String sql = "insert into tb_user(username,password) values(?,?)";
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getPassword());
            count = pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            DBUtil.closeAll(rs, pstmt, conn);
            return count;
        }
    }
}
