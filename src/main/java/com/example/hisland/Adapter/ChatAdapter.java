package com.example.hisland.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hisland.AppActivity.AddFriend;
import com.example.hisland.AppActivity.RecieveCom;
import com.example.hisland.Bean.Comunity;
import com.example.hisland.Bean.Friendinfo;
import com.example.hisland.Bean.Msg;
import com.example.hisland.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Comunity> data;

    private final int N_TYPE = 0;
    private final int F_TYPE = 1;

    private int Max_num = 1000;  //预加载的数据  一共15条

    private Boolean isfootview = true;  //是否有footview

    public ChatAdapter(Context context,List<Comunity> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comunity_item,viewGroup,false);
            return new ChatAdapter.RecyclerViewHolder(view,N_TYPE);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (isfootview && (getItemViewType(i))== F_TYPE){
            //底部加载提示
            final ChatAdapter.RecyclerViewHolder recyclerViewHolder = (ChatAdapter.RecyclerViewHolder) viewHolder;
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
            final ChatAdapter.RecyclerViewHolder recyclerViewHolder = (ChatAdapter.RecyclerViewHolder) viewHolder;
            final Comunity comunity = data.get(i);

            recyclerViewHolder.c_name.setText(comunity.getName());
            recyclerViewHolder.c_info.setText(comunity.getInfo());
            recyclerViewHolder.c_user.setText(comunity.getUser());
            recyclerViewHolder.use.setText(comunity.getUse());
            recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recyclerViewHolder.getAdapterPosition();
                    Intent in = new Intent(context, RecieveCom.class);
                    in.putExtra("username",comunity.getName());
                    in.putExtra("info",comunity.getInfo());
                    in.putExtra("onw",comunity.getUser());
                    in.putExtra("use",comunity.getUse());
//                    in.putExtra("id",data.get(position).getObjectId());
                    context.startActivity(in);
                }
            });
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

        public TextView c_name,c_info,c_user,use; //ord_item的TextView
        public TextView Loading;


        public RecyclerViewHolder(View itemview, int view_type) {
            super(itemview);
            if (view_type == N_TYPE){
                c_name = itemview.findViewById(R.id.c_name);
                c_info = itemview.findViewById(R.id.c_info);
                c_user = itemview.findViewById(R.id.c_user);
                use    = itemview.findViewById(R.id.use);
            }else if(view_type == F_TYPE){

            }
        }
    }
}
