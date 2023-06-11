package Encost;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class AccountLoginTest {

    private static Stream<Arguments> ValidLoginDetails() {
        // valid login username and password pairs
        return Stream.of(
            Arguments.of("encostUserA", "password789"),
            Arguments.of("encostUserB", "password234")
        );
        
    }

    @ParameterizedTest
    @MethodSource("ValidLoginDetails")
    @DisplayName("Valid Username, Password pairs Check Test For Account Login")
    public void checkLoginPairTestValid(String username, String password){
        // boolean result should be returned to indicate the success or failure of login
        boolean result = Encost.checkLoginPair(username, password);
        Assertions.assertTrue(result);
    }

    private static Stream<Arguments> InvalidLoginDetails() {
        return Stream.of(
            // valid login username and password pairs
            Arguments.of("username", "password"),
            Arguments.of("admin", "encost")
        );
    }

    @ParameterizedTest
    @MethodSource("InvalidLoginDetails")
    @DisplayName("Invalid Username, Password pairs Check Test For Account Login")
    public void checkLoginPairTestInvalid(String username, String password){
        // boolean result should be returned to indicate the success or failure of login
        boolean result = Encost.checkLoginPair(username, password);
        Assertions.assertFalse(result);
    }
}
