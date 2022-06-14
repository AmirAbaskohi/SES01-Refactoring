package domain.exceptions;

import domain.CourseSectionExamDate;
import domain.Student;

import java.util.List;

public class EnrollmentRequest {
    private Student student;
    private List<CourseSectionExamDate> courses;

    public EnrollmentRequest(Student student, List<CourseSectionExamDate> courses) {
        this.student = student;
        this.courses = courses;
    }

    public Student getStudent() {
        return student;
    }

    public List<CourseSectionExamDate> getCourses() {
        return courses;
    }


}
