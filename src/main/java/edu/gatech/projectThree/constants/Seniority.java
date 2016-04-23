package edu.gatech.projectThree.constants;

/**
 * Created by nick on 3/18/16.
 */
public enum Seniority {
    NULL(0),
    FRESHMAN(1),
    SOPHOMORE(2),
    JUNIOR(3),
    MASTERS(4),
    PHD(5),
    POSTDOC(6);

    private final int id;

    Seniority(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
