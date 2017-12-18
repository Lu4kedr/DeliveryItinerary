package dra0060.deliveryitinerary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lu4ke on 18.12.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String ITINERARY_TABLE_NAME = "itinerary";
    public static final String ITINERARY_COLUMN_ID = "id";
    public static final String ITINERARY_COLUMN_NAME = "name";
    public static final String ITINERARY_COLUMN_ADDRESS = "address";
    public static final String ITINERARY_COLUMN_NOTE = "note";
    public static final String ITINERARY_COLUMN_GPSLAT = "gpslat";
    public static final String ITINERARY_COLUMN_GPSLONG= "gpslong";
    public static final String ITINERARY_COLUMN_DISTRICT= "district";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        //db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key autoincrement, name text,address text,note text, gpslat text,gpslong text,district integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertDelivery (String name,int district, String address, String note, double gpsLat,double gpsLong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("note", note);
        contentValues.put("gpslat", gpsLat);
        contentValues.put("gpslong", gpsLong);
        contentValues.put("district", district);
        db.insert("itinerary", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from itinerary where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ITINERARY_TABLE_NAME );
        return numRows;
    }

    public boolean updateDelivery(Integer id, String name,int district, String address, String note, double gpsLat,double gpsLong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("note", note);
        contentValues.put("gpslat", gpsLat);
        contentValues.put("gpslong", gpsLong);
        contentValues.put("district", district);
        db.update("itinerary", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteDelivery(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
