package domain;

import java.util.List;

import domain.exceptions.EnrollmentRequest;
import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(EnrollmentRequest enrollmentRequest) throws EnrollmentRulesViolationException {
        for (CourseSectionExamDate o : enrollmentRequest.getCourses()) {
            enrollmentRequest.getStudent().courseHasBeenPassed(o);
            enrollmentRequest.getStudent().coursePrerequisitesHasBeenPassed(o);
            checkCourseExamTimeConflicts(enrollmentRequest.getCourses(), o);
            checkDuplicateEnrollment(enrollmentRequest.getCourses(), o);
        }
        checkUnitsRequested(enrollmentRequest.getStudent(), enrollmentRequest.getCourses());
        for (CourseSectionExamDate o : enrollmentRequest.getCourses())
            enrollmentRequest.getStudent().takeCourse(o.getCourse(), o.getSection());
	}

    private void checkUnitsRequested(Student s, List<CourseSectionExamDate> courses) throws EnrollmentRulesViolationException {
        if ((s.getGpa() < 12 && getUnitsRequested(courses) > 14) ||
				(s.getGpa() < 16 && getUnitsRequested(courses) > 16) ||
				(getUnitsRequested(courses) > 20))
			throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", getUnitsRequested(courses), s.getGpa()));
    }

    private int getUnitsRequested(List<CourseSectionExamDate> courses) {
        return courses.stream().mapToInt(course -> course.getCourse().getUnits()).sum();
    }

    private void checkDuplicateEnrollment(List<CourseSectionExamDate> courses, CourseSectionExamDate o) throws EnrollmentRulesViolationException {
        for (CourseSectionExamDate o2 : courses) {
            if (o == o2)
                continue;
            if (o.getCourse().equals(o2.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
        }
    }

    private void checkCourseExamTimeConflicts(List<CourseSectionExamDate> courses, CourseSectionExamDate o) throws EnrollmentRulesViolationException {
        for (CourseSectionExamDate o2 : courses) {
            if (o == o2)
                continue;
            if (o.checkExamTimeConflict(o2))
                throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
        }
    }

}
