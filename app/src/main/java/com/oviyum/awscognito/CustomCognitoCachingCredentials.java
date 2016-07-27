package com.oviyum.awscognito;

import android.content.Context;

import com.amazonaws.auth.AWSCognitoIdentityProvider;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.auth.CognitoCredentialsProvider;
import com.amazonaws.auth.SessionCredentialsProviderFactory;
import com.amazonaws.regions.Regions;

/**
 * Created by vinaydanaraddi on 7/26/16.
 */
public class CustomCognitoCachingCredentials extends CognitoCredentialsProvider {
    private AWSSessionCredentials sessionCredentials;

    public void setSessionCredentials(BasicSessionCredentials sessionCredentials) {
        this.sessionCredentials = sessionCredentials;
    }

    public CustomCognitoCachingCredentials(String identityPoolId, Regions region) {
        super(identityPoolId, region);
    }

    public CustomCognitoCachingCredentials(AWSCognitoIdentityProvider provider, String unauthArn, String authArn) {
        super(provider, unauthArn, authArn);
    }

    @Override
    protected void setIdentityId(String identityId) {
        super.setIdentityId(identityId);
    }



    @Override
    public AWSSessionCredentials getCredentials() {
        return this.sessionCredentials;
    }

    @Override
    public String getToken() {
        return super.getToken();
    }
}
