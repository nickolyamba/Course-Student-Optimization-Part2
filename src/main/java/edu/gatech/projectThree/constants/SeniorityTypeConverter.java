package edu.gatech.projectThree.constants;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static edu.gatech.projectThree.constants.Seniority.*;

/**
 * Created by Kolya on 4/10/2016.
 */
// https://www.javacodegeeks.com/2014/05/jpa-2-1-type-converter-the-better-way-to-persist-enums.html
@Converter(autoApply = true)
public class SeniorityTypeConverter implements AttributeConverter<Seniority, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Seniority attribute) {
        switch (attribute) {
            case FRESHMAN:
                return 1;
            case SOPHOMORE:
                return 2;
            case JUNIOR:
                return 3;
            case MASTERS:
                return 4;
            case PHD:
                return 5;
            case POSTDOC:
                return 6;
            case NULL:
                return 0;
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }

    @Override
    public Seniority convertToEntityAttribute(Integer dbData) {
        switch (dbData) {
            case 1:
                return FRESHMAN;
            case 2:
                return SOPHOMORE;
            case 3:
                return JUNIOR;
            case 4:
                return MASTERS;
            case 5:
                return PHD;
            case 6:
                return POSTDOC;
            case 0:
                return NULL;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }
}
