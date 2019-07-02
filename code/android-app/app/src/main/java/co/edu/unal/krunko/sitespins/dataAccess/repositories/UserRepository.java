package co.edu.unal.krunko.sitespins.dataAccess.repositories;

import com.google.firebase.auth.FirebaseAuth;


public class UserRepository {

	private FirebaseAuth auth;

	public UserRepository() {
		this.auth = FirebaseAuth.getInstance();
	}

	//TODO: implement the creation of the user with email and password

}
