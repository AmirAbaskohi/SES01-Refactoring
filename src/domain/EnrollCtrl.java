package domain;

import java.util.List;

import domain.exceptions.EnrollmentRequest;
import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(EnrollmentRequest enrollmentRequest) throws EnrollmentRulesViolationException {
        for (CourseSectionExamDate courseSectionExamDate : enrollmentRequest.getCourses()) {
            enrollmentRequest.getStudent().courseHasBeenPassed(courseSectionExamDate);
            enrollmentRequest.getStudent().coursePrerequisitesHasBeenPassed(courseSectionExamDate);
            checkCourseExamTimeConflicts(enrollmentRequest.getCourses(), courseSectionExamDate);
            checkDuplicateEnrollment(enrollmentRequest.getCourses(), courseSectionExamDate);
        }
        checkUnitsRequested(enrollmentRequest);
        for (CourseSectionExamDate o : enrollmentRequest.getCourses())
            enrollmentRequest.getStudent().takeCourse(o.getCourse(), o.getSection());
	}

    private void checkUnitsRequested(EnrollmentRequest enrollmentRequest) throws EnrollmentRulesViolationException {
        if ((enrollmentRequest.getStudent().getGpa() < 12 && getUnitsRequested(enrollmentRequest.getCourses()) > 14) ||
				(enrollmentRequest.getStudent().getGpa() < 16 && getUnitsRequested(enrollmentRequest.getCourses()) > 16) ||
				(getUnitsRequested(enrollmentRequest.getCourses()) > 20))
			throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f",
                    getUnitsRequested(enrollmentRequest.getCourses()), enrollmentRequest.getStudent().getGpa()));
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
