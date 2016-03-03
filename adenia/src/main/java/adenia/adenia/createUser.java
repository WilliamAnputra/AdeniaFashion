package adenia.adenia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class createUser extends AppCompatActivity implements View.OnKeyListener,AdapterView.OnItemSelectedListener {

    EditText username;
    EditText passwords;


    Spinner spinner;
    TextView typeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);


        username=(EditText)findViewById(R.id.username);
        passwords=(EditText)findViewById(R.id.passwordCreate);


        username.setOnKeyListener(this);
        passwords.setOnKeyListener(this);

        spinner=(Spinner)findViewById(R.id.spinner);

        ArrayAdapter adapter= ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }




    public void create(View view){
        final ParseUser user=new ParseUser();
        user.setUsername(String.valueOf(username.getText()));
        user.setPassword(String.valueOf(passwords.getText()));
        user.put("type", String.valueOf(typeText.getText()));
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in all the form", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_ENTER){
            InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(username.getWindowToken(),0);
            imm.hideSoftInputFromWindow(passwords.getWindowToken(),0);
            return true;
        }

        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         typeText=(TextView)view;


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Toast.makeText(getApplicationContext(),"You have to choose the type of user",Toast.LENGTH_LONG).show();
    }

    public void viewUser(View view){
        Intent viewUser= new Intent(this,viewUser.class);
        startActivity(viewUser);

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),seephotoActivity.class);
        startActivity(intent);
        finish();
    }
}
