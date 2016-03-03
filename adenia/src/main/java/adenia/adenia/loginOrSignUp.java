package adenia.adenia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class loginOrSignUp extends AppCompatActivity implements View.OnKeyListener {

    EditText username;
    EditText password;

    TextView ade_nia;
    TextView fashion;

    private Toolbar toolbar;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);


        Typeface BlackAdder=Typeface.createFromAsset(getAssets(),"fonts/ITCBLKAD.TTF");
        Typeface LCALLIG=Typeface.createFromAsset(getAssets(),"fonts/LCALLIG.TTF");


        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.passwords);

        username.setOnKeyListener(this);
        password.setOnKeyListener(this);

        ade_nia=(TextView)findViewById(R.id.adeNia);
        ade_nia.setTypeface(BlackAdder);

        fashion=(TextView)findViewById(R.id.fashion);
        fashion.setTypeface(LCALLIG);


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_ENTER){
            InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(username.getWindowToken(),0);
            imm.hideSoftInputFromWindow(password.getWindowToken(),0);
            return true;
        }

        return false;
    }



    public void login(View view){

        ParseUser.logInInBackground(String.valueOf(username.getText()), String.valueOf(password.getText()), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (user != null && user.getUsername().equals("adenia")) {

                    Intent intent = new Intent(getApplicationContext(), seephotoActivity.class);
                    startActivity(intent);
                    finish();
                }

                  else  if (user!=null && ParseUser.getCurrentUser().get("type").equals("ecer")){
                        Intent ecer = new Intent(getApplicationContext(), ecer.class);
                        startActivity(ecer);
                    finish();
                    }
                   else if (user!=null && ParseUser.getCurrentUser().get("type").equals("reseller")){
                        Intent reseller = new Intent(getApplicationContext(), reseller.class);
                        startActivity(reseller);
                    finish();
                    }

                else {

                    Toast.makeText(getApplicationContext(), "Please contact Adenia for verification", Toast.LENGTH_LONG).show();
                }

            }

        });



    }

}
