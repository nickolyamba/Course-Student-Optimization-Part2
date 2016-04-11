package edu.gatech.projectThree.constants;

/**
 * Created by dawu on 3/18/16.
 */
public enum UserType {
    STUDENT(1),
    PROFESSOR(2),
    TA(3),
    ADMINISTRATOR(4);

    private final int id;

    UserType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
