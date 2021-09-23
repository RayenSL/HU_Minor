package nl.hu.assignments.term1.week2.functionaltesting.data;

import org.apache.logging.log4j.util.Strings;
import org.iban4j.Iban;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;

@Component
public class IbanToStringConverter implements AttributeConverter<Iban, String> {
    @Override
    public String convertToDatabaseColumn(Iban attribute) {
        return attribute.toString();
    }

    @Override
    public Iban convertToEntityAttribute(String dbData) {
        if (Strings.isBlank(dbData)) {
            return null;
        }
        dbData = dbData.replace(" ", "");
        return Iban.valueOf(dbData);
    }
}
