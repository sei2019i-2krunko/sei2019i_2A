package co.edu.unal.krunko.sitespins.businessLogic;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterControllerTest {

    @Test
    public void validEmail() {
        assertTrue(RegisterController.invalidEmail("m"));
        assertTrue(RegisterController.invalidEmail("this.is@jach@yahoo.com"));

        assertFalse(RegisterController.invalidEmail("jfacostamu@unal.edu.co"));
        assertFalse(RegisterController.invalidEmail("mcsrk3@facebook.com"));
        assertFalse(RegisterController.invalidEmail("this.is.jach@hotmail.com"));
        assertTrue(RegisterController.invalidEmail("jfacostamu"));
        assertFalse(RegisterController.invalidEmail("jhonfredy_36@hotmail.com"));
    }
}