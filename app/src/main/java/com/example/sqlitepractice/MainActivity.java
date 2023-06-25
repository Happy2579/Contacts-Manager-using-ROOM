package com.example.sqlitepractice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlitepractice.adapter.ContactsAdapter;
import com.example.sqlitepractice.db.ContactsAppDatabase;
//import com.example.sqlitepractice.db.DatabaseHelper;
import com.example.sqlitepractice.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contactArrayList=new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAppDatabase contactsAppDatabase;
    //private DatabaseHelper db;                          If we use basic SQLite

    //To display contacts in the background
    private void DisplayAllContactInBackground(){
        ExecutorService executor= Executors.newSingleThreadExecutor();
        Handler handler=new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                //Background Work
                contactArrayList.addAll(contactsAppDatabase.getContactDAO().getContacts());
                //Executed after the background work had finished
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        contactsAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Favourite Contacts");

        //RecyclerView
        recyclerView=findViewById(R.id.recycler_view_contacts);


        //Callbacks








      //  db=new DatabaseHelper(this);                    If we use basic SQLite

        //Database
        contactsAppDatabase= Room.databaseBuilder(
                getApplicationContext(),
                ContactsAppDatabase.class,
                "ContactDB").allowMainThreadQueries().build();

        DisplayAllContactInBackground();

        //ContactsList
        //contactArrayList.addAll(db.getAllContacts());    If we use SQLite

        //Getting All Contacts
        contactArrayList.addAll(contactsAppDatabase.getContactDAO().getContacts());

        //ContactsAdapter
        contactsAdapter=new ContactsAdapter(this, contactArrayList,MainActivity.this);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);

        FloatingActionButton fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addandEditContacts(false,null,-1);
            }
        });
    }

    public void addandEditContacts(final boolean isUpdated,final Contact contact, final int position) {
        LayoutInflater layoutInflater=LayoutInflater.from(getApplicationContext());
        View view=layoutInflater.inflate(R.layout.layout_add_contact,null);

        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        TextView contactTitle=view.findViewById(R.id.new_contact_title);
        final EditText newContact=view.findViewById(R.id.name);
        final EditText contactEmail=view.findViewById(R.id.email);

        contactTitle.setText(!isUpdated ? "Add New Contact":"Edit Contact");

        if(isUpdated && newContact!=null){
            newContact.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }
        alertDialogBuilder.setCancelable(false).setPositiveButton(!isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isUpdated) {
                    DeleteContact(contact,position);
                }else{
                    dialog.cancel();
                }
            }
        });

        final AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(newContact.getText().toString())){
                    Toast.makeText(MainActivity.this, "Enter a Name", Toast.LENGTH_SHORT).show();

                    return;
                }else{
                    alertDialog.dismiss();
                }

                if(isUpdated && contact!=null){
                    UpdateContact(newContact.getText().toString(), contactEmail.getText().toString(),position);
                }
                else{
                    CreateContact(newContact.getText().toString(),contactEmail.getText().toString());
                }
            }
        });
    }

    private void DeleteContact(Contact contact, int position) {
        contactArrayList.remove(position);
        contactsAppDatabase.getContactDAO().deleteContact(contact);
        contactsAdapter.notifyDataSetChanged();
    }

    private void UpdateContact(String name, String email,int position){
        Contact contact=contactArrayList.get(position);

        contact.setName(name);
        contact.setEmail(email);

        //db.updateContact(contact);                    If we use SQLite

        contactsAppDatabase.getContactDAO().updateContact(contact);
        contactArrayList.set(position,contact);
        contactsAdapter.notifyDataSetChanged();
    }

    private void CreateContact(String name, String email){
        long id=contactsAppDatabase.getContactDAO().addContact(new Contact(name, email,0));
        Contact contact=contactsAppDatabase.getContactDAO().getContact(id);

        if(contact!=null){
            contactArrayList.add(0,contact);
            contactsAdapter.notifyDataSetChanged();
        }
    }

    //Menu Bar


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();

        if(id==R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}