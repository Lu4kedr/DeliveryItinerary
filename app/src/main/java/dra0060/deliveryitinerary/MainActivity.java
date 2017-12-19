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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    final MediaPlayer mp = new MediaPlayer();
    public DeliveryItem tmp1 = null;
    public int selecteditem1 = -666;
    public int selecteditem2 = -666;
    public boolean reorder;
    public static DBHelper db = null;
    public CustomAdapter lwAdapter;

    private LocationManager locManag;
    private LocationListener locLis;
    public Location actualLoc;
    public ListView lw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        lw = (ListView) findViewById(R.id.lv_itinerary);
       /* DeliveryItem[] items = {
                new DeliveryItem("Name1","Note","Address",2.343,4.3434,0,1),
                new DeliveryItem("Name2","Note","Address",2.343,4.3434,2,2),
                new DeliveryItem("Name3","Note","Address",2.343,4.3434,1,2),
                new DeliveryItem("Name4","Note","Address",2.343,4.3434,1,1),
                new DeliveryItem("Name5","Note","Address",2.343,4.3434,2,1),

        };

        for (DeliveryItem item: items) {
            db.insertDelivery(item);
        }*/

     //  DeliveryItem itt= new DeliveryItem("Doma2","Luke","FM",49.680476,18.359500,0,1);
       // db.insertDelivery(itt);
        lwAdapter = new CustomAdapter(this, db.getAllDelivery());


        lw.setAdapter(lwAdapter);


        lw.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Toast.makeText(getApplicationContext(),((DeliveryItem) parent.getItemAtPosition(position)).name,Toast.LENGTH_SHORT).show();
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

                                RefreshList(db.getAllDelivery());


                            }

                        } else {
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
        locManag.requestLocationUpdates("gps", 5000, 10, locLis);


    }


    private void CheckPosition()
    {
        Location tmp;
        DeliveryItem[] arr = db.getAllDelivery();
        for (DeliveryItem di: arr) {
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

            }
            RefreshList(db.getAllDelivery());
        }
    }


    public void RefreshList(DeliveryItem[] items)
    {
        lwAdapter= new CustomAdapter(this, db.getAllDelivery());
        lw.setAdapter(lwAdapter);
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
            case R.id.AddItem:
                Intent intent = new Intent(getApplicationContext(),AddDeliveryItem.class);

                startActivity(intent);
                break;
            case R.id.menu_getloc:

                break;
        }
        return true;
    }
}
