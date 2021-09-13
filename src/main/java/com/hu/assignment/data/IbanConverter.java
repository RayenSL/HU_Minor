package com.hu.assignment.data;

import org.iban4j.Iban;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.AttributeConverter;

@Component
public class IbanConverter implements AttributeConverter<Iban, String> {
    @Override
    public String convertToDatabaseColumn(Iban attribute) {
        return attribute.toString();
    }

    @Override
    public Iban convertToEntityAttribute(String dbData) {
        if (StringUtils.isEmpty(dbData)) {
            return null;
        }
        dbData = dbData.replace(" ", "");
        return Iban.valueOf(dbData);
    }
}
