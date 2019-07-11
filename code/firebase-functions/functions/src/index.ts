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
		const docRef = db.collection('users').doc(user.uid)

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
		const docRef = db.collection('users').doc(user.uid)

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
	const point = data.point || null

	const latitude = data.latitude || null
	const longitude = data.longitude || null

	// if it is a valid user
	if (uid) {
		//user document
		const u_doc = '/users/' + uid

		if (point) {
			if (point instanceof GeoPoint) {
				//do something
			}
		} else if (latitude && longitude) {
			if (latitude instanceof Number && longitude instanceof Number) {
				//do something
			}
		}
		console.error('[Save new map point] Invalid arguments were given')
		console.error('[Save new map point] point:', point)
		console.error('[Save new map point] latitude:', latitude)
		console.error('[Save new map point] longitude:', longitude)
		return false
	}

	return false
})