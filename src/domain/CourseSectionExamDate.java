package domain;
import domain.exceptions.EnrollmentRulesViolationException;

import java.util.Date;
import java.util.List;

public class CourseSectionExamDate {
	private Course course;
	private int section;
	private Date examDate;

	public CourseSectionExamDate(Course course) {
		this.course = course;
		this.section = 1;
		this.examDate = null;
	}

	public CourseSectionExamDate(Course course, Date examDate) {
		this.course = course;
		this.section = 1;
		this.examDate = examDate;
	}

	public CourseSectionExamDate(Course course, Date examDate, int section) {
		this.course = course;
		this.section = section;
		this.examDate = examDate;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public String toString() {
		return course.getName() + " - " + section;
	}
	
	public Date getExamTime() {
		return examDate;
	}

	public int getSection() { return section; }

	public boolean checkExamTimeConflict(CourseSectionExamDate c) {
		return getExamTime().equals(c.getExamTime());
	}

	public void checkDuplicateEnrollment(List<CourseSectionExamDate> courses) throws EnrollmentRulesViolationException {
		for (CourseSectionExamDate courseSectionExamDate : courses) {
			if (this == courseSectionExamDate)
				continue;
			if (this.getCourse().equals(courseSectionExamDate.getCourse()))
				throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice",
						this.getCourse().getName()));
		}
	}

	public void checkCourseExamTimeConflicts(List<CourseSectionExamDate> courses) throws EnrollmentRulesViolationException {
		for (CourseSectionExamDate courseSectionExamDate : courses) {
			if (this == courseSectionExamDate)
				continue;
			if (this.checkExamTimeConflict(courseSectionExamDate))
				throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time",
						this, courseSectionExamDate));
		}
	}
}
