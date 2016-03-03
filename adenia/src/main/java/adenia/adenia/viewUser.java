package adenia.adenia;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class viewUser extends AppCompatActivity {

    TextView ecerSize;
    TextView resellerSize;
    viewUserAdapter viewUserAdapter;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    userDescription current;
    final ArrayList<ParseUser>deletedUser= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);


        recyclerView=(RecyclerView)findViewById(R.id.showEcer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        recyclerView2=(RecyclerView)findViewById(R.id.showReseller);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));

        ecerSize=(TextView)findViewById(R.id.ecer_size);
        resellerSize=(TextView)findViewById(R.id.reseller_size);


    ParseQuery<ParseUser>queryEcerUser= ParseUser.getQuery();
        queryEcerUser.whereEqualTo("type", "ecer");
        queryEcerUser.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {

                ecerSize.setText(String.valueOf(list.size()));
            }
        });



        ParseQuery<ParseUser>queryResellerUser= ParseUser.getQuery();
        queryResellerUser.whereEqualTo("type","reseller");
        queryResellerUser.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {

                resellerSize.setText(String.valueOf(list.size()));
            }
        });

        loadEcer();
        loadReseller();
    }

    public void loadEcer(){

        final List<userDescription> data= new ArrayList<>();

        final ParseQuery<ParseUser> queryEcer=ParseUser.getQuery();


        queryEcer.whereEqualTo("type","ecer");
        queryEcer.orderByDescending("createdAt");
        queryEcer.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {

                if (e==null && list.size()>0){
                    for (ParseUser user:list){

                        current= new userDescription();
                        current.showEcer=user.getUsername();
                        data.add(current);

                        deletedUser.add(user);
                    }
                    viewUserAdapter = new viewUserAdapter(getApplicationContext(), data);
                    recyclerView.setAdapter(viewUserAdapter);

                    recyclerView.addOnItemTouchListener(new seephotoActivity.RecyclerViewListener(getApplicationContext(), recyclerView, new seephotoActivity.ClickListener() {
                        @Override
                        public void onClick(View view, int position) {


                        }

                        @Override
                        public void onLongClick(View view, final int position) {


                        }
                    }));

                }
            }
        });


    }

    public void loadReseller(){

        final List<userDescription> datas= new ArrayList<>();
        ParseQuery<ParseUser>queryReseller= ParseUser.getQuery();
        queryReseller.whereEqualTo("type","reseller");
        queryReseller.orderByDescending("createdAt");
        queryReseller.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
               if (e==null && list.size()>0){
                   for (ParseUser user:list){
                       userDescription currents= new userDescription();
                       currents.showReseller= user.getUsername();
                       datas.add(currents);
                   }

                   viewUserAdapter = new viewUserAdapter(getApplicationContext(), datas);
                   recyclerView2.setAdapter(viewUserAdapter);
               }
            }
        });

    }


    public void refreshViewUserPage(){

        Intent intents= new Intent(viewUser.this,viewUser.class);
        startActivity(intents);
    }

}
