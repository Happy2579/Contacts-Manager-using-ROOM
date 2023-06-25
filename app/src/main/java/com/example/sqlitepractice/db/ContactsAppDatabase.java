package com.example.sqlitepractice.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.sqlitepractice.db.entity.Contact;

@Database(entities={Contact.class},version = 1)
public abstract class ContactsAppDatabase extends RoomDatabase {

    //Linking DAO with our database
    public abstract ContactDAO getContactDAO();
}
