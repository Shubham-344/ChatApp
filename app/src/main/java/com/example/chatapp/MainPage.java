package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chatapp.Fragments.ChatsFragment;
import com.example.chatapp.Fragments.UsersFragment;
import com.example.chatapp.Model.Model;
import com.example.chatapp.Model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPage extends AppCompatActivity {
    CircleImageView profile_img;
    TextView username;
    Button showBtn;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    //userlist
        RecyclerView UserRecyclerView,chat_user_recycler_view;
        public FirebaseDatabase database =FirebaseDatabase.getInstance();

        public DatabaseReference refernce =database.getReference().child("users");
        public ArrayList<Model> list;
        public ArrayList<Model> chatList;
        public MyAdapter myAdapter;
        ImageButton userListOpenBtn,userListCloseBtn;
    //userlist
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

//      TabLayout tablayout = findViewById(R.id.tab_layout);
//      ViewPager viewPager = findViewById(R.id.viewpager);
//      ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//      viewPager.setAdapter(viewPagerAdapter);
//      tablayout.setupWithViewPager(viewPager);
        userListOpenBtn = findViewById(R.id.userListOpenBtn);
        userListCloseBtn=findViewById(R.id.userListCloseBtn);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        profile_img = findViewById(R.id.profile_img);
        username = findViewById(R.id.username);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
//        showBtn = findViewById(R.id.showbtn);
//        showBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
     //chatList
        chat_user_recycler_view=findViewById(R.id.chat_user_recycler_view);
        chat_user_recycler_view.setHasFixedSize(true);
        chat_user_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        chatList= new ArrayList<>();
        myAdapter=new MyAdapter(this,chatList);
        chat_user_recycler_view.setAdapter(myAdapter);

     //chatlist
    //userlist
        UserRecyclerView = findViewById(R.id.user_recycler_view);
        UserRecyclerView.setHasFixedSize(true);
        UserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        UserRecyclerView.setAdapter(myAdapter);
        String user = firebaseUser.getUid();
        showUsers();
    //userlist
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                profile_img.setImageResource(R.drawable.baseline_person_24);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        refernce.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Model model = dataSnapshot.getValue(Model.class);
                    User user = dataSnapshot.getValue(User.class);
                    Log.d("shubham", firebaseUser.getUid());
                    Log.d("shubham", model.getId());
                    if(model.getId().equals(firebaseUser.getUid())){
                        Log.d("shubham", "loged in user");
                    }
                    else
                    {list.add(model);}

                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        UserRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        userListOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUsers();
            }
        });

        userListCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideUser();
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainPage.this, LoginPage.class));
            finish();
            return true;
        }
        return false;
    }
    public void showUsers(){
        UserRecyclerView.setVisibility(View.VISIBLE);
        userListCloseBtn.setVisibility(View.GONE);
        userListOpenBtn.setVisibility(View.GONE);
    }

    private void hideUser() {
        UserRecyclerView.setVisibility(View.GONE);
        userListCloseBtn.setVisibility(View.GONE);
        userListOpenBtn.setVisibility(View.VISIBLE);
    }
}