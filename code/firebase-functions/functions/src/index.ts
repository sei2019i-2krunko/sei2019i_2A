import * as functions from 'firebase-functions'
import admin = require('firebase-admin')
import { GeoPoint, DocumentReference, CollectionReference } from '@google-cloud/firestore';
import { DocumentSnapshot } from 'firebase-functions/lib/providers/firestore';
import { isNullOrUndefined } from 'util';

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
exports.save_new_pin = functions.https.onCall((data, context) => {
	console.log('[Save new pin] Function has been called.')

	const uid = context.auth.uid || null

	// we verify if the user has passed as argument the point or latitute and longitude
	let point: GeoPoint = data.point || null
	const latitude: number = data.latitude || null
	const longitude: number = data.longitude || null

	// we verify if the user has passed these arguments (owner is not an argument)
	const name: string = data.name || null
	const comment: string = data.comment || null
	const owner: string = uid || null

	// admin purposes only
	const NEBound: GeoPoint = data.NEBound || null
	const SWBound: GeoPoint = data.SWBound || null

	// if it is a valid user
	if (!context.auth || isNullOrUndefined(owner)) {

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
							console.log('[Save new pin] point was not given but latitude and longitud were.')
							point = new GeoPoint(latitude, longitude)
						}

					} else {
						console.error('[Save new pin] Invalid arguments were given')
						console.error('[Save new pin] point:', point)
						console.error('[Save new pin] latitude:', latitude)
						console.error('[Save new pin] longitude:', longitude)

						throw new functions.https.HttpsError('invalid-argument', 'The function must be called with ' +
							'one arguments "point" containing the GeoPoint or arguments "latitude" and "longitude"')

					}

					//if name was given we attach it
					if (name) {
						if (comment) {
							doc_info = { owner, name, comment, point, visited: false }
						} else {
							doc_info = { owner, name, point, visited: false }
						}
					} else {
						if (comment) {
							doc_info = { owner, comment, point, visited: false }
						} else {
							doc_info = { owner, point, visited: false }
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
								console.log('[Save new pin] point was not given but latitude and longitud were.')
								point = new GeoPoint(latitude, longitude)
							}

							//if name was given we attach it
							if (name) {
								if (comment) {
									doc_info = { owner, name, comment, point, NEBound, SWBound }
								} else {
									doc_info = { owner, name: name, point, NEBound, SWBound }
								}
							} else {
								if (comment) {
									doc_info = { owner, comment: comment, NEBound, SWBound }
								} else {
									doc_info = { owner, point, NEBound, SWBound }
								}
							}

						} else {
							console.error('[Save new pin] Invalid arguments were given')
							console.error('[Save new pin] point:', point)
							console.error('[Save new pin] latitude:', latitude)
							console.error('[Save new pin] longitude:', longitude)

							throw new functions.https.HttpsError('invalid-argument', 'The function must be called with ' +
								'one arguments "point" containing the GeoPoint or arguments "latitude" and "longitude"')

						}
					} else {
						console.error('[Save new pin] Argument(s) was/were not provided.')
						console.error('[Save new pin] Admin has not provided North-East boundary and/or South-West boundaries.')

						throw new functions.https.HttpsError('invalid-argument', 'Admin has to provide North-East boundary and South-West boundaries.')
					}

				}

				// it creates a document with an auto-id
				return collection_ref.add(doc_info).then((value) => {
					console.log('[Save new pin] Document created in path:', value.path)
					console.log('[Save new pin] Document id:', value.id)

					return { autoId: value.id }
				}).catch((error) => {
					console.error('[Save new pin] latitude:', point.latitude)
					console.error('[Save new pin] longitude:', point.longitude)
					console.error('[Save new pin] There was a problem at trying to create the document')

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

	console.error('[Save new pin] User\'s id is not valid', uid)
	throw new functions.https.HttpsError('failed-precondition', 'The function must be called ' +
		'while authenticated.');

})

// this function returns a promise of document's deletion
function deleteDocumetPromise(document: DocumentReference) {
	console.log(`[deleteDocumetPromise] Document id ${document.id}`)
	console.log(`[deleteDocumetPromise] Path /global-pins/${document.id}`)

	return db.collection('/global-pins').doc(document.id).delete();
}

// this function deletes documents in global-pins when a new is created
exports.delete_global_pins = functions.firestore.document('/global-pins/{newPinID}')
	.onCreate(document => {
		console.log('[Delete global pins] Function has been triggered.')
		console.log(`[Delete global pins] New document /global-pins/${document.id}`)

		const promises = []

		console.log(`[Delete global pins] Before deleting all documents bu ${document.id}.`)

		return db.collection('/global-pins').listDocuments().then(docs => {
			docs.forEach(doc => {

				// we verify that the new document will not be deleted
				if (doc.id !== document.id) {
					console.log(`[Delete global pins] Document to be deleted ${doc.id}`)

					// this will add the document deletion promise to the list of promises.
					promises.push(deleteDocumetPromise(doc))
				}
			})
			return Promise.all(promises)
		}).then(value => {
			// successful deletion of documents
			console.log(`[Delete global pins] Succesful deletion.`)
			console.log(`[Delete global pins] Value obtained ${value.values.toString}.`)
			return true
		}).catch(error => {
			// error in deletion of documents
			console.log(`[Delete global pins] There was an error while deleting ${error}`)
			return false
		})

	})