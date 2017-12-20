package dra0060.deliveryitinerary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends Activity {

    final MediaPlayer mp = new MediaPlayer();
    public int selecteditem1 = -666;
    public int selecteditem2 = -666;
    public boolean reorder;
    public static DBHelper db = null;
    public CustomAdapter lwAdapter;
    private LocationManager locManag;
    private LocationListener locLis;
    public Location actualLoc;
    public ListView lw;

    // TODO Parts
    /*
    * 1 - GUI -lsitview, fragments(google maps), gestures
    * 2 - Concurrency - Thred - download csv file
    * 3 - Database - SQLLite
    * 4 - Multimedia - mp3 file play on DeliveryItem state change
    * 5 - Geo - GPS use + show DeliveryItem on Google map
    * 6 - Networking - download .csv from internet
    * */

    @Override
    public  void onResume()
    {
        super.onResume();

        RefreshList();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        lw = (ListView) findViewById(R.id.lv_itinerary);

        lwAdapter = new CustomAdapter(this, db.getAllDelivery());


        lw.setAdapter(lwAdapter);


        lw.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (reorder) {
                            if (selecteditem1 < 0) {
                                Toast.makeText(getApplicationContext(), "Selected: " + ((DeliveryItem) parent.getItemAtPosition(position)).name, Toast.LENGTH_SHORT).show();

                                selecteditem1 = ((DeliveryItem) parent.getItemAtPosition(position)).ID;
                            } else if (selecteditem2 < 0) {
                                Toast.makeText(getApplicationContext(), "Moved: " + ((DeliveryItem) parent.getItemAtPosition(position)).name, Toast.LENGTH_SHORT).show();

                                selecteditem2 = ((DeliveryItem) parent.getItemAtPosition(position)).ID;

                                db.switchItems(selecteditem1, selecteditem2);
                                selecteditem2 = -666;
                                selecteditem1 = -666;

                                RefreshList();


                            }

                        } else {
                            if(id==-1) //Visited
                            {
                                DeliveryItem di=((DeliveryItem) parent.getItemAtPosition(position));
                                di.state=1;
                                db.updateDelivery(di);
                                RefreshList();
                                return;
                            }
                            if(id==-2) // UnVisited
                            {
                                DeliveryItem di=((DeliveryItem) parent.getItemAtPosition(position));
                                di.state=2;
                                db.updateDelivery(di);
                                RefreshList();
                                return;
                            }

                            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                            intent.putExtra("lat", ((DeliveryItem) parent.getItemAtPosition(position)).gpsLat);
                            intent.putExtra("long", ((DeliveryItem) parent.getItemAtPosition(position)).gpsLong);

                            startActivity(intent);

                        }
                    }
                }

        );



        locManag = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locLis = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                actualLoc=location;
                    CheckPosition();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                },10);
            }
            return;
        }
        locManag.requestLocationUpdates("gps", 5000, 5, locLis);


    }


    private void CheckPosition()
    {
        Location tmp;
        DeliveryItem[] arr = db.getAllDelivery();
        DeliveryItem di;
        for(int i=0;i<arr.length;i++)
         {
            di=arr[i];
            tmp=new Location("tmp");
            tmp.setLatitude(di.gpsLat);
            tmp.setLongitude(di.gpsLong);
            int distance =(int) actualLoc.distanceTo(tmp);
            if(distance<15 && di.state==0 )
            {
                di.state=1;
                db.updateDelivery(di);

                if(mp.isPlaying())
                {
                    mp.stop();
                }

                try {
                    mp.reset();
                    AssetFileDescriptor afd;
                    afd = getAssets().openFd("notif.mp3");
                    mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                    mp.prepare();
                    mp.start();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(i>1) {
                    di = arr[i - 1];
                    if (di.state == 0) {
                        di.state = 2;
                        db.updateDelivery(di);
                        if (mp.isPlaying()) {
                            mp.stop();
                        }

                        try {
                            mp.reset();
                            AssetFileDescriptor afd;
                            afd = getAssets().openFd("missed.mp3");
                            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                            mp.prepare();
                            mp.start();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            RefreshList();
        }
    }


    public void RefreshList()
    {
        int posit = lw.getLastVisiblePosition();
        lwAdapter= new CustomAdapter(this, db.getAllDelivery());
        lw.setAdapter(lwAdapter);
        lw.setSelection(posit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_reorder:
                item.setChecked(!item.isChecked());
                reorder=item.isChecked();

                break;
            case R.id.menu_additem:
                Intent intent = new Intent(getApplicationContext(),AddDeliveryItem.class);

                startActivity(intent);
                break;
            case R.id.menu_reset:
                DeliveryItem[] arr = db.getAllDelivery();
                for (DeliveryItem it :arr ) {
                    it.state=0;
                    db.updateDelivery(it);
                }
                RefreshList();
                break;
            case R.id.menu_Delete:
                db.DeleteAll();
                RefreshList();
                break;
            case R.id.menu_Refresh:
                    RefreshList();
                break;
            case R.id.menu_csv2db:

                    LoadCSV();
                break;
            case R.id.menu_Settings:
                Intent intent1=new Intent(getBaseContext(),dra0060.deliveryitinerary.Settings.class);
                startActivity(intent1);
                break;
        }
        return true;
    }

    private  void LoadCSV()
    {
        if(dra0060.deliveryitinerary.Settings.CsvUrl==null){
            Toast.makeText(getApplicationContext(),"CSV url missing",Toast.LENGTH_SHORT).show();

            return;
        }
        Thread thr = new Thread(new Runnable(){

            public void run(){

                ReadWebCsv(dra0060.deliveryitinerary.Settings.CsvUrl,db);

            }
        });
        thr.start();

        try {
            thr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        db.getAllDelivery();
    }

    public void ReadWebCsv(String urlString, DBHelper db)
    {
        URLConnection feedUrl;
        ArrayList<DeliveryItem> result = new ArrayList<DeliveryItem>();
        DeliveryItem item;

        try {
            feedUrl = new URL(urlString).openConnection();
            InputStream is = feedUrl.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str=null;
            while ((str = reader.readLine()) != null) {
                String[] row =str.split(",");
                if(row.length!=8)continue;
                item= new DeliveryItem();
                item.ID=Integer.parseInt(row[0]);
                item.name=row[1];
                item.address=row[2];
                item.gpsLat=Double.parseDouble(row[3]);
                item.gpsLong=Double.parseDouble(row[4]);
                item.note=row[5];
                item.state=Integer.parseInt(row[6]);
                item.district=Integer.parseInt(row[7]);
                result.add(item);
            }
            is.close();
            for (DeliveryItem it:result) {
                db.insertDelivery(it);
            }

        } catch (Exception e) {
            Log.d("MyTag",e.toString());
        }
    }

}
