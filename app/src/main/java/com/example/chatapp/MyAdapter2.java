package com.example.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> {
    ArrayList<Model2> mList;
    Context context;
    public boolean selection = false;
    public static final int MSG_TYPE_LEFT =0;
    public static final int MSG_TYPE_RIGHT=1;
    public MyAdapter2(Context context, ArrayList<Model2> mList){
        this.context = context;
        this.mList=mList;
    }
    @NonNull
    @Override
    public MyAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View v = LayoutInflater.from(context).inflate(R.layout.msg_sent_view, parent, false);
            return new MyViewHolder(v);
        }
        else{
            View v = LayoutInflater.from(context).inflate(R.layout.msg_recieved_view, parent, false);
            return new MyViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter2.MyViewHolder holder, int position) {
        Model2 model2 =mList.get(position);
        holder.message.setText(model2.getMessage());
//        holder.message.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.anim_one));
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View view) {
//                if(!selection)
//                {view.setBackgroundResource(R.drawable.selected_bg);}
//                selection=true;
//                Toast.makeText(context, "selected", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selection=!selection;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.ChatMsgTextView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String fuser = FirebaseAuth.getInstance().getUid();
        if(mList.get(position).getSenderID().equals(fuser)){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}
