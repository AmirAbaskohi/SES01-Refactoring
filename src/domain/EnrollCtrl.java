package domain;

import java.util.List;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(Student s, List<CourseSectionExamDate> courses) throws EnrollmentRulesViolationException {
        for (CourseSectionExamDate o : courses) {
            s.courseHasBeenPassed(o);
            s.coursePrerequisitesHasBeenPassed(o);
            checkCourseExamTimeConflicts(courses, o);
            checkDuplicateEnrollment(courses, o);
        }
        checkUnitsRequested(s, courses);
        for (CourseSectionExamDate o : courses)
			s.takeCourse(o.getCourse(), o.getSection());
	}

    private void checkUnitsRequested(Student s, List<CourseSectionExamDate> courses) throws EnrollmentRulesViolationException {
        int unitsRequested = 0;
        for (CourseSectionExamDate o : courses)
            unitsRequested += o.getCourse().getUnits();
        if ((s.getGpa() < 12 && unitsRequested > 14) ||
				(s.getGpa() < 16 && unitsRequested > 16) ||
				(unitsRequested > 20))
			throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, s.getGpa()));
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
