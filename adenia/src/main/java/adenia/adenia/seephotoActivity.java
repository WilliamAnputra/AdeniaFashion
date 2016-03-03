package adenia.adenia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SendCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class seephotoActivity extends AppCompatActivity {
    EditText edit_text_progressbar;
    EditText adminInput;
    EditText reseller;
    private Toolbar toolbar;
    Uri selectedImage;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    Button notifyButton;
    Button ecerButton;
    Button resellerButton;
    Uri uri;

    RecyclerView recyclerView;
    myAdapter myAdapter;
    String userObjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seephoto);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.adenialogonew);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Loading.. Please wait");
        progressDialog.setIndeterminate(true);
        progressDialog.show();


        Typeface Aargh = Typeface.createFromAsset(getAssets(), "fonts/Aaargh.ttf");
        Typeface Alex = Typeface.createFromAsset(getAssets(), "fonts/AlexBrush-Regular.ttf");
        Typeface Roboto = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");



        adminInput = (EditText) findViewById(R.id.adminInput);
        edit_text_progressbar = (EditText) findViewById(R.id.saveEcer);


        userObjectId= ParseUser.getCurrentUser().getObjectId();


        ParseQuery<ParseObject> getAdminInput=  ParseQuery.getQuery("adminInput");
        getAdminInput.whereEqualTo("userObjectId", userObjectId);
        getAdminInput.orderByDescending("CreatedAt");
        getAdminInput.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject obj : list) {
                        adminInput.setText(obj.get("newMessage").toString());

                    }
                }
            }
        });

        notifyButton=(Button)findViewById(R.id.notifyButton);
        ecerButton=(Button)findViewById(R.id.ecer_button);

        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder= new AlertDialog.Builder(seephotoActivity.this);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setMessage("Do you want to notify customer?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ParsePush.subscribeInBackground("newItems");

                        ParsePush push= new ParsePush();
                        push.setMessage("Hai kk.. Cek Upload-an Terbaru Kita Ya..");
                        push.sendInBackground(new SendCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(getApplicationContext(), "Notification success", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Notification Error, Contact developer", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));

        loadData();
    }

    public void loadData(){

        final List<ImageUrl> data = new ArrayList<>();
        final ArrayList<ParseObject> parseObjectList=new ArrayList<>();

        ParseQuery<ParseObject>query= ParseQuery.getQuery("Images");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && list.size() > 0) {


                    for (ParseObject object : list) {
                        ParseFile file = object.getParseFile("image");
                        ImageUrl current = new ImageUrl();
                        current.url = file.getUrl();
                        data.add(current);
                        parseObjectList.add(object);
                    }
                    myAdapter = new myAdapter(getApplicationContext(), data);
                    recyclerView.setAdapter(myAdapter);
                    progressDialog.dismiss();


                    recyclerView.addOnItemTouchListener(new RecyclerViewListener(getApplicationContext(), recyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {

                            Intent next = new Intent(seephotoActivity.this,singleActivityViewSuperUser.class);
                            next.putExtra("imageUrl", data.get(position).url);
                            next.putExtra("objectId", parseObjectList.get(position).getObjectId());
                            startActivity(next);


                        }

                        @Override
                        public void onLongClick(View view, final int position) {
                            new AlertDialog.Builder(seephotoActivity.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Delete this photo?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            try {
                                                parseObjectList.get(position).delete();
                                                refresh();

                                            } catch (ParseException e1) {
                                                Toast.makeText(getApplicationContext(), "Error, please try again in a few seconds", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                    }));

                }
            }
        });
    }

    public void createUser(View view) {

            Intent create= new Intent(getApplicationContext(), createUser.class);
            startActivity(create);
            finish();
    }



    public void saveInfo(View view) {

        ParseObject adminInputText= new ParseObject("adminInput");
        adminInputText.put("newMessage", adminInput.getText().toString());
        adminInputText.put("userObjectId",userObjectId);
        adminInputText.pinInBackground();
        adminInputText.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "Your message has been edited", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), seephotoActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    public void refresh() {
        Intent intent = new Intent(getApplicationContext(), seephotoActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seephoto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sharePhoto) {

            Intent getPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(getPhoto, 1);
            return true;
        } else if (id == R.id.logOut) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParseUser.logOut();
                            Intent exit = new Intent(getApplicationContext(), loginOrSignUp.class);
                            startActivity(exit);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else if (id == R.id.refresh) {
            this.finish();
            refresh();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==RESULT_OK &&data!=null){

            selectedImage= data.getData();
            new DelayTask().execute();

        }

    }

    public class DelayTask extends AsyncTask<Void,Integer,String> {
        int count = 0;

        @Override
        protected void onPreExecute() {

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setEnabled(true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), selectedImage);

                Bitmap newBitmap= bitmapResizer(bitmap, 550, 550);

                ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();

                newBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);

                byte[] bytearray= byteArrayOutputStream.toByteArray();

                ParseFile file= new ParseFile("image.png",bytearray);

                ParseObject newImage= new ParseObject("Images");
                newImage.put("username",ParseUser.getCurrentUser().getUsername());

                newImage.put("image",file);

                newImage.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){

                            progressBar.setVisibility(View.INVISIBLE);
                            progressBar.setEnabled(false);
                            refresh();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There was an error,Please try again or contact admin", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
            while(count<5){
                SystemClock.sleep(1000); count++;
                publishProgress(count*20);
            }
            return "Complete";

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }


    }

    public Bitmap bitmapResizer(Bitmap bitmap,int newWidth, int newHeight){
        Bitmap scaledBitmap= Bitmap.createBitmap(newWidth,newHeight,Bitmap.Config.ARGB_8888);

        float ratioX= newWidth/(float)bitmap.getWidth();
        float ratioY= newHeight/(float)bitmap.getHeight();
        float middleX= newWidth/2.0f;
        float middleY= newWidth/2.0f;

        Matrix scaleMatrix= new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas= new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }


    public static class RecyclerViewListener implements RecyclerView.OnItemTouchListener{

        GestureDetector gestureDetector;
        ClickListener clicklistener;

        public RecyclerViewListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){

            this.clicklistener=clickListener;
            gestureDetector= new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.i("this is","SingleTap");
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child= recyclerView.findChildViewUnder(e.getX(),e.getY());

                    if (child!=null && clickListener!=null){

                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }

                    super.onLongPress(e);
                }
            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child= rv.findChildViewUnder(e.getX(),e.getY());

            if (child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){

                clicklistener.onClick(child,rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener{
        void onClick(View view,int position);
        void onLongClick(View view,int position);
    }


    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
