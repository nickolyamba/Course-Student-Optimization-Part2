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

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean isAssigned;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="STUDENT_ID", nullable = false)
    private Student student;
    
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="OFFERING_ID", nullable = false)
    private Offering offering;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="REQUEST_ID") //set to nullable to allow flexibility
    private Request request;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="OPTIMIZED_ID")
	private OptimizedTime optimizedTime;
    
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

	public Preference(Student student, Offering offering) {
		this.student = student;
		this.offering = offering;
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

    public OptimizedTime getOptimizedTime() {
        return optimizedTime;
    }

    public void setOptimizedTime(OptimizedTime optimizedTime) {
        this.optimizedTime = optimizedTime;
    }
/*
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Preference)) return false;

		Preference that = (Preference) o;

		if (!(student.getId() == that.student.getId())) return false;
		return offering.getId() == that.offering.getId();
	}

	@Override
	public int hashCode() {
		int result = student.hashCode();
		result = 31 * result + offering.hashCode();
		return result;
	}
*/

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

