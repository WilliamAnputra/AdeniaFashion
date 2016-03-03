package adenia.adenia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ParseUser currentUser= ParseUser.getCurrentUser();

        if (currentUser!=null && currentUser.getObjectId().equals("5mSPWwxjp3")){
            Intent intent= new Intent(getApplicationContext(),seephotoActivity.class);
            startActivity(intent);
            finish();
        }
        else if (currentUser!=null && currentUser.get("type").equals("ecer")){
            Intent ecer= new Intent(getApplicationContext(), adenia.adenia.ecer.class);
            startActivity(ecer);
            finish();
        }
        else if (currentUser!=null && currentUser.get("type").equals("reseller")){
            Intent reseller= new Intent(getApplicationContext(), reseller.class);
            startActivity(reseller);
            finish();
        }
        else {
            Intent x= new Intent(getApplicationContext(),loginOrSignUp.class);
            startActivity(x);
            finish();
        }


    }
}
