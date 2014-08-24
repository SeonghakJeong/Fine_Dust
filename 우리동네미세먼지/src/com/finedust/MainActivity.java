package com.finedust;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LocationManager locationManager;
		
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		Criteria reqment = new Criteria();
		reqment.setAccuracy(Criteria.ACCURACY_FINE);
		reqment.setAltitudeRequired(false);
		reqment.setBearingRequired(false);
		reqment.setCostAllowed(true);
		reqment.setPowerRequirement(Criteria.POWER_LOW);
		
		String pvder = locationManager.getBestProvider(reqment, true);
		Location loc = locationManager.getLastKnownLocation(pvder);
		
		LocUpdate(loc);
		locationManager.requestLocationUpdates(pvder, 500, 50, locListner);
		
	}
	
	private final LocationListener locListner = new LocationListener(){
		public void onLocationChanged(Location location){
			LocUpdate(location);
		}
		public void onProviderDisabled(String provider){
			LocUpdate(null);
		}
		
		public void onProviderEnabled(String provider){}
		public void onStatusChanged(String provider, int status, Bundle extra){}
		
	};

	private void LocUpdate(Location loc){
		TextView LocTV = (TextView)findViewById(R.id.LocTextView);
		System.out.println("loctostring"+loc.toString());
		String str= null;
		String addrStr = null;
		double lat = loc.getLatitude();
		double lng = loc.getLongitude();
		
		str = "(Latitude: " + lat + ", longitude : "+lng +")";
		
		Geocoder gCoder = new Geocoder(this, Locale.getDefault());
		
		try {
			List<Address> addrList = gCoder.getFromLocation(lat, lng, 1);
			StringBuilder strBuilder = new StringBuilder();
			if(addrList.size()>0){
				Address addr = addrList.get(0);
				for(int i=0;i<addr.getMaxAddressLineIndex();i++){
					strBuilder.append(addr.getAddressLine(i)).append("\n");
					strBuilder.append(addr.getLocality()).append("\n");
					strBuilder.append(addr.getCountryName());
					addrStr = strBuilder.toString();
				}
			}else{
				addrStr = "Can't find Address"; 
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		LocTV.setText("location : "+ str + "\n\n Address : "+addrStr);
	}
}
