package domain.exceptions;

import domain.CourseSectionExamDate;
import domain.Student;

import java.util.List;

public class EnrollmentRequest {
    private Student student;
    private List<CourseSectionExamDate> courses;
}
