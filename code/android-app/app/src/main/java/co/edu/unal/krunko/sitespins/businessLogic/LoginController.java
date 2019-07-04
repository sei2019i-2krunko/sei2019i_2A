package co.edu.unal.krunko.sitespins.businessLogic;

import co.edu.unal.krunko.sitespins.dataAccess.repositories.UserRepository;

public class LoginController {

	public enum LoginStatus {
		/**
		 * Here we show the auth state while logging in
		 * <p>
		 * WRONG CREDENTIALS: the email or the password are/is invalid.
		 * SUCCESSFUL_LOGIN: the user logging in was successful.
		 * EMAIL_IS_REQUIRED_OR_INVALID: if the user has enter an invalid email.
		 * PASSWORD_IS_REQUIRED: if the password has a length lower than 6 characters.
		 */
		WRONG_CREDENTIALS,
		SUCCESSFUL_LOGIN,
		EMAIL_IS_REQUIRED_OR_INVALID,
		PASSWORD_IS_REQUIRED
	}

	public static LoginStatus loginWithEmailAndPassword(String email, String password) {
		if (email == null
				|| !email.toLowerCase().matches("^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$")) {
			return LoginStatus.EMAIL_IS_REQUIRED_OR_INVALID;
		} else if (password == null || password.length() < 8) {
			return LoginStatus.PASSWORD_IS_REQUIRED;
		}

		UserRepository userRepository = new UserRepository();

		try {
			userRepository.getUserByEmailAndPassword(email, password);
		} catch (Exception e) {
			e.printStackTrace();
			return LoginStatus.WRONG_CREDENTIALS;
		}


		return LoginStatus.SUCCESSFUL_LOGIN;
	}

}
