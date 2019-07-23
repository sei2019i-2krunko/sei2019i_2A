import * as functions from 'firebase-functions'
import admin = require('firebase-admin')
import { GeoPoint, DocumentReference, CollectionReference, DocumentSnapshot } from '@google-cloud/firestore';

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

		return docRef.delete().then((value) => {
			console.log('[Delete user document] Value returned: ' + value)
			return db.collection(u_doc + '/pins/').listDocuments().then((docs) => {
				const promises = []
				docs.forEach(doc => {
					console.log(`[Delete user document] Document to be deleted ${doc.id}`)
					promises.push(deleteDocumetPromisePath(doc, u_doc + '/pins/'))

				})
				return Promise.all(promises).then((_) => {
					console.log('[Delete user document] Successful deletion.')
				}).catch((error) => {
					console.error(`[Delete user document] error: ${error}`)
				})

			})
		}).catch(error => {
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
	const latitude: number = data.latitude || null
	const longitude: number = data.longitude || null

	// we verify if the user has passed these arguments (owner is not an argument)
	const name: string = data.name || null
	const comment: string = data.comment || null
	const owner: string = uid || null

	// admin purposes only
	const NEBoundLatitude: number = data.NEBoundLatitude || null
	const NEBoundLongitude: number = data.NEBoundLongitude || null
	const SWBoundLatitude: GeoPoint = data.SWBoundLatitude || null
	const SWBoundLongitude: GeoPoint = data.SWBoundLongitude || null
	const zoom: number = data.zoom || null

	// if it is a valid user
	if (context.auth) {

		console.log('[Save new pin] User is logged in')

		let user_info: DocumentReference | DocumentSnapshot = db.doc(`/users/${uid}`)

		console.log(`[Save new pin] uid ${uid}`)

		console.log(`[Save new pin] Getting a snapshot of document /users/${uid}`)

		// we try to get the user's documet
		return user_info.get().then((doc) => {

			console.log(`[Save new pin] Document snapshot has been obtained ${doc}`)

			let doc_info: any
			let point: any
			user_info = doc

			//we check if the user has a document
			if (user_info.data()) {

				console.log('[Save new pin] There is info at the document')
				const isAdmin: boolean = user_info.data().admin

				console.log('[Save new pin] Is the user admin?', isAdmin)

				let collection_ref: CollectionReference

				//we check if the user is not admin
				if (!isAdmin) {

					console.log('[Save new pin] It is just a normal user')
					//pins's user collection
					const collection_path = `/users/${uid}/pins/`
					collection_ref = db.collection(collection_path)

					// if a non-null point or latitude and longitude arguments were given
					console.log('[Save new pin] latitude received:', latitude)
					console.log('[Save new pin] longitude received:', longitude);

					if (latitude && longitude) {


						// if point was not given
						if (typeof latitude === 'number' && typeof longitude === 'number') {
							console.log('[Save new pin] Creating instance of GeoPoint. [138]')
							point = new admin.firestore.GeoPoint(latitude, longitude)
						}
						console.log('[Save new pin] Apparently point was set (we need an else [141])')

					} else {
						console.error('[Save new pin] Invalid arguments were given')
						console.error('[Save new pin] point:', point)
						console.error('[Save new pin] latitude:', latitude)
						console.error('[Save new pin] longitude:', longitude)

						throw new functions.https.HttpsError('invalid-argument', 'The function must be called with ' +
							'one arguments "point" containing the GeoPoint or arguments "latitude" and "longitude"')

					}

					console.log('[Save new pin] Setting document info')
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

					console.log('[Save new pin] Document info:', doc_info)

				}

				else {
					console.log('[Save new pin] It is an admin user')

					//pins's admin collection
					let NEBound: GeoPoint
					let SWBound: GeoPoint
					const collection_path = `/global-pins/`
					collection_ref = db.collection(collection_path)

					if (NEBoundLatitude && NEBoundLongitude && SWBoundLatitude && SWBoundLongitude && zoom) {
						if (latitude && longitude) {

							// if point was not given
							if (typeof latitude === 'number' && typeof longitude === 'number'
								&& typeof NEBoundLatitude === 'number' && typeof NEBoundLongitude === 'number'
								&& typeof SWBoundLongitude === 'number' && typeof SWBoundLatitude === 'number') {
								console.log('[Save new pin] Creating admin points.')
								point = new admin.firestore.GeoPoint(latitude, longitude)
								NEBound = new admin.firestore.GeoPoint(NEBoundLatitude, NEBoundLongitude)
								SWBound = new admin.firestore.GeoPoint(SWBoundLatitude, SWBoundLongitude)
							} else {
								console.error('[Save new pin] Error while creating points')
								throw new functions.https.HttpsError('invalid-argument', 'Coordinate must numbers.')
							}

							//if name was given we attach it
							if (name) {
								if (comment) {
									doc_info = { owner, name, comment, point, NEBound, SWBound, createdAt: admin.firestore.Timestamp.now(), zoom }
								} else {
									doc_info = { owner, name: name, point, NEBound, SWBound, createdAt: admin.firestore.Timestamp.now(), zoom }
								}
							} else {
								if (comment) {
									doc_info = { owner, comment: comment, NEBound, SWBound, createdAt: admin.firestore.Timestamp.now(), zoom }
								} else {
									doc_info = { owner, point, NEBound, SWBound, createdAt: admin.firestore.Timestamp.now(), zoom }
								}
							}
							console.log('[Save new pin] Document info:', doc_info)

						} else {
							console.error('[Save new pin] Invalid arguments were given')
							console.error('[Save new pin] point:', point)
							console.error('[Save new pin] latitude:', latitude)
							console.error('[Save new pin] longitude:', longitude)
							console.error('[Save new pin] zoom:', zoom)

							throw new functions.https.HttpsError('invalid-argument', 'The function must be called with ' +
								'one arguments "point" containing the GeoPoint or arguments "latitude" and "longitude"')

						}
					} else {
						console.error('[Save new pin] Argument(s) was/were not provided.')
						console.error('[Save new pin] Admin has not provided North-East boundary and/or South-West boundaries.')

						throw new functions.https.HttpsError('invalid-argument', 'Admin has to provide North-East boundary and South-West boundaries.')
					}

				}

				console.log('[Save new pin] Before creating the new document')

				// it creates a document with an auto-id
				return collection_ref.add(doc_info).then((value) => {
					console.log('[Save new pin] Document created in path:', value.path)
					console.log('[Save new pin] Document id:', value.id)

					const ret = { autoId: value.id, admin: isAdmin }

					console.log('[Save new pin] values to be returned:', ret)
					return ret
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
				console.error('[Save new pin] There was no info in the document')
				throw new functions.https.HttpsError('not-found', `User\'s document was not found with uid: ${uid}`)
			}

			//if an error ocurred while getting user's document
		}).catch((error) => {
			console.error(`[Save new pin] Error accessing to document /users/${uid}`)
			console.error(`[Save new pin] ${error}`)

			throw new functions.https.HttpsError(
				'unknown',
				`There was an error while getting document /users/${uid} ${error}`,
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

function deleteDocumetPromisePath(document: DocumentReference, path: string) {
	console.log(`[deleteDocumetPromisePath] Document id ${document.id}`)
	console.log(`[deleteDocumetPromisePath] Path ${path}${document.id}`)

	return db.collection(path).doc(document.id).delete()
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