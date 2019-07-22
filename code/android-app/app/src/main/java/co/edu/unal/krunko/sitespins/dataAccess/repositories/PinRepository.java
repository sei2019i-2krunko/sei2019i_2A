package co.edu.unal.krunko.sitespins.dataAccess.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import co.edu.unal.krunko.sitespins.dataAccess.models.Pin;
import co.edu.unal.krunko.sitespins.dataAccess.models.PinAdmin;
import co.edu.unal.krunko.sitespins.dataAccess.models.PinUser;

import static com.google.android.gms.tasks.Tasks.await;

public class PinRepository {

	private FirebaseFunctions functions;
	private FirebaseFirestore firestore;
	private String uid;

	public PinRepository() throws NullPointerException {
		this.functions = FirebaseFunctions.getInstance();
		this.firestore = FirebaseFirestore.getInstance();


		if (UserRepository.getCurrentUser() != null) {
			this.uid = UserRepository.getCurrentUser().getUid();
		}

		if (this.uid == null) {
			throw new NullPointerException("User cannot be null");
		}
	}

	/**
	 * This method  works for any type of user.
	 * It calls the cloud function save_new_pin which returns the document id of the Pin created in Firebase.
	 *
	 * @param point   Pin's location.
	 * @param NEBound Pin's North-East boundary (this only applies for admin users).
	 * @param SWBound Pin's South-West boundary (this only applies for admin users).
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public Pin createNewPin(GeoPoint point, GeoPoint NEBound, GeoPoint SWBound) throws ExecutionException, InterruptedException, NullPointerException {
		HashMap<String, Object> parameters = new HashMap<>();

		String autoId;
		Boolean admin;

		parameters.put("point", point);
		parameters.put("NEBound", NEBound);
		parameters.put("SWBound", SWBound);

		Map<String, Object> result = (Map<String, Object>) await(
				this.functions.getHttpsCallable("save_new_pin")
						.call(parameters)
						.continueWith(new Continuation<HttpsCallableResult, Object>() {
							@Override
							public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
								HttpsCallableResult result = task.getResult();

								if (result != null) {
									return result.getData();
								}
								return null;
							}
						})
		);

		if (result == null) {
			throw new NullPointerException("Function has returned none data");
		}

		autoId = (String) result.get("autoId");
		admin = (Boolean) result.get("admin");

		if (autoId == null) {
			throw new NullPointerException("Pin's id in database is invalid or null.");
		} else if (admin == null) {
			throw new NullPointerException("It cannot be determined if user is an admin.");
		}

		if (admin) {
			return new PinAdmin(this.uid, null, autoId, null, point, NEBound, SWBound);
		}

		return new PinUser(this.uid, null, autoId, null, point);
	}

	/**
	 * This method  works for any type of user.
	 * It calls the cloud function save_new_pin which returns the document id of the Pin created in Firebase.
	 *
	 * @param name    Pin's name.
	 * @param point   Pin's location.
	 * @param NEBound Pin's North-East boundary (this only applies for admin users).
	 * @param SWBound Pin's South-West boundary (this only applies for admin users).
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public Pin createNewPin(String name, GeoPoint point, GeoPoint NEBound, GeoPoint SWBound) throws ExecutionException, InterruptedException, NullPointerException {
		HashMap<String, Object> parameters = new HashMap<>();

		String autoId;
		Boolean admin;

		parameters.put("name", name);
		parameters.put("point", point);
		parameters.put("NEBound", NEBound);
		parameters.put("SWBound", SWBound);

		Map<String, Object> result = (Map<String, Object>) await(
				this.functions.getHttpsCallable("save_new_pin")
						.call(parameters)
						.continueWith(new Continuation<HttpsCallableResult, Object>() {
							@Override
							public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
								HttpsCallableResult result = task.getResult();

								if (result != null) {
									return result.getData();
								}
								return null;
							}
						})
		);

		if (result == null) {
			throw new NullPointerException("Function has returned none data");
		}

		autoId = (String) result.get("autoId");
		admin = (Boolean) result.get("admin");

		if (autoId == null) {
			throw new NullPointerException("Pin's id in database is invalid or null.");
		} else if (admin == null) {
			throw new NullPointerException("It cannot be determined if user is an admin.");
		}

		if (admin) {
			return new PinAdmin(this.uid, name, autoId, null, point, NEBound, SWBound);
		}

		return new PinUser(this.uid, name, autoId, null, point);
	}

	/**
	 * This method  works for any type of user.
	 * It calls the cloud function save_new_pin which returns the document id of the Pin created in Firebase.
	 *
	 * @param latitude  Pin's latitude coordinate.
	 * @param longitude Pin's longitude coordinate.
	 * @param NEBound   Pin's North-East boundary (this only applies for admin users).
	 * @param SWBound   Pin's South-West boundary (this only applies for admin users).
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public Pin createNewPin(double latitude, double longitude, GeoPoint NEBound, GeoPoint SWBound) throws ExecutionException, InterruptedException, NullPointerException {
		HashMap<String, Object> parameters = new HashMap<>();

		String autoId;
		Boolean admin;

		parameters.put("NEBound", NEBound);
		parameters.put("SWBound", SWBound);
		parameters.put("latitude", latitude);
		parameters.put("longitude", longitude);

		Map<String, Object> result = (Map<String, Object>) await(
				this.functions.getHttpsCallable("save_new_pin")
						.call(parameters)
						.continueWith(new Continuation<HttpsCallableResult, Object>() {
							@Override
							public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
								HttpsCallableResult result = task.getResult();

								if (result != null) {
									return result.getData();
								}
								return null;
							}
						})
		);

		if (result == null) {
			throw new NullPointerException("Function has returned none data");
		}

		autoId = (String) result.get("autoId");
		admin = (Boolean) result.get("admin");

		if (autoId == null) {
			throw new NullPointerException("Pin's id in database is invalid or null.");
		} else if (admin == null) {
			throw new NullPointerException("It cannot be determined if user is an admin.");
		}

		if (admin) {
			return new PinAdmin(this.uid, null, autoId, null, null, NEBound, SWBound);
		}


		return new PinUser(this.uid, null, autoId, null, new GeoPoint(latitude, longitude));
	}

	/**
	 * This method  works for any type of user.
	 * It calls the cloud function save_new_pin which returns the document id of the Pin created in Firebase.
	 *
	 * @param name      Pin's name.
	 * @param latitude  Pin's latitude coordinate.
	 * @param longitude Pin's longitude coordinate.
	 * @param NEBound   Pin's North-East boundary (this only applies for admin users).
	 * @param SWBound   Pin's South-West boundary (this only applies for admin users).
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public Pin createNewPin(String name, double latitude, double longitude, GeoPoint NEBound, GeoPoint SWBound) throws ExecutionException, InterruptedException, NullPointerException {
		HashMap<String, Object> parameters = new HashMap<>();

		String autoId;
		Boolean admin;

		parameters.put("name", name);
		parameters.put("NEBound", NEBound);
		parameters.put("SWBound", SWBound);
		parameters.put("latitude", latitude);
		parameters.put("longitude", longitude);

		Map<String, Object> result = (Map<String, Object>) await(
				this.functions.getHttpsCallable("save_new_pin")
						.call(parameters)
						.continueWith(new Continuation<HttpsCallableResult, Object>() {
							@Override
							public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
								HttpsCallableResult result = task.getResult();

								if (result != null) {
									return result.getData();
								}
								return null;
							}
						})
		);

		if (result == null) {
			throw new NullPointerException("Function has returned none data");
		}

		autoId = (String) result.get("autoId");
		admin = (Boolean) result.get("admin");

		if (autoId == null) {
			throw new NullPointerException("Pin's id in database is invalid or null.");
		} else if (admin == null) {
			throw new NullPointerException("It cannot be determined if user is an admin.");
		}

		if (admin) {
			return new PinAdmin(this.uid, name, autoId, null, new GeoPoint(latitude, longitude), NEBound, SWBound);
		}


		return new PinUser(this.uid, name, autoId, null, new GeoPoint(latitude, longitude));
	}

	/**
	 * This method  works for any type of user.
	 * It calls the cloud function save_new_pin which returns the document id of the Pin created in Firebase.
	 *
	 * @param name    Pin's name.
	 * @param point   Pin's location.
	 * @param comment Pin's comment.
	 * @param NEBound Pin's North-East boundary (this only applies for admin users).
	 * @param SWBound Pin's South-West boundary (this only applies for admin users).
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public Pin createNewPin(String name, GeoPoint point, String comment, GeoPoint NEBound, GeoPoint SWBound) throws ExecutionException, InterruptedException, NullPointerException {
		Map<String, Object> parameters = new HashMap<>();

		String autoId;
		Boolean admin;

		parameters.put("name", name);
		parameters.put("latitude", point.getLatitude());
		parameters.put("longitude", point.getLongitude());
		parameters.put("comment", comment);
		parameters.put("NEBoundLatitude", NEBound.getLatitude());
		parameters.put("NEBoundLongitude", NEBound.getLongitude());
		parameters.put("SWBoundLatitude", SWBound.getLatitude());
		parameters.put("SWBoundLongitude", SWBound.getLongitude());


		Log.w("save_new_pin", "Before :v");
		Map<String, Object> result = (Map<String, Object>) await(
				this.functions.getHttpsCallable("save_new_pin")
						.call(parameters)
						.continueWith(new Continuation<HttpsCallableResult, Object>() {
							@Override
							public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
								HttpsCallableResult result = task.getResult();
								if (result != null) {
									Log.d("save_new_pin", "There are data");
									return result.getData();
								}
								Log.wtf("save_new_pin", "there is no data");
								return null;
							}
						})
		);

		if (result == null) {
			throw new NullPointerException("Function has returned none data");
		}

		autoId = (String) result.get("autoId");
		admin = (Boolean) result.get("admin");

		if (autoId == null) {
			throw new NullPointerException("Pin's id in database is invalid or null.");
		} else if (admin == null) {
			throw new NullPointerException("It cannot be determined if user is an admin.");
		}

		if (admin) {
			return new PinAdmin(this.uid, name, autoId, comment, point, NEBound, SWBound);
		}


		return new PinUser(this.uid, name, autoId, comment, point);
	}

	/**
	 * This method works for any type of user.
	 * It calls the cloud function save_new_pin which returns the document id of the Pin created in Firebase.
	 *
	 * @param name      Pin's name.
	 * @param latitude  Pin's latitude coordinate.
	 * @param longitude Pin's longitude coordinate.
	 * @param comment   Pin's comment.
	 * @param NEBound   Pin's North-East boundary (this only applies for admin users).
	 * @param SWBound   Pin's South-West boundary (this only applies for admin users).
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public Pin createNewPin(String name, double latitude, double longitude, String comment, GeoPoint NEBound, GeoPoint SWBound) throws ExecutionException, InterruptedException, NullPointerException {
		HashMap<String, Object> parameters = new HashMap<>();

		String autoId;
		Boolean admin;

		parameters.put("name", name);
		parameters.put("comment", comment);
		parameters.put("NEBound", NEBound);
		parameters.put("SWBound", SWBound);
		parameters.put("latitude", latitude);
		parameters.put("longitude", longitude);

		Map<String, Object> result = (Map<String, Object>) await(
				this.functions.getHttpsCallable("save_new_pin")
						.call(parameters)
						.continueWith(new Continuation<HttpsCallableResult, Object>() {
							@Override
							public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
								HttpsCallableResult result = task.getResult();

								if (result != null) {
									return result.getData();
								}
								return null;
							}
						})
		);

		if (result == null) {
			throw new NullPointerException("Function has returned none data");
		}

		autoId = (String) result.get("autoId");
		admin = (Boolean) result.get("admin");

		if (autoId == null) {
			throw new NullPointerException("Pin's id in database is invalid or null.");
		} else if (admin == null) {
			throw new NullPointerException("It cannot be determined if user is an admin.");
		}

		if (admin) {
			return new PinAdmin(this.uid, name, autoId, null, new GeoPoint(latitude, longitude), NEBound, SWBound);
		}

		return new PinUser(this.uid, name, autoId, comment, new GeoPoint(latitude, longitude));
	}


	/**
	 * This method will get every user's pins (from their collection in firebase).
	 *
	 * @return User's pins
	 * @throws ExecutionException   If happens any runtime error (such as network or anything else).
	 * @throws InterruptedException If the task is interrupted.
	 */
	@SuppressWarnings("JavadocReference")
	public List<PinUser> getPins() throws ExecutionException, InterruptedException {
		CollectionReference users_pins = this.firestore.collection("/users/" + this.uid + "/pins");

		final List<PinUser> userPins = new ArrayList<>();

		await(users_pins.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				if (task.isSuccessful()) {
					for (QueryDocumentSnapshot doc :
							Objects.requireNonNull(task.getResult())) {
						String id = doc.getId();
						String name = doc.getString("name");
						String owner = doc.getString("owner");
						String comment = doc.getString("comment");
						GeoPoint point = doc.getGeoPoint("point");
						boolean visited = doc.getBoolean("visited");

						userPins.add(new PinUser(owner, name, id, comment, point, visited));

						Log.d("PinRepository", "Pin's id: " + id);
						Log.d("PinRepository", "Pin's name: " + name);
						Log.d("PinRepository", "Pin's owner: " + owner);
						Log.d("PinRepository", "Pin's comment: " + comment);
						Log.d("PinRepository", "Pin's visited: " + visited);
						Log.d("PinRepository", "Pin's point: " + point.toString());
						Log.d("PinRepository", "---------------------------");
					}

				} else {
					Log.e("PinRepository", "Error getting documents in collection /users/" + uid + "/pins/");
				}
			}
		}));
		return userPins;
	}

	public PinAdmin getGlobalPin() throws ExecutionException, InterruptedException {
		CollectionReference global_pins = this.firestore.collection("/global-pins/");

		await(global_pins.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				// TODO: 22/07/19 get a time stamp in case that there are more than one global pin)
				//  get the most recent one
			}
		}));

		return null;
	}

	public static GeoPoint toGeoPoint(LatLng point) {
		return new GeoPoint(point.latitude, point.longitude);
	}

	public static LatLng toLatLong(GeoPoint point) {
		return new LatLng(point.getLatitude(), point.getLongitude());
	}

}
