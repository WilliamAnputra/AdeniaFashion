package adenia.adenia;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by william on 1/7/16.
 */
public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder> {

    LayoutInflater inflater;
    List<ImageUrl> data=null;
    private Context context;


    public myAdapter(Context context,List<ImageUrl> data){
        this.context=context;
        this.data=data;
    }

    @Override
    public myAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= inflater.from(context).inflate(R.layout.custom_layout,parent,false);

        myViewHolder holder= new myViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(myAdapter.myViewHolder holder, int position) {

        ImageUrl current= data.get(position);
        Glide.with(context)
                .load(current.url)

                .centerCrop()
                .placeholder(R.drawable.whitebackground)
                .into(holder.imageViewRecycler);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewRecycler;

        public myViewHolder(View itemView) {
            super(itemView);
            imageViewRecycler=(ImageView)itemView.findViewById(R.id.imageViewRecycle);


        }
    }
}
