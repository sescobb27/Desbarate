package simon.proyecto.desbarate.detectores;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;

public class GoogleServicesDetector {
	
	private static GoogleServicesDetector services_detector;
	private static Activity activity;
	
//	CONSTANTS
	public static final int TRUE = 1;
//	END CONSTANTS
	
	private GoogleServicesDetector(Activity new_activity) {
		activity = new_activity;
	}
	
	public static GoogleServicesDetector getGoogleServicesDetector(Activity new_activity) {
		if (services_detector ==  null)
			services_detector = new GoogleServicesDetector(new_activity);
		else
			activity = new_activity;
		return services_detector;
	}
	
	public int isGoogleServicesEnable() {
		int result_code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
		if ( result_code == ConnectionResult.SUCCESS ) {
			return TRUE;
		}
		return result_code;
	}
}
