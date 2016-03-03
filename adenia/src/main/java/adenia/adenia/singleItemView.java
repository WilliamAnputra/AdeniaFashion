package adenia.adenia;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class singleItemView extends AppCompatActivity implements View.OnKeyListener {

    String imageUrl;
    String userObjectId;
    EditText editText;
    Button saveButton;
    TextView textView;
    RelativeLayout relativeLayout;
    Uri uri;
    Intent chooser;


    float scale=1f;
    ScaleGestureDetector SGD;
    android.opengl.Matrix matrix;
    ImageView detailImageView;
     String imageUris;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the view from singleitemview.xml
        setContentView(R.layout.activity_single_item_view);

        textView=(TextView)findViewById(R.id.tex);

        detailImageView=(ImageView)findViewById(R.id.detailImageView);

        final EditText adminDecidePrice=(EditText)findViewById(R.id.getInput);

        relativeLayout=(RelativeLayout)findViewById(R.id.singleItemView);

        Intent i = getIntent();
        // Get the intent from adapter
        imageUrl = i.getStringExtra("imageUrl");
        userObjectId=i.getStringExtra("objectId");

        Snackbar snackbar = Snackbar
                .make(relativeLayout, "Click To Zoom In", Snackbar.LENGTH_LONG)
                .setAction(" ", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
// Changing message text color
        snackbar.setActionTextColor(Color.BLACK);

// Changing action button text color
        View sbView = snackbar.getView();
        TextView textViews = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textViews.setTextColor(Color.BLACK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textViews.setLetterSpacing(0.4f);
        }


        if (ParseUser.getCurrentUser().get("type").equals("ecer")){

            ParseQuery<ParseObject>getQuery= ParseQuery.getQuery("ecerText");
            getQuery.whereEqualTo("userObjectId",userObjectId);
            getQuery.orderByDescending("CreatedAt");
            getQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null && list.size() > 0) {
                        for (ParseObject objs : list) {

                            adminDecidePrice.setText(String.valueOf(objs.get("newMessage")));
                            adminDecidePrice.setEnabled(false);
                        }
                    }
                }
            });
        }
        if (ParseUser.getCurrentUser().get("type").equals("reseller")){
            ParseQuery<ParseObject>getQuery= ParseQuery.getQuery("resellerText");
            getQuery.whereEqualTo("userObjectId",userObjectId);
            getQuery.orderByDescending("CreatedAt");
            getQuery.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e == null && list.size() > 0) {
                        for (ParseObject objs : list) {

                            adminDecidePrice.setText(String.valueOf(objs.get("newMessage")));
                            adminDecidePrice.setEnabled(false);
                        }
                    }
                }
            });
        }


        sbView.setBackgroundColor(getResources().getColor(R.color.snack_bar_color));
        snackbar.show();


        Typeface custom_font=Typeface.createFromAsset(getAssets(),"fonts/Aaargh.ttf");
        adminDecidePrice.setTypeface(custom_font);
        textViews.setTypeface(custom_font);
        textView.setTypeface(custom_font);


        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setPadding(0, 0, 0, 0);
        getSupportActionBar().setTitle(null);
        toolbar.setLogo(R.drawable.sharebuttonsmall);

            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Drawable drawable=detailImageView.getDrawable();
                    Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();

                    String path= MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"Image Description",null);
                    uri=Uri.parse(path);

                    Intent intent= new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM,uri);
                    chooser= intent.createChooser(intent,"Send Image");
                    startActivity(chooser);
                }
            });
        // Locate the ImageView in singleitemview.xml
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .asBitmap()
                .placeholder(R.drawable.whitebackground)
                .into(detailImageView);

        if (ParseUser.getCurrentUser().getUsername().equals("adenia")) {

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseObject userInput = new ParseObject("UserInput");
                    userInput.put("newMessage", editText.getText().toString());
                    userInput.put("userObjectId", userObjectId);
                    userInput.pinInBackground();
                    userInput.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "Your message has been edited", Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(getApplicationContext(),seephotoActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.i("Error", e.getMessage());

                            }
                        }
                    });

                }
            });
        }

        ParseQuery<ParseObject>query= ParseQuery.getQuery("UserInput");
        query.whereEqualTo("userObjectId", userObjectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {

                    for (ParseObject object : objects) {

                        if (ParseUser.getCurrentUser().getUsername().equals("adenia")) {
                            editText.setText(object.get("newMessage").toString());
                        } else {
                            textView.setText(object.get("newMessage").toString());
                        }
                    }
                }
            }
        });


        detailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendUrl=new Intent(getApplicationContext(),ZoomableActivity.class);
                sendUrl.putExtra("imageurl",imageUrl);
                startActivity(sendUrl);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_item_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if (id==R.id.saveImage){

            Bitmap bitmap=((BitmapDrawable)detailImageView.getDrawable()).getBitmap();
            String fileName = "test.jpg";

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File ExternalStorageDirectory = Environment.getExternalStorageDirectory();
            File file = new File(ExternalStorageDirectory + File.separator + fileName);

            FileOutputStream fileOutputStream = null;

            try {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytes.toByteArray());
                ContentResolver cr = getContentResolver();
                String imagePath = file.getAbsolutePath();
                String name = file.getName();
                String description = "My bitmap created by Android-er";
                String savedURL = MediaStore.Images.Media
                        .insertImage(cr, imagePath, name, description);
                Toast.makeText(getApplicationContext(),
                        "Image Saved",
                        Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if(fileOutputStream != null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
        return super.onOptionsItemSelected(item);

    }
    /*
        MenuItem.OnMenuItemClickListener SaveImageClickListener= new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


            }
        };
    */
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_ENTER){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
        return false;


    }




}
