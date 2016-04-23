package edu.gatech.projectThree.constants;

import org.springframework.context.annotation.Role;

import java.lang.annotation.Annotation;

/**
 * Created by dawu on 3/18/16.
 */
public enum UserType implements Role {
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

    @Override
    public int value() {
        return 0;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
