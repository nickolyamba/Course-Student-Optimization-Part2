package edu.gatech.projectThree.constants;

/**
 * Created by dawu on 3/18/16.
 */
public enum Season {
    SUMMER(3),
    FALL(1),
    SPRING(2);

    private final int id;

    Season(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
