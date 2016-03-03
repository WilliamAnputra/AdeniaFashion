package adenia.adenia;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;

/**
 * Created by william on 1/8/16.
 */
public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(this, "1iDHsHz7hA1UZNYtExUbhaREvXvm92FT8yT36Y5T", "I7iuP4KmQeMkZiq8i6oyWCK9pydsgnjP5YmiCbkm");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


    }
}
