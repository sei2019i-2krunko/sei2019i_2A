package co.edu.unal.krunko.sitespins.dataAccess.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

import co.edu.unal.krunko.sitespins.dataAccess.models.User;


public class UserRepository {

	private FirebaseAuth auth;
	private User user;

	public UserRepository() {
		this.auth = FirebaseAuth.getInstance();
		this.user = User.fromFirebaseUser(this.auth.getCurrentUser());
	}

	public User getUser() {
		return this.user;
	}


	public User createUserWithEmailAndPassword(String _email, String _password) throws Exception {
		final Exception[] exception = {null};
		auth.createUserWithEmailAndPassword(_email, _password).addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {
					user = User.fromFirebaseUser(auth.getCurrentUser());
				} else {
					user = null;
					exception[0] = task.getException();
				}
			}
		});

		if (exception[0] != null) {
			throw exception[0];
		}
		return user;
	}
}
