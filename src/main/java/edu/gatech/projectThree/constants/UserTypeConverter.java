package edu.gatech.projectThree.constants;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static edu.gatech.projectThree.constants.UserType.*;

/**
 * Created by Kolya on 4/10/2016.
 */
// https://www.javacodegeeks.com/2014/05/jpa-2-1-type-converter-the-better-way-to-persist-enums.html
@Converter(autoApply = true)
public class UserTypeConverter implements AttributeConverter<UserType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(UserType attribute) {
        switch (attribute) {
            case STUDENT:
                return 1;
            case PROFESSOR:
                return 2;
            case TA:
                return 3;
            case ADMINISTRATOR:
                return 4;
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }

    @Override
    public UserType convertToEntityAttribute(Integer dbData) {
        switch (dbData) {
            case 1:
                return STUDENT;
            case 2:
                return PROFESSOR;
            case 3:
                return TA;
            case 4:
                return ADMINISTRATOR;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }
}
