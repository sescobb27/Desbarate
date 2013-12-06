package simon.proyecto.desbarate.subscriptors;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author simon
 *
 */
public interface LocationSuscriptor {

	public void OnLocationChange(LatLng location);
	public void OnGpsProviderEnable();
	public void OnNetworkProviderEnable();
	public void OnGpsAndNetworkDisable();
}
