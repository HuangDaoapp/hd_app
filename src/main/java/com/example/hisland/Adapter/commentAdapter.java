package com.example.hisland.Adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hisland.Bean.Commentinfo;
import com.example.hisland.R;

import java.util.List;

public class commentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Commentinfo> data;

    private final int N_TYPE = 0;
    private final int F_TYPE = 1;

    private int Max_num = 1000;//预加载的数据  一共15条

    private Boolean isfootview = true;  //是否有footview

    public commentAdapter(Context context,List<Commentinfo> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_list,viewGroup,false);
        return new RecyclerViewHolder(view,N_TYPE);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (isfootview && (getItemViewType(i))== F_TYPE){
            //底部加载提示
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) viewHolder;
            recyclerViewHolder.Loading.setText("加载中...");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Max_num += 8;
                    notifyDataSetChanged();
                }
            },2000);
        }else {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) viewHolder;
            final Commentinfo comment = data.get(i);


            recyclerViewHolder.comment_content.setText(comment.getInfo());
            recyclerViewHolder.comment_username.setText(comment.getUser());

//            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = recyclerViewHolder.getAdapterPosition();
//                    Intent in = new Intent(context, RecieveCom.class);
//                    in.putExtra("username",comunity.getName());
//                    in.putExtra("info",comunity.getInfo());
//                    in.putExtra("onw",comunity.getUser());
////                    in.putExtra("id",data.get(position).getObjectId());
//                    context.startActivity(in);
//                }
//            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == Max_num - 1){
            return F_TYPE;  //底部type
        }else {
            return N_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        if (data.size() < Max_num){
            return data.size();
        }
        return Max_num;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView comment_username,comment_content; //ord_item的TextView
        public TextView Loading;


        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if (view_type == N_TYPE){
                comment_username = itemview.findViewById(R.id.comment_username);
                comment_content = itemview.findViewById(R.id.comment_content);

            }else if(view_type == F_TYPE){

            }
        }
    }
}
