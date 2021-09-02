package miyagi



import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.ContentType

class GoogleMapsService {
	def googleMapsApiUrl = 'https://maps.googleapis.com/maps/api/geocode/json'
	def GOOGLE_MAPS_API_KEY = 'AIzaSyCOiSlF5NaJj4UuMhb3hmncLWX3GwLN-tY'

	def geocode(address) {
		log.info "GEOCODE"

		def http = new HTTPBuilder(googleMapsApiUrl)
		http.request(Method.GET, ContentType.JSON) { req ->
			uri.query = [
				address: address,
				key:     GOOGLE_MAPS_API_KEY
			]
			response.success = { resp, data -> data }
			response.failure = { resp, data ->
				log.info "FAILED GET ${googleMapsApiUrl} ${body}"
				log.info data.toString()
			}
		}
	}
}
