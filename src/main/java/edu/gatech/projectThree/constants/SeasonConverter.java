package edu.gatech.projectThree.constants;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static edu.gatech.projectThree.constants.Season.*;

/**
 * Created by Kolya on 4/10/2016.
 */
// https://www.javacodegeeks.com/2014/05/jpa-2-1-type-converter-the-better-way-to-persist-enums.html
@Converter(autoApply = true)
public class SeasonConverter implements AttributeConverter<Season, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Season attribute) {
        switch (attribute) {
            case FALL:
                return 1;
            case SPRING:
                return 2;
            case SUMMER:
                return 3;
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }

    @Override
    public Season convertToEntityAttribute(Integer dbData) {
        switch (dbData) {
            case 1:
                return FALL;
            case 2:
                return SPRING;
            case 3:
                return SUMMER;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }
}
