package dear.dearles.customclasses;

import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import dear.dearles.DearApp;


public class LocationAwareness implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Context context;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    public LocationAwareness (Context context) {
        this.context = context;
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            if (mGoogleApiClient != null) {
                createLocationRequest();
                mGoogleApiClient.connect();
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                // En caso de no haberse registrado todavia ninguna posición con el listener usamos la
                // LastKnownLocation del movil
                if (mLastLocation == null) {
                    // Mandarle aqui a que active los servicios de localizacion
                    System.out.println("Y AUN ASI mLastLocation sigue siendo NULL");
                    // Si aun asi sigue siendo null devuelvo (0,0)
                    mLastLocation = new Location("");
                    mLastLocation.setLatitude(0);
                    mLastLocation.setLongitude(0);
                }
            }
        }
    }


    // Method to verify google play services on the device
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                System.out.println("resultcode: " + resultCode);
                //GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                System.out.println("This device is not supported.");
                //Aqui iba un toast indicando que no se puede conectar
                //finish();
            }
            return false;
        }
        return true;
    }

    // Creating google api client object
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    // Devuelve la LastKnownLoc en caso de existir
    public Location GetLastKnownLoc () {
        return mLastLocation;
    }


    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        System.out.println("HA ARRANCADO EL STARTLOCATIONUPDATES");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        System.out.println("LocationChanged -> " + location.getLatitude() + " , " + location.getLongitude());
    }



    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        System.out.println("VOY POR EL onConnected");
        startLocationUpdates();
        // En caso de que en settings tenga activada la opcion de activar la posicion GPS
        // if (mRequestingLocationUpdates) { startLocationUpdates(); }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        System.out.println("VOY POR EL onConnectionSuspended");
        stopLocationUpdates();
        //mGoogleApiClient.connect();
    }

}