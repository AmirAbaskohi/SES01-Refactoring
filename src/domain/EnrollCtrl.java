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
        if ((enrollmentRequest.getStudent().getGpa() < 12 && enrollmentRequest.getUnitsRequested() > 14) ||
				(enrollmentRequest.getStudent().getGpa() < 16 && enrollmentRequest.getUnitsRequested() > 16) ||
				(enrollmentRequest.getUnitsRequested() > 20))
			throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f",
                    enrollmentRequest.getUnitsRequested(), enrollmentRequest.getStudent().getGpa()));
    }

    private void checkDuplicateEnrollment(List<CourseSectionExamDate> courses, CourseSectionExamDate reviewingCourseSectionExamDate) throws EnrollmentRulesViolationException {
        for (CourseSectionExamDate courseSectionExamDate : courses) {
            if (reviewingCourseSectionExamDate == courseSectionExamDate)
                continue;
            if (reviewingCourseSectionExamDate.getCourse().equals(courseSectionExamDate.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice",
                        reviewingCourseSectionExamDate.getCourse().getName()));
        }
    }

    private void checkCourseExamTimeConflicts(List<CourseSectionExamDate> courses, CourseSectionExamDate reviewingCourseSectionExamDate) throws EnrollmentRulesViolationException {
        for (CourseSectionExamDate courseSectionExamDate : courses) {
            if (reviewingCourseSectionExamDate == courseSectionExamDate)
                continue;
            if (reviewingCourseSectionExamDate.checkExamTimeConflict(courseSectionExamDate))
                throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", reviewingCourseSectionExamDate, courseSectionExamDate));
        }
    }

}
