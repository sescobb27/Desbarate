package simon.proyecto.desbarate;

import java.util.List;

import simon.proyecto.desbarate.services.LocationConstants;
import simon.proyecto.desbarate.subscriptors.LocationSuscriptor;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class LocationController implements LocationListener {

	private LocationManager location_manager;
	private Criteria location_criteria;
	
	private LocationSuscriptor suscriptor;
	
	private NoLocationServiceEnable service_exception;
	
	private LatLng last_location;
	
//	CONSTANTS
	private final int _5_MIN = 500000;
//	END CONSTANTS
	
	/**
	 * @param context
	 * Description
	 * based on the context of the application we need to create a standard
	 * location controller to provide a very high location accuracy based on
	 * GPS and Network providers
	 */
	public LocationController(Context context) {
		location_manager = (LocationManager)
				context.getSystemService(Context.LOCATION_SERVICE);
		location_criteria = new Criteria();
		location_criteria.setAccuracy(Criteria.ACCURACY_HIGH);
		service_exception = new NoLocationServiceEnable();
	}
	
	/**
	 * @param enable_only
	 * @return String
	 * Description
	 * the name of the best location provider based on if the provider
	 * must be enable or not
	 */
	@SuppressWarnings("javadoc")
	public String getBestLocationByCriteria(boolean enable_only) {
		return location_manager.getBestProvider(location_criteria, enable_only);
	}
	
	/**
	 * @param enable_only
	 * @return List<String> names
	 * Description
	 * return a list of the best providers on the device, where must be enable
	 * or not, depends on the @param
	 */
	@SuppressWarnings("javadoc")
	public List<String> getAllBestProvidersByCriteria(boolean enable_only) {
		return location_manager.getProviders(location_criteria, enable_only);
	}
	
	/**
	 * @return List<String> all the provider available on the devise
	 */
	public List<String> getAllProviders() {
		return location_manager.getAllProviders();
	}
	
	/**
	 * @param suscriptor
	 * Description
	 * when a class implements LocationSuscriptor, should subscribe with this
	 * method to get notifications about location updates
	 */
	@SuppressWarnings("hiding")
	public void suscribeToOnLocationChange( LocationSuscriptor suscriptor ) {
		this.suscriptor = suscriptor;
	}
	
	/**
	 * @param provider
	 * @param time
	 * Description
	 * set the how much time per update the user will be have, and which provider
	 * will be use the location manager for handle the location updates
	 */
	private void setLocationUpdates(String provider, long time) {
		location_manager.requestLocationUpdates(
			provider, time, 0, this);
	}
	
	private boolean isGpsEnable() {
		return location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	private boolean isWifiLocationEnable() {
		return location_manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	
	/**
	 * @return LocationProvider GPS
	 */
	public LocationProvider getGpsProvider() {
		return location_manager.getProvider(LocationManager.GPS_PROVIDER);
	}
	
	/**
	 * @return LocationProvider Network
	 */
	public LocationProvider getNetworkProvider() {
		return location_manager.getProvider(LocationManager.NETWORK_PROVIDER);
	}
	
	/**
	 * @return Boolean GPS as Provider or Network else Throws
	 * @throws NoLocationServiceEnable
	 * first it tries to set GPS as provider because its accuracy, but if it isn't
	 * enable then tries with the Network provider, else if neither of both are
	 * enable it throws an exception
	 */
	public boolean setGpsOrNetworLocation() throws NoLocationServiceEnable {
		if ( setGpsAsProvider() || setNetworAsProvider()  )
			return true;
		throw service_exception;
	}
	
	/**
	 * @return Boolean True if it could set GPS as provider, else False
	 */
	public boolean setGpsAsProvider() {
		if (isGpsEnable()) {
			setLocationUpdates(LocationManager.GPS_PROVIDER,
					LocationConstants.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
			return true;
		}
		return false;
	}
	
	/**
	 * @return Boolean True if it could set Network as provider, else False
	 */
	public boolean setNetworAsProvider() {
		if (isWifiLocationEnable()) {
			setLocationUpdates(LocationManager.NETWORK_PROVIDER,
					LocationConstants.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
			return true;
		}
		return false;
	}
	
	/**
	 * @param provider
	 */
	public void updateUpdateRate(String provider) {
		setLocationUpdates(provider, _5_MIN);
	}
	
	/**
	 * 
	 */
	public void removeUpdates() {
		location_manager.removeUpdates(this);
	}

	@Override
	public void onLocationChanged(Location location) {
		LatLng new_location = new LatLng(location.getLatitude(), location.getLongitude());
		if (last_location ==  null) {
			last_location = new_location;
		}
		suscriptor.OnLocationChange(new_location);
	}

	@Override
	public void onProviderDisabled(String provider) {
		if (!isWifiLocationEnable() && !isGpsEnable())
			suscriptor.OnGpsAndNetworkDisable();
	}

	@Override
	public void onProviderEnabled(String provider) {
		if (provider.equals(LocationManager.GPS_PROVIDER))
			suscriptor.OnGpsProviderEnable();
		else if (isWifiLocationEnable())
			suscriptor.OnNetworkProviderEnable();	
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO
	}
}
