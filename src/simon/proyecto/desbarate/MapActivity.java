package simon.proyecto.desbarate;

import simon.proyecto.desbarate.detectores.ConnectionDetector;
import simon.proyecto.desbarate.detectores.GoogleServicesDetector;
import simon.proyecto.desbarate.services.GoogleLocationService;
import simon.proyecto.desbarate.services.notifier.NotifierReceiver;
import simon.proyecto.desbarate.subscriptors.NotifierSubscriptor;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * @author simon
 *
 */
public class MapActivity extends FragmentActivity
							implements OnMapClickListener, OnMarkerClickListener,
							NotifierSubscriptor {

	private CameraUpdate camara;
	private LatLng posicion;
	private GoogleMap mapa;
	private MarkerOptions marker;
	
	private ConnectionDetector connection;
	private GoogleServicesDetector service_detector;
	private boolean manual_location = false;
	
//	CONSTANTES
	private final double LATITUD_INICIAL = 6.235357;
	private final double LONGITUD_INICIAL = -75.575480;
	private final int ZOOM = 14;
//	FIN CONSTANTES
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_layout);
		
//		Accederemos al mapa llamando al método getMap() del fragmento MapFragment via getSupportFragment
//		que contenga nuestro mapa
		mapa = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		posicion = new LatLng(LATITUD_INICIAL, LONGITUD_INICIAL);
		camara = CameraUpdateFactory.newLatLngZoom(posicion, ZOOM);
		mapa.moveCamera(camara);
		
		connection = ConnectionDetector.getConnectionDetector(getApplicationContext());
		if (!connection.isConnectedToInternet()) {
			Toast.makeText(
				getApplicationContext(),
				"Active los datos o conectate al wifi mas cercano",
				Toast.LENGTH_LONG
			).show();
		} else {
			detectGoogleServices();
		}
		mapa.setOnMapClickListener( this );
		mapa.setOnMarkerClickListener( this );
		marker = new MarkerOptions();
	}

	/**
	 * @description
	 * detects if the smart phone has support for google play services,
	 * if yes, then this class subscribes it self to receive notifications based
	 * on Google play services location library, else @TODO subscribe to
	 * receive notifications based on GPS, Network or any hardware device
	 * with location based support
	 */
	private void detectGoogleServices() {
		service_detector = GoogleServicesDetector.getGoogleServicesDetector(this);
		int result_code = service_detector.isGoogleServicesEnable();
		if (result_code == GoogleServicesDetector.TRUE) {
			Intent intent = new Intent(this,GoogleLocationService.class);
			NotifierReceiver.subscribeToNotifications(this);
			startService(intent);
		} else {
			//@TODO
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	@Override
	/**
	 * @param int featureId
	 * @param MenuItem item
	 * @return boolean true if manual_location was clicked
	 * 
	 * @description
	 * if manual_location was clicked the user have to provide his location
	 * manually, else the device must have to update the user location
	 * automatically 
	 */
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch( item.getItemId() ){
		case R.id.manual_location:
			if (manual_location) {
				manual_location = false;
				item.setChecked(false);
			} else {
				manual_location = true;
				item.setChecked(true);
			}
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	/**
	 * @param LatLng point
	 * When user clicks the map, and the boolean flag manual_location is true,
	 * (the user checks in the menu for set the location manually) this method
	 * catch the point where the user clicks and puts a marker there
	 */
	public void onMapClick(LatLng point) {
		if( manual_location ) {
			mapa.clear();
			marker.position(point).title("Help/Ayuda");
			mapa.addMarker(marker);
		}
	}

	@Override
	/**
	 * @param Marker marker
	 * @TODO if the user clicks the marker, a pop up window should be shown
	 * and prompt the user to delete the marker
	 */
	public boolean onMarkerClick(Marker marker) {
		return true;
	}

	@Override
	/**
	 * @param Location location
	 * @description
	 * Method implemented to receive location updates from Google play services
	 * but it should work when receiving notifications from location based on
	 * hardware (GPS, Network, etc)
	 */
	public void receiveLocationUpdate(Location location) {
		mapa.clear();
		marker.position(new LatLng(location.getLatitude(), location.getLongitude()));
		mapa.addMarker(marker);
	}
	

}
