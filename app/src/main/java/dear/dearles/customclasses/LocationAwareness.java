package dear.dearles.customclasses;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;


public class LocationAwareness implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
        , ResultCallback<LocationSettingsResult> {

    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FASTEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    Status status;

    public LocationAwareness (Context context) {
        this.context = context;
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            if (mGoogleApiClient != null) {
                createLocationRequest();
                buildLocationSettingsRequest();
                mGoogleApiClient.connect();
                checkLocationSettings();
            }
        }
    }



    // CHECKS --------------------------------------------------------------------------------------
    // Method to verify google play services on the device
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                System.out.println("resultcode: " + resultCode);
            } else {
                System.out.println("This device is not supported.");
            }
            return false;
        }
        return true;
    }

    public void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        System.out.println("VOY POR EL CHECKLOCATIONSETTINGS");
        result.setResultCallback(this);
    }


    // BUILDS --------------------------------------------------------------------------------------
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    // Creating google api client object
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    // GENERAL METHODS -----------------------------------------------------------------------------
    // Devuelve la LastKnownLoc
    public Location GetLastKnownLoc () {
        return mLastLocation;
    }

    // Devuelve el status de la setting
    public Status getLocationSettingsStatus () {
        return status;
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        System.out.println("HA ARRANCADO EL STARTLOCATIONUPDATES");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    // LOCATION REQUEST OBJECT ---------------------------------------------------------------------
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }


    // LISTENER ------------------------------------------------------------------------------------
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        System.out.println("LocationChanged -> " + location.getLatitude() + " , " + location.getLongitude());
    }



    // GOOGLE API CALLBACKS METHODS ----------------------------------------------------------------
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("Connection failed: ConnectionResult.getErrorCode() = " +
                result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        System.out.println("VOY POR EL onConnected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        System.out.println("VOY POR EL onConnectionSuspended");
        //stopLocationUpdates();
        //mGoogleApiClient.connect();
    }

    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {
        System.out.println("VOY POR EL onResult");
        status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                System.out.println("STATUS SUCESS");
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                System.out.println("STATUS RESOLUTION_REQUIRED");
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                System.out.println("SETTINGS_CHANGE_UNAVAILABLE");
                break;
        }
    }

}