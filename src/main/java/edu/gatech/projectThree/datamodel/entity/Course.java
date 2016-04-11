package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dawu on 3/18/16.
 */
@Entity
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(nullable = false)
    private String course_name;
    
    @Column(nullable = false)
    private String course_num;

	private boolean fall_term;

	private boolean spring_term;

	private boolean summer_term;
    
    // Recursive aggregation

	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "PreCourse",
			joinColumns =
			@JoinColumn(name = "course", referencedColumnName = "ID"),
			inverseJoinColumns =
			@JoinColumn(name = "prereq", referencedColumnName = "ID"))
    private Set<Course> prereqs;

    @OneToMany(mappedBy="course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Offering> offerings = new HashSet<Offering>();

	public Course(){}

    public Course(int course_id, String course_name, String course_num) {
        this.id = course_id;
        this.course_name = course_name;
        this.course_num = course_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

	public boolean isFall_term() {
		return fall_term;
	}

	public void setFall_term(boolean fall_term) {
		this.fall_term = fall_term;
	}

	public boolean isSpring_term() {
		return spring_term;
	}

	public void setSpring_term(boolean spring_term) {
		this.spring_term = spring_term;
	}

	public boolean isSummer_term() {
		return summer_term;
	}

	public void setSummer_term(boolean summer_term) {
		this.summer_term = summer_term;
	}
	
	public void addPrereq(Course course) {
		prereqs.add(course);
	}

	public void removePrereq(Course course) {
		prereqs.remove(course);
	}

	public void addOffering(Offering offering) {
		offerings.add(offering);
		offering.setCourse(this);
	}

	public void removeOffering(Offering offering) {
		offerings.remove(offering);
		offering.setCourse(null);
	}

	public Set<Course> getPrereqs() {
		return prereqs;
	}

	public void setPrereqs(Set<Course> prereqs) {
		this.prereqs = prereqs;
	}

	public Set<Offering> getOfferings() {
		return offerings;
	}

	public void setOfferings(Set<Offering> offerings) {
		this.offerings = offerings;
	}

	@Override
    public String toString() {
        return "Course{" +
                "course_id=" + id +
				", course_name='" + course_name + '\'' +
				", course_num='" + course_num + '\'' +
                //"prereq=" + prereqs.toString() +
                //", offering=" + offerings +
                '}';
    }
}
