package com.example.testmybatis.Common;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {
    private static final String driver="oracle.jdbc,driver.OracleDriver";
    private static final String url="jdbc:oracle:thin:@10.1.51.226:1521:orcl";
    private  static final String username="CSB_CD";
    private static  final String password="kingthis";


    public static Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName(driver);
            connection= DriverManager.getConnection(url,username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    public static Boolean executeUpdate(String sql,Object...parem){
            Connection connection=null;
        PreparedStatement ps=null;


        try {
            connection=DBUtils.getConnection();
            ps=connection.prepareStatement(sql);
            for (int i = 0; i <parem.length ; i++) {
                ps.setObject(i+1,parem[i]);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(connection,ps,null);
        }
        return false;
    }
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        if (rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static List executeQuery(String sql,Class c,Object...parem){
        Connection connection=null;
        PreparedStatement ps=null;
        ResultSet rs=null;
        List list=null;



        try {
            connection=getConnection();
            ps=connection.prepareStatement(sql);

            for (int i = 0; i <parem.length ; i++) {
                ps.setObject(i+1,parem[i]);
            }
            rs=ps.executeQuery();
            list=new ArrayList();
            Field[] dfield=c.getDeclaredFields();
            Map<String,Class> map=new HashMap<>();
            for (Field field:dfield){

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
return list;
    }
}
