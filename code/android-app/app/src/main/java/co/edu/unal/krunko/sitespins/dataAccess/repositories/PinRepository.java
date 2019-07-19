package co.edu.unal.krunko.sitespins.dataAccess.repositories;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.functions.FirebaseFunctions;

import co.edu.unal.krunko.sitespins.dataAccess.models.PinUser;

public class PinRepository {

	FirebaseFunctions functions;

	public PinRepository() {
		this.functions = FirebaseFunctions.getInstance();
	}

	/**
	 * This method only works for non-admin users.
	 *
	 * @param point Pin's location.
	 * @return A Pin instance with the parameters given with its id in Firebase.
	 */
	public PinUser createNewPin(GeoPoint point) {
		return null;
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
