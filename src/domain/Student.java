package domain;
import domain.exceptions.EnrollmentRulesViolationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
	private String id;
	private String name;

	static class CourseSection {
        CourseSection(Course course, int section) {
            this.course = course;
            this.section = section;
        }
        Course course;
	    int section;
    }
	private Map<Term, Map<Course, Double>> transcript;
	private List<CourseSection> currentTerm;

	public Student(String id, String name) {
		this.id = id;
		this.name = name;
		this.transcript = new HashMap<>();
		this.currentTerm = new ArrayList<>();
	}
	
	public void takeCourse(Course c, int section) {
		currentTerm.add(new CourseSection(c, section));
	}

	public Map<Term, Map<Course, Double>> getTranscript() {
		return transcript;
	}

	public void addTranscriptRecord(Course course, Term term, double grade) {
	    if (!transcript.containsKey(term))
	        transcript.put(term, new HashMap<>());
	    transcript.get(term).put(course, grade);
    }

    public List<CourseSection> getCurrentTerm() {
        return currentTerm;
    }

    public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}

	public void courseHasBeenPassed(CourseSectionExamDate o) throws EnrollmentRulesViolationException {
		for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
			for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
				if (r.getKey().equals(o.getCourse()) && r.getValue() >= 10)
					throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));
			}
		}
	}

	public void coursePrerequisitesHasBeenPassed(CourseSectionExamDate o) throws EnrollmentRulesViolationException {
		for (Course pre : o.getCourse().getPrerequisites()) {
			if (!pre.checkStudentPrerequisites(this))
				throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), o.getCourse().getName()));
		}
	}

	public double getGpa() {
		double points = 0;
		int totalUnits = 0;
		for (Map.Entry<Term, Map<Course, Double>> tr : transcript.entrySet()) {
			for (Map.Entry<Course, Double> r : tr.getValue().entrySet()) {
				points += r.getValue() * r.getKey().getUnits();
				totalUnits += r.getKey().getUnits();
			}
		}
		return points / totalUnits;
	}
}
