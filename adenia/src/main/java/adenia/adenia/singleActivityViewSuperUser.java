package adenia.adenia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class singleActivityViewSuperUser extends AppCompatActivity {

    EditText saveEcer;
    EditText saveReseller;
    private android.support.v7.widget.Toolbar toolbar;
    String imageUrl;
    ImageView detailImageView;
    String userObjectId;
    TextView smallEcer;
    TextView smallReseller;
    private String imageUris;
   Uri uri;
     private Intent chooser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single_activity_view_super_user);

        saveEcer=(EditText)findViewById(R.id.saveEcer);
        saveReseller=(EditText)findViewById(R.id.saveReseller);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.adenialogonewsmall);
        getSupportActionBar().setTitle(null);


        detailImageView=(ImageView)findViewById(R.id.detailImageView);
        smallEcer=(TextView)findViewById(R.id.smallEcer);
        smallReseller=(TextView)findViewById(R.id.smallReseller);


        Typeface Aargh = Typeface.createFromAsset(getAssets(), "fonts/Aaargh.ttf");
        Typeface Alex = Typeface.createFromAsset(getAssets(), "fonts/AlexBrush-Regular.ttf");
        Typeface Roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Typeface BlackAdder = Typeface.createFromAsset(getAssets(), "fonts/ITCBLKAD.TTF");
        Typeface LCALLIG = Typeface.createFromAsset(getAssets(), "fonts/LCALLIG.TTF");


        smallEcer.setTypeface(LCALLIG);
        smallReseller.setTypeface(LCALLIG);




        Intent i= getIntent();
        imageUrl = i.getStringExtra("imageUrl");
        userObjectId=i.getStringExtra("objectId");


        detailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendUrl = new Intent(getApplicationContext(), ZoomableActivity.class);
                sendUrl.putExtra("imageurl", imageUrl);
                startActivity(sendUrl);
            }
        });

        ParseQuery<ParseObject>query=ParseQuery.getQuery("ecerText");
        query.whereEqualTo("userObjectId", userObjectId);
        query.orderByDescending("CreatedAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject obj : list) {
                        saveEcer.setText(obj.get("newMessage").toString());
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ParseQuery<ParseObject> queries =ParseQuery.getQuery("resellerText");
        queries.orderByDescending("CreatedAt");
        queries.whereEqualTo("userObjectId", userObjectId);
        queries.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject obj : list) {
                        saveReseller.setText(obj.get("newMessage").toString());
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });


        loadImage();

    }

    public void saveButton(View view){

        ParseObject saveecer= new ParseObject("ecerText");
        saveecer.put("newMessage", String.valueOf(saveEcer.getText()));
        saveecer.put("userObjectId",userObjectId);
        saveecer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Message edited", Toast.LENGTH_SHORT).show();
                    goBack();
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ParseObject savereseller= new ParseObject("resellerText");
        savereseller.put("newMessage",String.valueOf(saveReseller.getText()));
        savereseller.put("userObjectId",userObjectId);
        savereseller.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    Toast.makeText(getApplicationContext(),"Message edited",Toast.LENGTH_SHORT).show();
                    goBack();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void saveReseller(View view){


    }

    public void refresh(){
        Intent intent= new Intent(this, singleActivityViewSuperUser.class);
        startActivity(intent);
        finish();
    }

    public void loadImage(){
        Glide.with(this)
                .load(imageUrl)
                .asBitmap()
                .placeholder(R.drawable.whitebackground)
                .into(detailImageView);
    }

    public void goBack(){
        Intent goBack= new Intent(getApplicationContext(),seephotoActivity.class);
        startActivity(goBack);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_activity_view_user,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int ItemId= item.getItemId();

        Drawable drawable=detailImageView.getDrawable();
        Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

        String path= MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Image Description",null);

        uri= Uri.parse(path);

        if (ItemId==R.id.shareButton){
            Intent intent= new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            chooser= intent.createChooser(intent, "Send Image");
            startActivity(chooser);
        }
        return true;
    }
}
