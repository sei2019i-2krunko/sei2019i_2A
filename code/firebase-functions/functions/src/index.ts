import * as functions from 'firebase-functions'
import admin = require('firebase-admin')
import { GeoPoint, DocumentReference, CollectionReference } from '@google-cloud/firestore';
import { DocumentSnapshot } from 'firebase-functions/lib/providers/firestore';

admin.initializeApp(functions.config().firebase)

const db = admin.firestore()

// this function creates a document when an user has been created
// this allow us to take control of the user in firestore
exports.create_user_document = functions.auth.user().onCreate((user, context) => {
	if (user.uid) {

		//user document
		const u_doc = '/users/' + user.uid

		console.log('[Create user document] Function will be affecting', user.uid)

		console.log('[Create user document] Accessing to:', u_doc)
		const docRef = db.doc(u_doc)

		console.log('[Create user document] Setting information up in the document:', u_doc)

		const default_data = {
			uid: user.uid,
			admin: false
		}

		return docRef.set(default_data).catch((error) => {
			console.error(
				'[Create user document] There was an error at trying to create the document',
				u_doc,
				'\nWith error:', error
			)
		})

	}

	console.error('[Create user document] Invalid user id', user.uid)
	return false
})

// this function deletes document asociated with the user when the user has been deleted
exports.delete_user_document = functions.auth.user().onDelete((user, context) => {
	if (user.uid) {

		//user document
		const u_doc = '/users/' + user.uid

		console.log('[Delete user document] Function will be affecting', user.uid)

		console.log('[Delete user document] Accessing to document', u_doc)
		const docRef = db.doc(u_doc)

		console.log('[Delete user document] Deleting document', u_doc)

		return docRef.delete().catch(error => {
			console.error(
				'[Delete user document] There was a problem at trying to eliminate the document',
				u_doc,
				'\nWith error:', error
			)
		})

	}
	console.error('[Delete user document] Invalid user id', user.uid)
	return false
})

// this function will save a geo point in firestore
exports.save_new_geo_point = functions.https.onCall((data, context) => {
	console.log('[Save new map point] Function has been called.')

	const uid = context.auth.uid || null

	// we verify if the user has passed as argument the point or latitute and longitude
	let point: GeoPoint = data.point || null

	const latitude: number = data.latitude || null
	const longitude: number = data.longitude || null
	const name: string = data.name || null
	const comment: string = data.comment || null

	// admin purposes only
	const NEBound: GeoPoint = data.NEBound || null
	const SWBound: GeoPoint = data.SWBound || null
	const owner: string = uid

	// if it is a valid user
	if (!context.auth) {

		let user_info: DocumentReference | DocumentSnapshot = db.doc(`/users/${uid}`)

		// we try to get the user's documet
		return user_info.get().then((doc) => {
			let doc_info: any
			user_info = doc

			//we check if the user has a document
			if (user_info.exists) {

				let collection_ref: CollectionReference

				//we check if the user is not admin
				if (!user_info.data().admin) {

					//pins's user collection
					const collection_path = `/users/${uid}/pins/`
					collection_ref = db.collection(collection_path)

					// if a non-null point or latitude and longitude arguments were given
					if (point || (latitude && longitude)) {

						// if point was not given
						if (!(point instanceof GeoPoint) && typeof latitude === 'number' && typeof longitude === 'number') {
							console.log('[Save new map point] point was not given but latitude and longitud were.')
							point = new GeoPoint(latitude, longitude)
						}

					} else {
						console.error('[Save new map point] Invalid arguments were given')
						console.error('[Save new map point] point:', point)
						console.error('[Save new map point] latitude:', latitude)
						console.error('[Save new map point] longitude:', longitude)

						throw new functions.https.HttpsError('invalid-argument', 'The function must be called with ' +
							'one arguments "point" containing the GeoPoint or arguments "latitude" and "longitude"')

					}

					//if name was given we attach it
					if (name) {
						if (comment) {
							doc_info = { name, comment, point, visited: false }
						} else {
							doc_info = { name, point, visited: false }
						}
					} else {
						if (comment) {
							doc_info = { comment, point, visited: false }
						} else {
							doc_info = { point, visited: false }
						}
					}

				}

				else {
					//pins's admin collection
					const collection_path = `/global-pins/`
					collection_ref = db.collection(collection_path)

					if (NEBound && SWBound && NEBound instanceof GeoPoint && SWBound instanceof GeoPoint) {
						if (point || (latitude && longitude)) {

							// if point was not given
							if (!(point instanceof GeoPoint) && typeof latitude === 'number' && typeof longitude === 'number') {
								console.log('[Save new map point] point was not given but latitude and longitud were.')
								point = new GeoPoint(latitude, longitude)
							}

							//if name was given we attach it
							if (name) {
								if (comment) {
									doc_info = { owner, name, comment, point, visited: false, NEBound, SWBound }
								} else {
									doc_info = { owner, name: name, point, visited: false, NEBound, SWBound }
								}
							} else {
								if (comment) {
									doc_info = { owner, comment: comment, point, visited: false, NEBound, SWBound }
								} else {
									doc_info = { owner, point, visited: false, NEBound, SWBound }
								}
							}

						} else {
							console.error('[Save new map point] Invalid arguments were given')
							console.error('[Save new map point] point:', point)
							console.error('[Save new map point] latitude:', latitude)
							console.error('[Save new map point] longitude:', longitude)

							throw new functions.https.HttpsError('invalid-argument', 'The function must be called with ' +
								'one arguments "point" containing the GeoPoint or arguments "latitude" and "longitude"')

						}
					} else {
						console.error('[Save new map point] Argument(s) was/were not provided.')
						console.error('[Save new map point] Admin has not provided North-East boundary and/or South-West boundaries.')

						throw new functions.https.HttpsError('invalid-argument', 'Admin has to provide North-East boundary and South-West boundaries.')
					}

				}

				// it creates a document with an auto-id
				return collection_ref.add(doc_info).then((value) => {
					console.log('[Save new map point] Document created in path:', value.path)
					console.log('[Save new map point] Document id:', value.id)

					return { autoId: value.id }
				}).catch((error) => {
					console.error('[Save new map point] latitude:', point.latitude)
					console.error('[Save new map point] longitude:', point.longitude)
					console.error('[Save new map point] There was a problem at trying to create the document')

					console.error(error)

					throw new functions.https.HttpsError('unknown', 'There was an error creating the document', error)
				})
			}

			//if user's document does not exist
			else {
				throw new functions.https.HttpsError('not-found', `User\'s document was not found with uid: ${uid}`)
			}

			//if an error ocurred while getting user's document
		}).catch((error) => {
			throw new functions.https.HttpsError(
				'unknown',
				`There was an error while getting document /users/${uid}`,
				error
			)
		})
	}

	console.error('[Save new map point] User\'s id is not valid', uid)
	throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
		'while authenticated.');

})