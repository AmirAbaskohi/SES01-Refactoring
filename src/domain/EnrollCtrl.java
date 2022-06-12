package domain;

import java.util.List;
import java.util.Map;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
	public void enroll(Student s, List<CSE> courses) throws EnrollmentRulesViolationException {
        Map<Term, Map<Course, Double>> transcript = s.getTranscript();
        for (CSE o : courses) {
            s.courseHasBeenPassed(o);
			nextPre:
			for (Course pre : o.getCourse().getPrerequisites()) {
                for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
                    for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                        if (r.getKey().equals(pre) && r.getValue() >= 10)
                            continue nextPre;
                    }
				}
				throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), o.getCourse().getName()));
			}
            // Checking the taken twice and time conflict
            for (CSE o2 : courses) {
                if (o == o2)
                    continue;
                if (o.getExamTime().equals(o2.getExamTime()))
                    throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));
                if (o.getCourse().equals(o2.getCourse()))
                    throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
            }
		}
        // Checking units requested with the student's GPA
		int unitsRequested = 0;
		for (CSE o : courses)
			unitsRequested += o.getCourse().getUnits();
		double points = 0;
		int totalUnits = 0;
        for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
            for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
                points += r.getValue() * r.getKey().getUnits();
                totalUnits += r.getKey().getUnits();
            }
		}
		double gpa = points / totalUnits;
		if ((gpa < 12 && unitsRequested > 14) ||
				(gpa < 16 && unitsRequested > 16) ||
				(unitsRequested > 20))
			throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, gpa));
        // Take the course
		for (CSE o : courses)
			s.takeCourse(o.getCourse(), o.getSection());
	}


}
