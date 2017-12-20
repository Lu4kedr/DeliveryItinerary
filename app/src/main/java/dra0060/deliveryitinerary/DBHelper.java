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
    public static final String ITINERARY_COLUMN_STATE= "state";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // db.execSQL("DROP TABLE IF EXISTS contacts");
        db.execSQL(
                "create table itinerary " +
                        "(id integer primary key autoincrement, name text,address text,note text, gpslat double,gpslong double,district integer, state integer)"
        );
    }
    public void DeleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS itinerary");
        db.execSQL(
                "create table itinerary " +
                        "(id integer primary key autoincrement, name text,address text,note text, gpslat double,gpslong double,district integer, state integer)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS itinerary");
        onCreate(db);
    }



    public boolean insertDelivery (DeliveryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if(item.ID>0)
            contentValues.put("id", item.ID);
        contentValues.put("name", item.name);
        contentValues.put("address", item.address);
        contentValues.put("note", item.note);
        contentValues.put("gpslat", item.gpsLat);
        contentValues.put("gpslong", item.gpsLong);
        contentValues.put("district", item.district);
        contentValues.put("state", item.state);
        db.insert("itinerary", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from itinerary where id="+id+"", null );
        return res;
    }
    public DeliveryItem getItem(int ItemID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from itinerary where id="+ItemID, null );
        DeliveryItem di =null;
        if(res.moveToFirst()) {
            di=new DeliveryItem();
            di.note = res.getString(res.getColumnIndex(ITINERARY_COLUMN_NOTE));
            di.state = res.getInt(res.getColumnIndex(ITINERARY_COLUMN_STATE));
            di.gpsLong = res.getDouble(res.getColumnIndex(ITINERARY_COLUMN_GPSLONG));
            di.gpsLat = res.getDouble(res.getColumnIndex(ITINERARY_COLUMN_GPSLAT));
            di.address = res.getString(res.getColumnIndex(ITINERARY_COLUMN_ADDRESS));
            di.district = res.getInt(res.getColumnIndex(ITINERARY_COLUMN_DISTRICT));
            di.name = res.getString(res.getColumnIndex(ITINERARY_COLUMN_NAME));
            di.ID = res.getInt(res.getColumnIndex(ITINERARY_COLUMN_ID));
        }
        return di;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, ITINERARY_TABLE_NAME );
        return numRows;
    }

    public boolean updateDelivery(Integer id, String name,int district,int state, String address, String note, double gpsLat,double gpsLong) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("note", note);
        contentValues.put("gpslat", gpsLat);
        contentValues.put("gpslong", gpsLong);
        contentValues.put("district", district);
        contentValues.put("state", state);
        db.update("itinerary", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateDelivery(DeliveryItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", item.name);
        contentValues.put("address", item.address);
        contentValues.put("note", item.note);
        contentValues.put("gpslat", item.gpsLat);
        contentValues.put("gpslong", item.gpsLong);
        contentValues.put("district", item.district);
        contentValues.put("state", item.state);
        db.update("itinerary", contentValues, "id = ? ", new String[] { Integer.toString(item.ID) } );
        return true;
    }

    public Integer deleteDelivery(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("itinerary",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public DeliveryItem[] getAllDelivery() {
        ArrayList<DeliveryItem> array_list = new ArrayList<DeliveryItem>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from itinerary order by id", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            DeliveryItem di = new DeliveryItem();
            di.note=res.getString(res.getColumnIndex(ITINERARY_COLUMN_NOTE));
            di.state=res.getInt(res.getColumnIndex(ITINERARY_COLUMN_STATE));
            di.gpsLong=res.getDouble(res.getColumnIndex(ITINERARY_COLUMN_GPSLONG));
            di.gpsLat=res.getDouble(res.getColumnIndex(ITINERARY_COLUMN_GPSLAT));
            di.address=res.getString(res.getColumnIndex(ITINERARY_COLUMN_ADDRESS));
            di.name=res.getString(res.getColumnIndex(ITINERARY_COLUMN_NAME));
            di.ID=res.getInt(res.getColumnIndex(ITINERARY_COLUMN_ID));
            di.district=res.getInt(res.getColumnIndex(ITINERARY_COLUMN_DISTRICT));

            // TODO load fromDB
            // TODO list switch items


            array_list.add(di);
            res.moveToNext();
        }
        res.close();
        return array_list.toArray(new DeliveryItem[array_list.size()]);
    }

    public void switchItems(int id1,int id2)
    {
        int idtmp1,idtmp2;
        DeliveryItem tmp1= this.getItem(id1);
        DeliveryItem tmp2= this.getItem(id2);

        idtmp1=tmp1.ID;
        idtmp2=tmp2.ID;

        tmp1.ID=idtmp2;
        tmp2.ID=idtmp1;

        this.deleteDelivery(id1);
        this.deleteDelivery(id2);

        this.insertDelivery(tmp1);
        this.insertDelivery(tmp2);

    }

}
