package edu.gatech.projectThree.datamodel.entity;

import edu.gatech.projectThree.constants.CourseType;

import java.util.Set;

import javax.persistence.*;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    
    @Column(nullable = false)
    private String course_name;
    
    @Column(nullable = false)
    private String course_num;
    
    @Column(nullable = false)
    private CourseType type;
    
    // Recursive aggregation
    @ManyToOne
    private Course parent;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="parent")
    private Set<Course> prereqs;
    
    @OneToMany(fetch=FetchType.LAZY, mappedBy="course")
    private Set<Offering> offerings;

    public Course(int course_id, String course_name, String course_num) {
        this.id = course_id;
        this.course_name = course_name;
        this.course_num = course_num;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public CourseType getType() {
        return type;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    /**
	 * @return the course_name
	 */
	public String getCourse_name() {
		return course_name;
	}

	/**
	 * @param course_name the course_name to set
	 */
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	/**
	 * @return the course_num
	 */
	public String getCourse_num() {
		return course_num;
	}

	/**
	 * @param course_num the course_num to set
	 */
	public void setCourse_num(String course_num) {
		this.course_num = course_num;
	}

	/**
	 * @return the prereq
	 */
	public Set<Course> getPrereq() {
		return prereqs;
	}

	/**
	 * @param prereq the prereq to set
	 */
	public void setPrereq(Set<Course> prereq) {
		this.prereqs = prereq;
	}
	
	/**
	 * @return the offering
	 */
	public Set<Offering> getOffering() {
		return offerings;
	}

	/**
	 * @param offering the offering to set
	 */
	public void setOffering(Set<Offering> offering) {
		this.offerings = offering;
	}
	
	public void addPrereq(Course course) {
		prereqs.add(course);
		parent = this;
	}
	
	public void addOffering(Offering offering) {
		offerings.add(offering);
	}

	@Override
    public String toString() {
        return "Course{" +
                "course_id=" + id +
                "course_name=" + course_name +
                "course_num=" + course_num +
                "prereq=" + prereqs +
                "coereq=" + prereqs +
                ", offering=" + offerings +
                ", type=" + type +
                '}';
    }
}
