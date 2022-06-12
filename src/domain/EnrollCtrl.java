package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(Student s, List<CSE> courses) throws EnrollmentRulesViolationException {
        Map<Term, Map<Course, Double>> transcript = s.getTranscript();
        for (CSE o : courses) {
            s.courseHasBeenPassed(o);
            s.coursePrerequisitesHasBeenPassed(o);
            checkCourseExamTimeConflicts(courses, o);
            checkDuplicateEnrollment(courses, o);
        }
		int unitsRequested = 0;
		for (CSE o : courses)
			unitsRequested += o.getCourse().getUnits();
        double gpa = s.getGpa();
        if ((gpa < 12 && unitsRequested > 14) ||
				(gpa < 16 && unitsRequested > 16) ||
				(unitsRequested > 20))
			throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, gpa));
        // Take the course
		for (CSE o : courses)
			s.takeCourse(o.getCourse(), o.getSection());
	}

    private void checkDuplicateEnrollment(List<CSE> courses, CSE o) throws EnrollmentRulesViolationException {
        for (CSE o2 : courses) {
            if (o == o2)
                continue;
            if (o.getCourse().equals(o2.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
        }
    }

    private void checkCourseExamTimeConflicts(List<CSE> courses, CSE o) throws EnrollmentRulesViolationException {
        for (CSE o2 : courses) {
            if (o == o2)
                continue;
            if (o.checkExamTimeConflict(o2))
                throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
        }
    }

}
