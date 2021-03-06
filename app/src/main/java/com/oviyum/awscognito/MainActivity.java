package com.oviyum.awscognito;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSEnhancedCognitoIdentityProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentity;
import com.amazonaws.services.cognitoidentity.AmazonCognitoIdentityClient;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oviyum.fleetfoot.FleetMobileClient;
import com.oviyum.fleetfoot.model.LoginRequest;
import com.oviyum.fleetfoot.model.LoginResponse;
import com.oviyum.fleetfoot.model.MenuResponse;
import com.oviyum.fleetfoot.model.SetLocationRequest;
import com.oviyum.fleetfoot.model.SetLocationRequestLocationItem;
import com.oviyum.fleetfoot.model.SetLocationRequestOwnerItem;
import com.oviyum.fleetfoot.model.SetLocationResponse;
import com.amazonaws.services.cognitoidentity.*;
import com.amazonaws.services.cognitoidentity.model.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String IDENITITY_POOL_ID = "us-east-1:a5cf40c8-c0a1-40ab-bd23-5d456c3fbc2e";
    private final String AUTH_ROLE = "arn:aws:iam::777031169818:role/api_gateway";
    private final String UNAUTH_ROLE = "arn:aws:iam::777031169818:role/Cognito_FleetfootDevUnauth_Role";

    private FleetMobileClient fleetMobileClient;
    private ApiClientFactory apiClientFactory;
    private CognitoCredentialsProvider credentialsProvider;

    private FleetCredentialProvider fleetCredentialProvider;

    private DeveloperAuthenticationProvider developerAuthenticationProvider;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    private SetLocationRequest locationRequest;
    private SetLocationResponse locationResponse;

    private GetOpenIdTokenRequest tokenRequest;
   // GetOpenIdTokenForDeveloperIdentityRequest;

    ObjectMapper mapper = new ObjectMapper();

    private Button button;
    private TextView textview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init(){
        button = (Button)findViewById(R.id.location_btn);
        textview = (TextView)findViewById(R.id.loation_tv);
        button.setOnClickListener(this);

        //credentialsProvider = new CognitoCredentialsProvider(IDENITITY_POOL_ID, Regions.US_EAST_1);
        apiClientFactory = new ApiClientFactory();

        fleetMobileClient = apiClientFactory.build(FleetMobileClient.class);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@shary.com");
        loginRequest.setPassword("spravce9631");

        new LoginAsyncTask().execute();


    }

    private void prepareforlocationrequest(){
        locationRequest = getLocationRequest(0); // headoffice





        new LocationAsyncTask().execute();
    }

    public SetLocationRequest getLocationRequest(int locationID){
        SetLocationRequest setLocationRequest = new SetLocationRequest();

        List<SetLocationRequestOwnerItem> setLocationRequestOwnerItems = getSetLocationRequestOwnerItemList(loginResponse);
        List<SetLocationRequestLocationItem> setLocationRequestLocationItems = getSetLocationRequestLocationItemList(loginResponse,locationID);

        setLocationRequest.setOwner(setLocationRequestOwnerItems);
        setLocationRequest.setLocation(setLocationRequestLocationItems);


        return setLocationRequest;
    }




    private List<SetLocationRequestOwnerItem> getSetLocationRequestOwnerItemList(LoginResponse loginResponse){

        List<SetLocationRequestOwnerItem> setLocationRequestOwnerItems = new ArrayList<>();
        SetLocationRequestOwnerItem locationRequestOwnerItem = new SetLocationRequestOwnerItem();


        locationRequestOwnerItem.setCompanyName(loginResponse.getOwners().get(0).getCompanyName());
        locationRequestOwnerItem.setId(loginResponse.getOwners().get(0).getId());
        locationRequestOwnerItem.setFleetUserId(loginResponse.getOwners().get(0).getFleetUserId());

        setLocationRequestOwnerItems.add(locationRequestOwnerItem);


        return setLocationRequestOwnerItems;

    }

    private List<SetLocationRequestLocationItem> getSetLocationRequestLocationItemList(LoginResponse loginResponse, int locationID){
        List<SetLocationRequestLocationItem> setLocationRequestLocationItems = new ArrayList<>();
        SetLocationRequestLocationItem locationRequestLocationItem = new SetLocationRequestLocationItem();

        locationRequestLocationItem.setId(loginResponse.getLocations().get(locationID).getId());
        locationRequestLocationItem.setFleetLocationId(loginResponse.getLocations().get(locationID).getFleetLocationId());
        locationRequestLocationItem.setCaption(loginResponse.getLocations().get(locationID).getCaption());

        setLocationRequestLocationItems.add(locationRequestLocationItem);

        return setLocationRequestLocationItems;
    }

    @Override
    public void onClick(View view) {
        new LoginAsyncTask().execute();
    }


    private class LoginAsyncTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
           loginResponse = fleetMobileClient.processLoginPost(loginRequest);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.v("FLEETFOOT","******************************** LOGGED IN *************************************");
            textview.setText("Logged IN");
            prepareforlocationrequest();
            super.onPostExecute(s);
        }
    }

    private class MenuClassAsyncTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            MenuResponse response = fleetMobileClient.menuGet();
            return null;
        }
    }

    private class LocationAsyncTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {

                developerAuthenticationProvider = new DeveloperAuthenticationProvider(null,IDENITITY_POOL_ID,Regions.US_EAST_1);


                HashMap<String,String> logins = new HashMap<>();
                logins.put(developerAuthenticationProvider.getProviderName(),loginResponse.getToken());

                developerAuthenticationProvider.setCredentials(loginResponse.getIdentityId(),loginResponse.getToken());

                //developerAuthenticationProvider.refresh();
                // developerAuthenticationProvider.clearCredentials();
                developerAuthenticationProvider.setLogins(logins);

                credentialsProvider = new CognitoCredentialsProvider(developerAuthenticationProvider,Regions.US_EAST_1);
                credentialsProvider.refresh();



                apiClientFactory.credentialsProvider(credentialsProvider);
                fleetMobileClient = apiClientFactory.build(FleetMobileClient.class);

                locationResponse = fleetMobileClient.setSessionLocationPost(locationRequest);

            }catch (Exception e){

                e.printStackTrace();

            }
            return null;
        }

        @Override
        protected void onPreExecute() {





            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            textview.setText("");
            new MenuClassAsyncTask().execute();
            super.onPostExecute(s);
        }
    }




}
