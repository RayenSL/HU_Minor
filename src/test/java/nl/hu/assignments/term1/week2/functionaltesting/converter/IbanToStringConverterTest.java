package nl.hu.assignments.term1.week2.functionaltesting.converter;

import nl.hu.assignments.term1.week2.functionaltesting.data.IbanToStringConverter;
import org.iban4j.Iban;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class IbanToStringConverterTest {

    @InjectMocks
    private IbanToStringConverter converter;

    @Test
    public void givenAValidNonSpaceIban_whenConvertToAttribute_thenReturnStringRepresentation() {
        final var iban = Iban.valueOf("NL73INGB7071437513");

        final var attribute = converter.convertToDatabaseColumn(iban);

        assertThat(attribute).isEqualTo("NL73INGB7071437513");
    }

    @Test
    public void givenNullIban_whenConvertToAttribute_thenThrowException() {
        assertThatThrownBy(() -> converter.convertToDatabaseColumn(null))
                .isInstanceOf(NullPointerException.class);
    }

    @ParameterizedTest
    @MethodSource("convertToEntityAttribute_values")
    public void givenAStringIban_whenConvertToEntityAttribute_thenReturnExpectedIbanOfStringIban(String given, Iban expected) {
        final var iban = converter.convertToEntityAttribute(given);
        assertThat(iban).isEqualTo(expected);
    }

    private static Stream<Arguments> convertToEntityAttribute_values() {
        final var iban = Iban.valueOf("NL73INGB7071437513");
        return Stream.of(
                Arguments.of("NL73INGB7071437513", iban), //packed
                Arguments.of("NL73 INGB 7071 4375 13", iban), //unpacked
                Arguments.of(null, null), //null
                Arguments.of("", null), //empty
                Arguments.of(" ", null) //whitespace empty
        );
    }
}
