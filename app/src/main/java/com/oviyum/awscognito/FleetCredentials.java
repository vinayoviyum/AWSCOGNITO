package com.oviyum.awscognito;

import com.amazonaws.auth.AWSCredentials;

/**
 * Created by vinaydanaraddi on 7/8/16.
 */
public class FleetCredentials implements AWSCredentials {
    String accessKey;
    String secretKey;

    public FleetCredentials(String accessKey,String secretKey){
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }


    @Override
    public String getAWSAccessKeyId() {
        return accessKey;
    }

    @Override
    public String getAWSSecretKey() {
        return secretKey;
    }
}
