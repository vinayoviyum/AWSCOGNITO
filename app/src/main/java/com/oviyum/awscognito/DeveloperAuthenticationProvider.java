package com.oviyum.awscognito;

import com.amazonaws.auth.AWSAbstractCognitoDeveloperIdentityProvider;
import com.amazonaws.regions.Regions;

/**
 * Created by vinaydanaraddi on 7/22/16.
 */
public class DeveloperAuthenticationProvider extends AWSAbstractCognitoDeveloperIdentityProvider {
    private static final String DEVLOPER_PROVIDER = "dev2.octanesofttech.com";



    public DeveloperAuthenticationProvider(String accountId, String identityPoolId, Regions region) {
        super(accountId, identityPoolId, region);
    }


    @Override
    public String getProviderName() {
        return DEVLOPER_PROVIDER;
    }


    public void setCredentials(String idenityid,String token) {
        setToken(token);
        setIdentityId(idenityid);
        update(idenityid,token);
    }

    public String refresh() {
        update(identityId, token);
        return token;

    }

    @Override
    public String getToken() {
        return super.getToken();
    }

    // If the app has a valid identityId return it, otherwise get a valid
    // identityId from your backend.


    @Override
    public String getIdentityId() {
        return super.getIdentityId();
    }
}