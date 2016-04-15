package edu.gatech.projectThree.datamodel.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by nick on 3/18/16.
 */
@Entity
public class Preference implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @Column(nullable = false)
	private int priority;
    
    @Column
	private String recommend;

    @Transient
    private boolean isAssigned;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="STUDENT_ID", nullable = false)
    private Student student;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OFFERING_ID", nullable = false)
    private Offering offering;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="REQUEST_ID", nullable = false)
    private Request request;
    
    public Preference() {
        
    }
	
	// constructor
	public Preference(Student student, Offering offering, int priority) {
		this.student = student;
		this.offering = offering;
		this.priority = priority;
        this.request = null;
		isAssigned = false; // by default Student is not assigned to a course, until 
		                    // Compute Engine produces solution that have a Student 
		                    // assigned to the Course. Set upon results of Gurobi computation
	}

	public Preference(Student student, Offering offering, int priority, Request request) {
		this.student = student;
		this.offering = offering;
		this.priority = priority;
		this.request = request;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Offering getOffering() {
		return offering;
	}

	public void setOffering(Offering offering) {
		this.offering = offering;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isAssigned() {
		return isAssigned;
	}

	public void setAssigned(boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public String toString() {
        return "Preference{" +
                "id=" + id +
                ", student='" + student.toString() + '\'' +
                ", offering='" + offering.toString() + '\'' +
                ", priority='" + priority + '\'' +
                ", recommendation='" + recommend + '\'' +
				", isAssigned='" + isAssigned + '\'' +
                '}';
    }
	
}

