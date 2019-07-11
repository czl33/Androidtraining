package com.newczl.androidtraining1.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.newczl.androidtraining1.DB.Bean.Star;

public class starDB {

    class MySQLiteOpenHelper extends SQLiteOpenHelper {

        private String createTable = "create table stars(id integer primary key autoincrement,"+
                "name varcher(200),url varcher(200),createP varcher(200))";


        public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context,name,factory,version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createTable);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion){
                case 1:
                    //db.execSQL(updateTable);
                    break;
                case 2:
                    //db.execSQl(updateTable2);
                    break;

            }
        }
    }

    private Context context;
    private String name;
    private SQLiteDatabase sqLiteDatabase;

    public starDB(Context context,String name){
        this.context=context;
        this.name=name;
    }

    public void open(){
        MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(context,name,null,1);
        sqLiteDatabase = mySQLiteOpenHelper.getReadableDatabase();
    }

    public void close(){
        if (sqLiteDatabase!=null){
            sqLiteDatabase.close();
        }
    }

    public long insert(Star star){
        ContentValues values = new ContentValues();
        values.put("name",star.getName());
        values.put("url",star.getUrl());
        values.put("createP",star.getCreateP());
        return sqLiteDatabase.insert("stars",null,values);
    }

    public Cursor queryAll(){
        return sqLiteDatabase.query("stars",null,null,
                null,null,null,null);
    }

    public Cursor queryByID(int id){
        return sqLiteDatabase.query("stars",null,"id=?",
                new String[]{id+""},null,null,null);
    }

    public Cursor queryByCP(String cp){
        return sqLiteDatabase.query("stars",null,"createP=?",
                new String[]{cp},null,null,null);
    }

    public int deleteByID(int id){
        return sqLiteDatabase.delete("stars","id="+id,null);
    }

//    public int updateByID(int id,People people){
////        ContentValues values=new ContentValues();
////        values.put("name",people.getName());
////        values.put("age",people.getAge());
////        values.put("height",people.getHeight());
////        return sqLiteDatabase.update("people",values,"id+?",
////                new String[]{id+""});
////    }
}
