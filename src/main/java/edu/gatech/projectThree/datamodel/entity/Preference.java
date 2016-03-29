package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by nick on 3/18/16.
 */
@Entity
public class Preference implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    
    @Column(nullable = false)
	private int priority;
    
    @Column(nullable = true)
	private String recommend;
    
    private boolean isAssigned;
    
    @ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinColumn(name="STUDENT_ID")
    private Student student;
    
    @ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
    @JoinColumn(name="OFFERING_ID")
    private Offering offering;
    
    public Preference() {
        
    }
	
	// constructor
	// @param: none
	public Preference(Student student, Offering offering, int priority) {
		this.student = student;
		this.offering = offering;
		this.priority = priority;
		isAssigned = false; // by default Student is not assigned to a course, until 
		                    // Compute Engine produces solution that have a Student 
		                    // assigned to the Course 
	}

	/**
	 * @return the student
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * @param student the student to set
	 */
	public void setStudent(Student student) {
		this.student = student;
	}

	/**
	 * @return the offering
	 */
	public Offering getOffering() {
		return offering;
	}

	/**
	 * @param offering the offering to set
	 */
	public void setOffering(Offering offering) {
		this.offering = offering;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the recommend
	 */
	public String getRecommend() {
		return recommend;
	}

	/**
	 * @param recommend the recommend to set
	 */
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	
    /**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the isAssigned
	 */
	public boolean isAssigned() {
		return isAssigned;
	}

	/**
	 * @param isAssigned the isAssigned to set
	 */
	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

	@Override
    public String toString() {
        return "Preference{" +
                "id=" + id +
                ", student='" + student.toString() + '\'' +
                ", offering='" + offering.toString() + '\'' +
                ", priority='" + priority + '\'' +
                ", recommendation='" + recommend + '\'' +
                '}';
    }
	
}
