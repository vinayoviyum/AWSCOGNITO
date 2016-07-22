package com.oviyum.awscognito;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;

/**
 * Created by vinaydanaraddi on 7/8/16.
 */
public  class FleetCredentialProvider extends BasicSessionCredentials implements AWSCredentialsProvider{

    FleetCredentials fleetCredentials;

    public FleetCredentialProvider(String awsAccessKey, String awsSecretKey, String sessionToken) {
        super(awsAccessKey, awsSecretKey, sessionToken);

        fleetCredentials = new FleetCredentials(awsAccessKey,awsSecretKey);

    }

    @Override
    public AWSCredentials getCredentials() {
        return fleetCredentials;
    }

    @Override
    public void refresh() {

    }


}
