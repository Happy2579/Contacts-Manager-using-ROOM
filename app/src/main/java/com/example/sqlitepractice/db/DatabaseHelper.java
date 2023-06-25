// package com.example.sqlitepractice.db;
//
// import android.content.ContentValues;
// import android.content.Context;
// import android.database.Cursor;
// import android.database.sqlite.SQLiteDatabase;
// import android.database.sqlite.SQLiteOpenHelper;
//
// import com.example.sqlitepractice.db.entity.Contact;
//
// import java.util.ArrayList;



//***********************************************************
//If we use SQLite, we need to write a lot of code as written below
//***********************************************************
//
// public class DatabaseHelper extends SQLiteOpenHelper {
//
//     private static final int DATABASE_VERSION=1;
//     private static final String DATABASE_NAME="contact_db";
//
//     public DatabaseHelper(Context context){
//         super(context,DATABASE_NAME,null,DATABASE_VERSION);
//     }
//
//     @Override
//    // public void onCreate(SQLiteDatabase db) {
//      //   db.execSQL(Contact.CREATE_TABLE);
//
//     }
//
//     @Override
//     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//         db.execSQL("DROP TABLE IF EXISTS Contact.TABLE_NAME");
//
//         onCreate(db);
//
//     }
//
//     //Insert data into database
//
//     public long insertcontact(String name, String email){
//         SQLiteDatabase database=this.getWritableDatabase();
//         ContentValues values=new ContentValues();
//
//         values.put(Contact.COLUMN_NAME,name);
//         values.put(Contact.COLUMN_EMAIL,email);
//
//         long id=database.insert(Contact.TABLE_NAME,null,values);
//         database.close();
//
//         return id;
//     };
//
//     //Getting Contact from the database
//
//     public Contact getContact(long id){
//         SQLiteDatabase db=this.getReadableDatabase();
//         Cursor cursor=db.query(Contact.TABLE_NAME,new String[]{
//                 Contact.COLUMN_ID,
//                 Contact.COLUMN_NAME,
//                 Contact.COLUMN_EMAIL},
//                 Contact.COLUMN_ID+"=?",
//                 new String[]{
//                         String.valueOf(id)
//                 },
//                 null,
//                 null,
//                 null,
//                 null);
//
//         if(cursor!=null){
//             cursor.moveToFirst();
//         }
//
//         Contact contact=new Contact(
//         );
//
//         cursor.close();
//         return contact;
//     }
//
//     //Getting all Contacts Method
//
//     public ArrayList<Contact> getAllContacts(){
//         ArrayList<Contact> contacts=new ArrayList<>();
//
//         String selectQuery="SELECT * FROM " +Contact.TABLE_NAME+ " ORDER BY " +Contact.COLUMN_ID +" DESC";
//         SQLiteDatabase db=this.getWritableDatabase();
//         Cursor cursor=db.rawQuery(selectQuery,null);
//
//         if(cursor.moveToFirst()){
//             do{
//                 Contact contact=new Contact();
//                 contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COLUMN_ID)));
//                 contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_NAME)));
//                 contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COLUMN_EMAIL)));
//
//                 contacts.add(contact);
//             }while(cursor.moveToNext());
//         }
//         db.close();
//         return contacts;
//     }
//
//     public int updateContact(Contact contact){
//         SQLiteDatabase db=this.getWritableDatabase();
//
//         ContentValues values=new ContentValues();
//
//         values.put(Contact.COLUMN_NAME,contact.getName());
//         values.put(Contact.COLUMN_EMAIL,contact.getEmail());
//
//         return db.update(Contact.TABLE_NAME,values,Contact.COLUMN_ID+"=?",new String[]{String.valueOf(contact.getId())});
//
//     }
//
//     public void deleteContact(Contact contact){
//         SQLiteDatabase db=this.getWritableDatabase();
//         db.delete(Contact.TABLE_NAME,Contact.COLUMN_ID+"=?",new String[]{String.valueOf(contact.getId())});
//
//         db.close();
//     }
// }
