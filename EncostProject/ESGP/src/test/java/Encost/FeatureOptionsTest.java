package Encost;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FeatureOptionsTest {

    @ParameterizedTest
    @ValueSource(strings = {"1", "x"})
    @DisplayName("Community Valid Feature Option Check Test For Feature Options")
    public void communityFeatureTestValid(String input) {
        // Set User type
        Encost.setUserType("community");
        // result boolean indicates success or failure of the input validation
        boolean result = Encost.checkFeatureOption(input);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2", "3", "e"})
    @DisplayName("Community Invalid Feature Option Check Test For Feature Options")
    public void communityFeatureTestInvalid(String input) {
        // Set User type
        Encost.setUserType("community");
        // result boolean indicates success or failure of the input validation
        boolean result = Encost.checkFeatureOption(input);
        Assertions.assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2", "3", "x"})
    @DisplayName("Encost Valid Feature Option Check Test For Feature Options")
    public void encostFeatureTestValid(String input) {
        // Set User type
        Encost.setUserType("encost-verified");
        // result boolean indicates success or failure of the input validation
        boolean result = Encost.checkFeatureOption(input);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"4", "0", "z"})
    @DisplayName("Encost Invalid Feature Option Check Test For Feature Options")
    public void encostFeatureTestInvalid(String input) {
        // Set User type
        Encost.setUserType("encost-verified");
        // result boolean indicates success or failure of the input validation
        boolean result = Encost.checkFeatureOption(input);
        Assertions.assertFalse(result);
    }

}
