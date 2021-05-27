 package com.application.chatroomsv2;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

 public class MainActivity extends AppCompatActivity {

    ListView lvChatRooms;
    ArrayList<String> listOfChatRooms = new ArrayList<String>();
    ArrayAdapter arrayAdapter;

    String UserName;

    private DatabaseReference dbr = FirebaseDatabase.getInstance().getReference().getRoot();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkRootMethod1() || checkRootMethod2() || checkRootMethod3()){
            Toast.makeText(this, "rooted device detected", Toast.LENGTH_SHORT).show();
            MainActivity.this.finish();
            System.exit(0);
        }

        if (isEmulator()){
            Toast.makeText(this, "emulator detected", Toast.LENGTH_SHORT).show();
            MainActivity.this.finish();
            System.exit(0);
        }

        lvChatRooms = (ListView) findViewById(R.id.lvChatRooms);
        arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, listOfChatRooms);
        lvChatRooms.setAdapter(arrayAdapter);

        getUserName();

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<String>();
                Iterator i = snapshot.getChildren().iterator();

                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                arrayAdapter.clear();
                arrayAdapter.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvChatRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i =new Intent(getApplicationContext(), ChatActivity.class );
                i.putExtra("selected_chat_room", ((TextView)view).getText().toString());
                i.putExtra("user_name", UserName);
                startActivity(i);
            }
        });
    }

    private void getUserName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        EditText userName = new EditText(this);

        builder.setView(userName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                UserName = userName.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getUserName();
            }
        });
        builder.show();
    }


     private static boolean checkRootMethod1() {
         String buildTags = android.os.Build.TAGS;
         return buildTags != null && buildTags.contains("test-keys");
     }

     private static boolean checkRootMethod2() {
         String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
                 "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                 "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
         for (String path : paths) {
             if (new File(path).exists()) return true;
         }
         return false;
     }

     private static boolean checkRootMethod3() {
         Process process = null;
         try {
             process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
             BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
             if (in.readLine() != null) return true;
             return false;
         } catch (Throwable t) {
             return false;
         } finally {
             if (process != null) process.destroy();
         }
     }

     private boolean isEmulator() {
         return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                 || Build.FINGERPRINT.startsWith("generic")
                 || Build.FINGERPRINT.startsWith("unknown")
                 || Build.HARDWARE.contains("goldfish")
                 || Build.HARDWARE.contains("ranchu")
                 || Build.MODEL.contains("google_sdk")
                 || Build.MODEL.contains("Emulator")
                 || Build.MODEL.contains("Android SDK built for x86")
                 || Build.MANUFACTURER.contains("Genymotion")
                 || Build.PRODUCT.contains("sdk_google")
                 || Build.PRODUCT.contains("google_sdk")
                 || Build.PRODUCT.contains("sdk")
                 || Build.PRODUCT.contains("sdk_x86")
                 || Build.PRODUCT.contains("vbox86p")
                 || Build.PRODUCT.contains("emulator")
                 || Build.PRODUCT.contains("simulator");
     }
}