package simon.proyecto.desbarate.services;

import simon.proyecto.desbarate.detectores.GoogleServicesDetector;
import simon.proyecto.desbarate.dialogs.ErrorDialogFragment;
import android.app.Dialog;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class GoogleLocationService extends FragmentActivity implements LocationListener,
											GooglePlayServicesClient.ConnectionCallbacks,
											GooglePlayServicesClient.OnConnectionFailedListener {
	private static LocationRequest location_request;
	private static LocationClient location_client;
	private GoogleServicesDetector services_detector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		location_request = LocationRequest.create();
		location_request.setInterval(LocationConstants.UPDATE_INTERVAL_IN_MILLISECONDS);
		location_request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		location_request.setFastestInterval(LocationConstants.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		location_client = new LocationClient(getApplicationContext(), this, this);
	}
	
	private void stopUpdates() {
		if ( location_client.isConnected() ) {
			location_client.removeLocationUpdates(this);
		}
		location_client.disconnect();
	}

	private void startUpdates() {
		services_detector = GoogleServicesDetector.getGoogleServicesDetector(this);
		int result_code = services_detector.isGoogleServicesEnable();
		if ( result_code == GoogleServicesDetector.TRUE )
			location_client.requestLocationUpdates( location_request, this );
		else {
			showErrorDialog( result_code, 0);
		}
	}
	
	@Override
	public void onLocationChanged(Location location) {
		// TODO
	}
	
	private void showErrorDialog(int error_code, int request_code) {
        Dialog error_dialog = GooglePlayServicesUtil.getErrorDialog( error_code, this, request_code );
        // If Google Play services can provide an error dialog
        if (error_dialog != null) {
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(error_dialog);
            errorFragment.show(getSupportFragmentManager(), "Desbarate");
        }
    }

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if ( result.hasResolution() ) {
			try {
				result.startResolutionForResult(this, LocationConstants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			showErrorDialog(result.getErrorCode(), LocationConstants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		startUpdates();
	}

	@Override
	public void onDisconnected() {
		stopUpdates();
	}

	public void disconnect() {
		location_client.removeLocationUpdates(this);
		// After disconnect() is called, the client is considered "dead".
		location_client.disconnect();
	}
}
