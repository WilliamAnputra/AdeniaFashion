package adenia.adenia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by william on 1/18/16.
 */
public class viewUserAdapter extends RecyclerView.Adapter<viewUserAdapter.showViewHolder> {


    Context context;
    List<userDescription>data;


    public viewUserAdapter(Context context,List<userDescription>data){
        this.context=context;
        this.data=data;
    }



    @Override
    public viewUserAdapter.showViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.custom_layout_2,parent,false);

        showViewHolder holder= new showViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(viewUserAdapter.showViewHolder holder, int position) {

        userDescription current= data.get(position);

        holder.showEcers.setText(current.showEcer);

        holder.showResellers.setText(current.showReseller);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class showViewHolder extends RecyclerView.ViewHolder {

        TextView showEcers;
        TextView showResellers;


        public showViewHolder(View itemView) {
            super(itemView);

            showEcers= (TextView)itemView.findViewById(R.id.ecerTextView);
            showResellers=(TextView)itemView.findViewById(R.id.resellerTextView);


        }
    }
}
