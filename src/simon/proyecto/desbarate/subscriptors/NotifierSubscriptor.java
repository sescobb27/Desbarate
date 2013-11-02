package simon.proyecto.desbarate.subscriptors;

import android.location.Location;

public interface NotifierSubscriptor {

	public void receiveLocationUpdate(Location location);
}
