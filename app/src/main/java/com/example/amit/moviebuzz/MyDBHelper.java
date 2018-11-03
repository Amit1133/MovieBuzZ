package com.example.amit.moviebuzz;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="Movie.db";
    private static final String TABLE_NAME="mymovie";
    private static final int DB_VERSION=2;
    private static final String ID="_id";
    private static final String IMDBID="imdbid";
    private static final String TITLE="title";
    private static final String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+IMDBID+" VARCHAR(12),"+TITLE+" VARCHAR(50) );";
    private static final String SELECT_ALL="SELECT * FROM "+TABLE_NAME+" ;";
    private static final String DROP="DROP TABLE IF EXISTS "+TABLE_NAME+" ;";

    private Context context;


    public MyDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_TABLE);
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
        db.execSQL(DROP);
        onCreate(db);
        }catch (Exception e){
            Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public long insertData(String imdbid,String title){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(IMDBID,imdbid);
        contentValues.put(TITLE,title);
        long rowid=sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        return rowid;
    }


    public ArrayList<BookmarkData> loadData(){
        ArrayList<BookmarkData> arrayList=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(SELECT_ALL,null);
        while(cursor.moveToNext()){
            int id=cursor.getInt(0);
            String imdbId=cursor.getString(1);
            String title=cursor.getString(2);
            BookmarkData bookmarkData=new BookmarkData(id,imdbId,title);

            arrayList.add(bookmarkData);
        }
        return arrayList;
    }

    public void dropTable(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL(DROP);
    }
    public void deleteTable(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,null,null);
        sqLiteDatabase.close();
    }
}
