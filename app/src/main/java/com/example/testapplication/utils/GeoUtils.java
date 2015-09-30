package com.example.testapplication.utils;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by apelipets on 7/29/15.
 */
public final class GeoUtils {

    public static LatLng getLocationFromAddress(Geocoder geocoder, String strAddress){
        List<Address> addressList;
        LatLng result = null;
        try {
            addressList = geocoder.getFromLocationName(strAddress, 1);
            if(addressList != null && addressList .size() >0){
                Address address =  addressList .get(0);
                result = new LatLng(address.getLatitude(), address.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        }

        return result;
    }
}
