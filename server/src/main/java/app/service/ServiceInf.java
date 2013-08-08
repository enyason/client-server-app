package app.service;

import app.socket.RequestObject;
import app.socket.ResponseObject;

public interface ServiceInf {
	/**
	 * addbird service.
	 * @param requestObject
	 * @return
	 */
	public ResponseObject addbird(RequestObject requestObject);
	
	/**
	 * addsighting service.
	 * @param requestObject
	 * @return
	 */
	public ResponseObject addsighting(RequestObject requestObject);

	/**
	 * remove service.
	 * @param requestObject
	 * @return
	 */
	public ResponseObject remove(RequestObject requestObject);

	/**
	 * listbirds service.
	 * @param requestObject
	 * @return
	 */
	public ResponseObject listbirds(RequestObject requestObject);
	
	/**
	 * listsightings service.
	 * @param requestObject
	 * @return
	 */
	public ResponseObject listsightings(RequestObject requestObject);


}
