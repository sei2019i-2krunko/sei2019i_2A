import * as functions from 'firebase-functions'
import admin = require('firebase-admin')
import { GeoPoint } from '@google-cloud/firestore';

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

	const uid = context.auth.uid



	// we verify if the user has passed as argument the point or latitute and longitude
	let point: GeoPoint = data.point || null

	const latitude: number = data.latitude || null
	const longitude: number = data.longitude || null

	const name: string = data.name || null

	// if it is a valid user
	if (uid) {

		//marker's user collection
		const collection_path = '/users/' + uid + '/markers/'
		const collection_ref = db.collection(collection_path)

		let doc_info: { name: string; position: GeoPoint; visited: boolean; } | { position: GeoPoint; visited: boolean; name?: undefined; } | { name: string; position: GeoPoint; visited: boolean; } | { position: GeoPoint; visited: boolean; name?: undefined; }
		// if a non-null point was given
		if (point) {
			if (point instanceof GeoPoint) {
				doc_info = name ? { name: name, position: point, visited: false } : { position: point, visited: false }

				// it creates a document with an auto-id
				return collection_ref.add(doc_info).catch(error => {

					console.error('[Save new map point] latitude:', point.latitude)
					console.error('[Save new map point] longitude:', point.longitude)

					console.error(
						'[Save new map point] There was a problem at trying to create the document',
						'\nWith error:', error
					)
				})
			}

		}
		// if a non-null latitude and longitude were given
		else if (latitude && longitude) {
			if (typeof latitude === 'number' && typeof longitude === 'number') {
				point = new GeoPoint(latitude, longitude)

				doc_info = name ? { name: name, position: point, visited: false } : { position: point, visited: false }

				// it creates a document with an auto-id
				return collection_ref.add(doc_info).then((value) => {
					console.log('[Save new map point] Document created', value.path)
					console.log('[Save new map point] Document id:', value.id)
				}).catch(error => {

					console.error('[Save new map point] latitude:', latitude)
					console.error('[Save new map point] longitude:', longitude)

					console.error(
						'[Save new map point] There was a problem at trying to create the document',
						'\nWith error:', error
					)
				})
			}
		}

		console.error('[Save new map point] Invalid arguments were given')
		console.error('[Save new map point] point:', point)
		console.error('[Save new map point] latitude:', latitude)
		console.error('[Save new map point] longitude:', longitude)
		return false
	}

	console.error('[Save new map point] User\'s id is not valid', uid)
	return false
})