package com.example.sqlitepractice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqlitepractice.adapter.ContactsAdapter;
import com.example.sqlitepractice.db.DatabaseHelper;
import com.example.sqlitepractice.db.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contactArrayList=new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseHelper db;

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
        db=new DatabaseHelper(this);

        //ContactsList
        contactArrayList.addAll(db.getAllContacts());

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
        db.deleteContact(contact);
        contactsAdapter.notifyDataSetChanged();
    }

    private void UpdateContact(String name, String email,int position){
        Contact contact=contactArrayList.get(position);

        contact.setName(name);
        contact.setEmail(email);

        db.updateContact(contact);

        contactArrayList.set(position,contact);
        contactsAdapter.notifyDataSetChanged();
    }

    private void CreateContact(String name, String email){
        long id=db.insertcontact(name, email);
        Contact contact=db.getContact(id);

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