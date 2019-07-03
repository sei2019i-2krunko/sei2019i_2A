package co.edu.unal.krunko.sitespins.businessLogic;

import co.edu.unal.krunko.sitespins.dataAccess.repositories.UserRepository;

public class RegisterController {

	public enum RegisterStatus {
		INVALID_NAME,
		INVALID_EMAIL,
		INVALID_PASSWORD,
		REGISTER_UNSUCCESSFUL,
		REGISTER_SUCCESSFUL
	}

	public static RegisterStatus registerWithEmailAndPassword(String name, String email, String password) {
		if (name == null || name.isEmpty()) {
			return RegisterStatus.INVALID_NAME;
		} else if (email == null || !email.toLowerCase().matches("^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$")) {
			return RegisterStatus.INVALID_EMAIL;
		} else if (password == null || password.length() < 8) {
			return RegisterStatus.INVALID_PASSWORD;
		}

		UserRepository userRepository = new UserRepository();

		try {
			userRepository.createUserWithEmailAndPassword(email, password);
		} catch (Exception e) {
			e.printStackTrace();
			return RegisterStatus.REGISTER_UNSUCCESSFUL;
		}

		// TODO: change the user's name

		return RegisterStatus.REGISTER_SUCCESSFUL;
	}
}
