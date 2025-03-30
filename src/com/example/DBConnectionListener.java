package com.example;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Set;
import java.util.HashSet;

@WebListener
public class DBConnectionListener implements ServletContextListener {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/javapa?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "root";
    
    // 用于跟踪所有创建的连接
    private static final Set<Connection> connections = new HashSet<>();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // 1. 加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 2. 建立连接
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            connections.add(conn);
            
            // 3. 存储连接
            sce.getServletContext().setAttribute("DB_CONNECTION", conn);
            
            System.out.println("数据库连接已建立并存储在ServletContext中");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("数据库连接初始化失败:");
            e.printStackTrace();
            throw new RuntimeException("数据库连接初始化失败", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 1. 关闭所有跟踪的连接
        synchronized (connections) {
            for (Connection conn : connections) {
                try {
                    if (!conn.isClosed()) {
                        conn.close();
                        System.out.println("数据库连接已关闭");
                    }
                } catch (SQLException e) {
                    System.err.println("关闭数据库连接时出错:");
                    e.printStackTrace();
                }
            }
            connections.clear();
        }
        
        // 2. 手动注销JDBC驱动
        deregisterDrivers();
        
        // 3. 停止MySQL的连接清理线程
        stopAbandonedConnectionCleanupThread();
    }
    
    private void deregisterDrivers() {
        java.util.Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            java.sql.Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                System.out.println("注销JDBC驱动: " + driver.getClass().getName());
            } catch (SQLException e) {
                System.err.println("注销JDBC驱动时出错 " + driver.getClass().getName() + ":");
                e.printStackTrace();
            }
        }
    }
    
    private void stopAbandonedConnectionCleanupThread() {
        try {
            Class<?> clazz = Class.forName("com.mysql.cj.jdbc.AbandonedConnectionCleanupThread");
            java.lang.reflect.Method method = clazz.getDeclaredMethod("checkedShutdown");
            method.invoke(null);
            System.out.println("MySQL废弃连接清理线程已停止");
        } catch (Throwable t) {
            // 忽略所有异常，因为这个方法可能在不同版本的驱动中不可用
            System.err.println("停止MySQL废弃连接清理线程失败:");
            t.printStackTrace();
        }
    }
}


//// DBConnectionListener.java
//package com.example;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//@WebListener
//public class DBConnectionListener implements ServletContextListener {
//    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/javapa?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
//    
//    private static final String JDBC_USER = "root";
//    private static final String JDBC_PASSWORD = "root";
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
//            sce.getServletContext().setAttribute("DBCONNECTION", conn);
//        } catch (ClassNotFoundException | SQLException e) {
//            throw new RuntimeException("数据库连接初始化失败", e);
//        }
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        Connection conn = (Connection) sce.getServletContext().getAttribute("DBConnection");
//        if (conn != null) {
//            try {
//                conn.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}