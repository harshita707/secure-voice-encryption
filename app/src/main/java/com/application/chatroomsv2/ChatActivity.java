package com.application.chatroomsv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    Button btnSendMessage;
    EditText etMsg;
    ListView lvChatroom;

    ArrayList<String> listConversation = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    private DatabaseReference dbr;

    String UserName, SelectedRoom, user_msg_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnSendMessage = (Button)findViewById(R.id.btn_send);
        etMsg = (EditText) findViewById(R.id.et_Message);

        lvChatroom = (ListView) findViewById(R.id.lv_chats);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                listConversation);
        lvChatroom.setAdapter(arrayAdapter);

        UserName = getIntent().getExtras().get("user_name").toString();
        SelectedRoom = getIntent().getExtras().get("selected_chat_room").toString();
        setTitle("ChatRoom: " + SelectedRoom);

        dbr = FirebaseDatabase.getInstance().getReference().child(SelectedRoom);

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String , Object>();
                user_msg_key =dbr.push().getKey();
                dbr.updateChildren(map);

                DatabaseReference dbr2 = dbr.child(user_msg_key);
                Map<String, Object> map2 = new HashMap<String , Object>();
                map2.put("msg", etMsg.getText().toString());
                map2.put("user", UserName);
                dbr2.updateChildren(map2);

            }
        });

        dbr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateConversation(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                updateConversation(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void updateConversation(DataSnapshot snapshot){
        String msg, user, conversation;
        Iterator i = snapshot.getChildren().iterator();
        while(i.hasNext()){
            msg = (String)((DataSnapshot)i.next()).getValue();
            user = (String)((DataSnapshot)i.next()).getValue();

            conversation = user + ": " + msg;
            arrayAdapter.add(conversation);
            arrayAdapter.notifyDataSetChanged();
        }
    }
}