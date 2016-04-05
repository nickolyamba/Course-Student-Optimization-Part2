package edu.gatech.projectThree.datamodel.entity;

/**
 * Created by nick on 3/18/16.
 */
/*
@Entity
@DiscriminatorValue("userType")*/
public class ProfessorOffering extends TeacherOffering {

    public ProfessorOffering(){

    }

	// constructor
	public ProfessorOffering(User user, Offering offering) {
		super(user, offering);
	}


	@Override
    public String toString() {
		return super.toString();
	}
	
}

