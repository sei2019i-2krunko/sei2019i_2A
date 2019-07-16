package co.edu.unal.krunko.sitespins.businessLogic;

import android.app.Activity;

import java.util.concurrent.ExecutionException;

import co.edu.unal.krunko.sitespins.dataAccess.repositories.UserRepository;

public class RegisterController {

	private Activity activity;

	public RegisterController(Activity activity) {
		this.activity = activity;
	}

	public enum RegisterStatus {
		PENDING,
		INVALID_NAME,
		INVALID_EMAIL,
		NAME_NOT_UPDATED,
		INVALID_PASSWORD,
		REGISTER_SUCCESSFUL,
		REGISTER_UNSUCCESSFUL
	}

	public static boolean invalidEmail(String email) {
		return email == null || !email.toLowerCase().matches("^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$");
	}

	public RegisterStatus registerWithEmailAndPassword(String name, String email, String password) throws ExecutionException, InterruptedException {
		if (name == null || name.isEmpty()) {
			return RegisterStatus.INVALID_NAME;
		} else if (RegisterController.invalidEmail(email)) {
			return RegisterStatus.INVALID_EMAIL;
		} else if (password == null || password.length() < 8) {
			return RegisterStatus.INVALID_PASSWORD;
		}

		UserRepository userRepository = new UserRepository(this.activity);

		userRepository.createUserWithEmailAndPassword(email, password);
		userRepository.updateCurrentUserName(name);

		return RegisterStatus.PENDING;
	}
}
