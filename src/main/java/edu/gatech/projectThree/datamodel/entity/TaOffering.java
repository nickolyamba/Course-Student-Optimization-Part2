package edu.gatech.projectThree.datamodel.entity;

/**
 * Created by nick on 3/18/16.
 */
/*
@Entity
@DiscriminatorValue("userType")*/
public class TaOffering extends TeacherOffering {

	public TaOffering(){

	}

	// constructor
	public TaOffering(User user, Offering offering) {
		super(user, offering);
	}


	@Override
	public String toString() {
		return super.toString();
	}
}

