package simon.proyecto.desbarate;

import com.google.android.gms.maps.model.LatLng;

public interface LocationSuscriptor {

	public void OnLocationChange(LatLng location);
	public void OnGpsProviderEnable();
	public void OnNetworkProviderEnable();
	public void OnGpsAndNetworkDisable();
}
