package Encost;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CategorisingUsersTest {
    
    @ParameterizedTest
    @ValueSource(strings = {"1", "2"})
    @DisplayName("Valid User Check Test For Categorising User Type")
    public void validateUserInputValid(String input) {
        // boolean result should be returned to indicate the success or failure of login
        boolean result = Encost.checkUserType(input);
        Assertions.assertTrue(result);
    }

    @ParameterizedTest
    @DisplayName("Invalid User Check Test For Categorising User Type")
    @ValueSource(strings = {"one", "two", "community", "user", "encost", "Encost",
        "community user", "2 Encost", "1111111111111111111111111111111111", "COMMUNITY", "admin"})
    public void validateUserInputInvalid(String input) {
        // boolean result should be returned to indicate the success or failure of login
        boolean result = Encost.checkUserType(input);
        Assertions.assertFalse(result);
    }
}
