package simon.proyecto.desbarate;

import simon.proyecto.desbarate.detectores.ConnectionDetector;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class MapActivity extends android.support.v4.app.FragmentActivity
							implements OnMapClickListener {

	private CameraUpdate camara;
	private LatLng posicion;
	
	private ConnectionDetector connection;
	
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
		GoogleMap mapa = (
					(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)
				).getMap();
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
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO
	}

}
