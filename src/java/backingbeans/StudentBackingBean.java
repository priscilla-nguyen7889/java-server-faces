/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.ManagedBean;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author prisc
 */
@Named(value = "studentBackingBean")

@SessionScoped
public class StudentBackingBean implements Serializable {
   
    @Inject 
    private Student student;//https://docs.oracle.com/javaee/6/tutorial/doc/gjbbk.html
    @Inject 
    private StudentDL sdl;
    private ArrayList<String> courses;

    public StudentBackingBean(Student student, ArrayList<String> courses) {
        this.student = student;
        this.courses = courses;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    public ArrayList<Student> getAllStudents(){
        try {
            return sdl.FetchAllStudents();
        } catch (Exception ex) {
            System.out.println("Error getting a student from the database! " + ex.getMessage());
            return null;
        }
    }
    public String AddStudent(){

        String stringCourses = "";
        for (String course: courses){
            stringCourses += course + "|";
        }
        stringCourses=stringCourses.substring(0,stringCourses.length()-1);
        student.setCourses(stringCourses);
        
        try {
           if (sdl.InsertStudent(student)) {
                    return "DisplayStudents.xhtml?faces-redirect=true";
                }
                else return "DisplayStudents.xhtml?faces-redirect=true";
            
            
        } catch (Exception ex) {
            System.out.println("Error adding a book to the database! " + ex.getMessage());
            return "DisplayStudents.xhtml?faces-redirect=true";
        }
    }
    public StudentBackingBean() {
    }
 
}
