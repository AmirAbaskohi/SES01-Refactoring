package domain;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(EnrollmentRequest enrollmentRequest) throws EnrollmentRulesViolationException {
        for (CourseSectionExamDate courseSectionExamDate : enrollmentRequest.getCourses()) {
            enrollmentRequest.getStudent().courseHasBeenPassed(courseSectionExamDate);
            enrollmentRequest.getStudent().coursePrerequisitesHasBeenPassed(courseSectionExamDate);
            courseSectionExamDate.checkCourseExamTimeConflicts(enrollmentRequest.getCourses());
            courseSectionExamDate.checkDuplicateEnrollment(enrollmentRequest.getCourses());
        }
        enrollmentRequest.checkUnitsRequested();
        for (CourseSectionExamDate o : enrollmentRequest.getCourses())
            enrollmentRequest.getStudent().takeCourse(o.getCourse(), o.getSection());
	}


}
