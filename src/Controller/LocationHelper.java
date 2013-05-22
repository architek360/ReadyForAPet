package Controller;

import com.google.android.maps.GeoPoint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Location Helper Class that handles creation of the Location Manager and Location Listener.
 * 
 */
public class LocationHelper{

	private double distance = 0;
	GeoPoint geoPointA;
	GeoPoint geoPointB;

	//location manager and listener
	private LocationManager locationManager;
	private MyLocationListener locationListener;

	/**
	 * Constructor for LocationHelper
	 * 
	 * @param context - The context of the calling activity.
	 */
	public LocationHelper(Context context){

		//setup the location manager
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

		//create the location listener
		locationListener = new MyLocationListener();

		//setup a callback for when the GPS gets a lock and we receive data
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 10, locationListener);

	}

	/**
	 * Receiving notifications from the Location Manager when they are sent.
	 *
	 */
	public class MyLocationListener implements LocationListener {


		/**
		 * called when the location service reports a change in location
		 */
		public void onLocationChanged(Location location) {

			if (geoPointB == null){
				geoPointB = new GeoPoint(degreesToMicrodegrees(location.getLatitude()), degreesToMicrodegrees(location.getLongitude()));
			}

			//Getting the current GeoPoint
			geoPointA = new GeoPoint(degreesToMicrodegrees(location.getLatitude()), degreesToMicrodegrees(location.getLongitude()));

			//Calculating the distance in meters
			distance = distance + nu.placebo.whatsup.util.Geodetics.distance(geoPointA, geoPointB);

			//Making current GeoPoint the previous GeoPoint
			geoPointB = geoPointA;

		}

		//called when the provider is disabled
		public void onProviderDisabled(String provider) {}
		//called when the provider is enabled
		public void onProviderEnabled(String provider) {}
		//called when the provider changes state
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	}
	
	/**
	 * Convert a float point degree value into a microderee value
	 * 
	 * @param deg
	 * @return float point in microdegree value
	 */
	public static int degreesToMicrodegrees(double deg){
		return (int)(deg * 1E6);
	}


	/**
	 * Stop updates from the Location Service.
	 */
	public void killLocationServices(){
		locationManager.removeUpdates(locationListener);
	}

	/**
	 * Get Distance 
	 *
	 * @return - The current distance walked.
	 */
	public double getDistance(){
		return distance;
	}

	/**
	 * Check if a location has been found yet.
	 * @return - True if a location has been acquired. False otherwise.
	 */
	public Boolean gpsEnabled(){
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}	
}