package com.oviyum.awscognito;

import com.amazonaws.auth.AWSAbstractCognitoDeveloperIdentityProvider;
import com.amazonaws.regions.Regions;

/**
 * Created by vinaydanaraddi on 7/22/16.
 */
public class DeveloperAuthenticationProvider extends AWSAbstractCognitoDeveloperIdentityProvider {
    private static final String DEVLOPER_PROVIDER = "dev2.octanesofttech.com";

    private String idenityID = "";

    private String token = "";

    public DeveloperAuthenticationProvider(String accountId, String identityPoolId, Regions region) {
        super(accountId, identityPoolId, region);
    }

    @Override
    public String getProviderName() {
        return null;
    }


    public void setIdenityid(String idenityid) {
        this.identityId = idenityid;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String refresh() {

        setToken(null);

        // If the logins map is not empty make a call to your backend
        // to get the token and identityId
        if (getProviderName() != null &&
                !this.loginsMap.isEmpty() &&
                this.loginsMap.containsKey(getProviderName())) {

            /**
             * This is where you would call your backend
             **/

            // now set the returned identity id and token in the provider
            update(identityId, token);
            return token;

        } else {
            // Call getIdentityId method and return null
            this.getIdentityId();
            return null;
        }
    }

    // If the app has a valid identityId return it, otherwise get a valid
    // identityId from your backend.

    public String getIdentityId() {

        // Load the identityId from the cache
        identityId = this.idenityID;

        if (identityId == null) {

            // If the logins map is not empty make a call to your backend
            // to get the token and identityId

            if (getProviderName() != null && !this.loginsMap.isEmpty()
                    && this.loginsMap.containsKey(getProviderName())) {

                /**
                 * This is where you would call your backend
                 **/

                // now set the returned identity id and token in the provider
                update(identityId, token);
                return token;

            } else {
                // Otherwise call &COG; using getIdentityId of super class
                return super.getIdentityId();
            }

        } else {
            return identityId;
        }

    }
}