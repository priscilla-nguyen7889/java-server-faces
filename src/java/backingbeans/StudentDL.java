/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backingbeans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Stateless;

/**
 *
 * @author prisc
 */
@Stateless
public class StudentDL {
    
   
    private static Connection conn;
    public StudentDL (){
        conn = GetConnection();
    }
   
     public static Connection GetConnection() {
        Connection conn =null;  
        String username = "root";
        String password = "";
        String dbUrl = "jdbc:mysql://localhost:3306/nbcc";
        
        try {
            
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbUrl, username, password);
            System.out.println("Connection made.");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Error connecting to the MySQL Driver ClassNotFoundException! " + ex.getMessage());
        }
        catch (SQLException ex) {
            System.out.println("Error connecting to the database SQLException! " + ex.getMessage());
        }  
        return conn;
    }

    
    @PreDestroy
    public static void CloseConnection() {
        try {
            System.out.println("Closing connection...");
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Error closing the connection! " + ex.getMessage());
        }
    }
    
    public static boolean InsertStudent(Student s){
        String sqlStatement = "INSERT INTO students (id, name, program, courses) VALUES (?, ?, ?, ?)";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(sqlStatement);
            ps.setInt(1, s.getId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getProgram());
            ps.setString(4, s.getCourses());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error executing query! " + ex.getMessage());  
            return false;
        }
        return false;
    }
    public static ArrayList<Student> FetchAllStudents(){
        String sqlStatement = "SELECT id, name, program, courses FROM students";
        PreparedStatement ps;
        try {
            ps = conn.prepareStatement(sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ArrayList<Student> students = new ArrayList<Student>();
            ResultSet rs = ps.executeQuery();
            rs.first();
            
            do {
                Student newStudent = new Student(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                students.add(newStudent);
            } while (rs.next());
            
            return students;
        } catch (SQLException ex) {
            System.out.println("Error executing query! " + ex.getMessage());  
            return null;
        }
            
    }    

}
