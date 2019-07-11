package co.edu.unal.krunko.sitespins.businessLogic;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;

import co.edu.unal.krunko.sitespins.dataAccess.models.User;
import co.edu.unal.krunko.sitespins.dataAccess.repositories.UserRepository;

public class MapControllers {

    private Activity activity;git

    public MapControllers(){
        this.activity = null;
    }

    public LatLng markerOfTheDay(){
        LatLng markerOfTheDay  = new LatLng(-34, 151);
        return markerOfTheDay;
    }


}
