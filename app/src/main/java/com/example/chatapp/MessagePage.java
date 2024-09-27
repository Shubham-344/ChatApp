package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MessagePage extends AppCompatActivity {
    //MSG
    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenceMsg = database.getReference().child("chats");
    private MyAdapter myAdapter;
    private ArrayList<String> arrUser;
    //MSG
    ImageView profilePhoto;
    TextView usernameView;
    DatabaseReference reference;
    Intent intent;
    //msg
    String senderID;
    String recieverID;
    ImageView sendBtn;
    EditText msgText;
    RecyclerView msgRecyclerView;
    ArrayList<Model2> mList;
    MyAdapter2 myAdapter2;
//    public void fetchUser(){
//        referenceMsg.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Model2 model2 =dataSnapshot.getValue(Model2.class);
////                    if((Objects.equals(model2.getSenderID(), senderID) && Objects.equals(model2.getReciverID(), recieverID)) ||
////                            (Objects.equals(model2.getReciverID(), senderID) && Objects.equals(model2.senderID, recieverID)))
//                    {mList.add(model2);}
//                }
//                myAdapter2.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_page);

        //region Toolbar

        Toolbar toolbar = findViewById(R.id.chatPageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Log.d("stepscheck", "1");

//endregion


        //region OpenChatPageOnClick
        profilePhoto = findViewById(R.id.inChat_profile_img);
        usernameView= findViewById(R.id.chat_username_show);

        intent = getIntent();
        String  userid = intent.getStringExtra("userid");
        String username = intent.getStringExtra("username");

        reference = FirebaseDatabase.getInstance().getReference("users").child(userid);
        Log.d("stepscheck", "4");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model model = snapshot.getValue(Model.class);
                usernameView.setText(username);
                Log.d("stepscheck", "5");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recieverID = intent.getStringExtra("userid");
        senderID=FirebaseAuth.getInstance().getUid();
        String chatRooms = senderID + recieverID;


        msgRecyclerView = findViewById(R.id.msgRecyclerView);
        msgRecyclerView.setHasFixedSize(true);
        msgRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mList = new ArrayList<>();
        myAdapter2 = new MyAdapter2(this,mList);
        msgRecyclerView.setAdapter(myAdapter2);
        referenceMsg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Model2 model2 =dataSnapshot.getValue(Model2.class);
                    if((model2.getSenderID().equals(senderID)&&model2.getRecieverID().equals(recieverID)) ||(model2.getSenderID().equals(recieverID) && model2.getRecieverID().equals(senderID)))
                    {mList.add(model2);}
                }
                myAdapter2.notifyDataSetChanged();
                msgRecyclerView.scrollToPosition(myAdapter2.getItemCount()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendBtn = findViewById(R.id.msgSendBtn);
        msgText =findViewById(R.id.msgEdit);

        Log.d("stepcheck", senderID+" "+recieverID);
        String link = senderID + recieverID;
        Log.d("stepscheck", "6");

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = msgText.getText().toString();
                if (msgText.getText().toString().equals("")) {
                    Toast.makeText(MessagePage.this, "Write something..", Toast.LENGTH_SHORT).show();
                } else {
                    msgText.setText("");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chats");
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("senderID", senderID);
                    hashMap.put("recieverID", recieverID);
                    hashMap.put("message", message);
                    Log.d("stepscheck", "7");
                    reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MessagePage.this, message + "sent", Toast.LENGTH_SHORT).show();
                            msgText.setText("");
                        }
                    });
                    myAdapter2.notifyDataSetChanged();
                }
            }
        });

        msgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgRecyclerView.scrollToPosition(myAdapter2.getItemCount()-1);
            }
        });
        msgRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                msgRecyclerView.scrollToPosition(myAdapter2.getItemCount()-1);
            }
        });
    }
}