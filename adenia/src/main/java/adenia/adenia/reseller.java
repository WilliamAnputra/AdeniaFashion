package adenia.adenia;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class reseller extends AppCompatActivity {

    Toolbar toolbar;
    TextView hellouser;
    EditText reseller;
    EditText getAdminInput;
    RecyclerView recyclerView;
    myAdapter myAdapter;
     ProgressDialog PG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reseller);



        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.adenialogonew);
        toolbar.setTitle(" ");
        PG= new ProgressDialog(this);
        PG.setMessage("Loading.. Please wait");
        PG.setIndeterminate(true);
        PG.show();


        getAdminInput=(EditText)findViewById(R.id.getAdminInput);


        ParseQuery<ParseObject>getQueri= ParseQuery.getQuery("adminInput");
        getQueri.orderByDescending("CreatedAt");
        getQueri.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && list.size() > 0) {
                    for (ParseObject objs : list) {

                        getAdminInput.setText(String.valueOf(objs.get("newMessage")));
                        getAdminInput.setEnabled(false);
                    }
                }
            }
        });

        String userId= ParseUser.getCurrentUser().getUsername();


        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        loadData();
    }

    public void loadData(){

        final List<ImageUrl> data= new ArrayList<>();

        final ArrayList<ParseObject> parseObjectList= new ArrayList<>();

        ParseQuery<ParseObject> query= ParseQuery.getQuery("Images");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e==null && list.size()>0){
                  for (ParseObject obj:list){
                      ImageUrl current= new ImageUrl();
                      current.url=obj.getParseFile("image").getUrl();
                      data.add(current);
                      parseObjectList.add(obj);
                  }
                    myAdapter=new myAdapter(getApplicationContext(),data);
                    recyclerView.setAdapter(myAdapter);
                    PG.dismiss();

                    recyclerView.addOnItemTouchListener(new RecyleViewListener(getApplicationContext(), recyclerView, new ClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Intent next = new Intent(getApplicationContext(), singleItemView.class);
                            next.putExtra("imageUrl", data.get(position).url);
                            next.putExtra("objectId", parseObjectList.get(position).getObjectId());
                            startActivity(next);
                        }

                        @Override
                        public void onLongClick(View view, int position) {

                        }
                    }));



                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.reseller_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();

        if (id==R.id.LogOut){

            new AlertDialog.Builder(this)
                    .setMessage("Anda yakin ingin LogOut?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParseUser.logOut();
                            Intent x = new Intent(getApplicationContext(), loginOrSignUp.class);
                            startActivity(x);
                            finish();
                        }
                    })
                    .setNegativeButton("Tidak",null)
                    .show();
        }
        if (id==R.id.refresh){

            Intent intent=new Intent(getApplicationContext(),reseller.class);
            startActivity(intent);
            finish();
        }

        if (id==R.id.exitApp){

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if (id==R.id.delete) {

            new AlertDialog.Builder(reseller.this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Delete " + ParseUser.getCurrentUser().getUsername() + " ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ParseUser.getCurrentUser().deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
                                        ParseUser.logOut();
                                        Intent intent = new Intent(getApplicationContext(), loginOrSignUp.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton("No",null)
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }

    class RecyleViewListener implements RecyclerView.OnItemTouchListener{

        ClickListener clickListener;
        GestureDetector gestureDetector;

        public RecyleViewListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){

            this.clickListener=clickListener;

            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
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

            if (child!=null && clickListener!=null &&gestureDetector.onTouchEvent(e)){
                clickListener.onClick(child,rv.getChildLayoutPosition(child));
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

    private Boolean exit=false;

    @Override
    public void onBackPressed() {
        if (exit){
            finish();
        }
        else{
            Toast.makeText(this,"Press Back again to Exit",Toast.LENGTH_SHORT).show();
            exit=true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit=false;
                }
            }, 3* 1000);
        }
    }
}
