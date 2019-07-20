package co.edu.unal.krunko.sitespins.dataAccess.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import co.edu.unal.krunko.sitespins.dataAccess.models.PinUser;

import static com.google.android.gms.tasks.Tasks.await;

public class PinRepository {

	private FirebaseFunctions functions;

	public PinRepository() {
		this.functions = FirebaseFunctions.getInstance();
	}

	/**
	 * This method only works for non-admin users.
	 * It calls the cloud function save_new_geo_point which returns the document id of the Pin created in Firebase.
	 *
	 * @param point Pin's location.
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public PinUser createNewPin(GeoPoint point) throws ExecutionException, InterruptedException, NullPointerException {
		HashMap<String, Object> parameters = new HashMap<>();
		String autoId;

		parameters.put("point", point);

		autoId = (String) await(
				this.functions.getHttpsCallable("save_new_geo_point")
						.call(parameters)
						.continueWith(new Continuation<HttpsCallableResult, Object>() {
							@Override
							public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
								HttpsCallableResult result = task.getResult();

								if (result != null) {
									Map<String, Object> values = (Map<String, Object>) result.getData();
									return values.get("autoId");
								}
								return null;
							}
						})
		);

		if (autoId == null) {
			throw new NullPointerException("Pin's id in database is invalid or null.");
		}

		return new PinUser(UserRepository.getCurrentUser().getUid(), null, autoId, null, point);
	}

	/**
	 * This method only works for non-admin users.
	 *
	 * @param name  Pin's name.
	 * @param point Pin's location.
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public PinUser createNewPin(String name, GeoPoint point) {
		return null;
	}

	/**
	 * This method only works for non-admin users.
	 *
	 * @param latitude  Pin's latitude coordinate.
	 * @param longitude Pin's longitude coordinate.
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public PinUser createNewPin(double latitude, double longitude) {
		return null;
	}

	/**
	 * This method only works for non-admin users.
	 *
	 * @param name      Pin's name.
	 * @param latitude  Pin's latitude coordinate.
	 * @param longitude Pin's longitude coordinate.
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public PinUser createNewPin(String name, double latitude, double longitude) {
		return null;
	}

	/**
	 * This method only works for non-admin users.
	 *
	 * @param name    Pin's name.
	 * @param point   Pin's location.
	 * @param comment Pin's comment.
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public PinUser createNewPin(String name, GeoPoint point, String comment) {
		return null;
	}

	/**
	 * This method only works for non-admin users.
	 *
	 * @param name      Pin's name.
	 * @param latitude  Pin's latitude coordinate.
	 * @param longitude Pin's longitude coordinate.
	 * @param comment   Pin's comment.
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public PinUser createNewPin(String name, double latitude, double longitude, String comment) {
		return null;
	}

}
