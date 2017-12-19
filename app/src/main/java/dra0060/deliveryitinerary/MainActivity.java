package dra0060.deliveryitinerary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

    public DeliveryItem tmp1=null;
    public int selecteditem1=-666;
    public int selecteditem2=-666;
    public boolean reorder;
    public  DBHelper db=null;
    public CustomAdapter lwAdapter;

    public ListView lw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        lw =(ListView) findViewById(R.id.lv_itinerary);
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

        lwAdapter= new CustomAdapter(this, db.getAllDelivery());




        lw.setAdapter(lwAdapter);


        lw.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       // Toast.makeText(getApplicationContext(),((DeliveryItem) parent.getItemAtPosition(position)).name,Toast.LENGTH_SHORT).show();
                        if(reorder){
                            if(selecteditem1<0) {
                                Toast.makeText(getApplicationContext(),"Selected: "+((DeliveryItem) parent.getItemAtPosition(position)).name,Toast.LENGTH_SHORT).show();

                                selecteditem1 = ((DeliveryItem) parent.getItemAtPosition(position)).ID;
                            }
                            else if(selecteditem2<0)
                            {
                                Toast.makeText(getApplicationContext(),"Moved: "+((DeliveryItem) parent.getItemAtPosition(position)).name,Toast.LENGTH_SHORT).show();

                                selecteditem2=((DeliveryItem) parent.getItemAtPosition(position)).ID;

                                db.switchItems(selecteditem1,selecteditem2);
                                selecteditem2=-666;
                                selecteditem1=-666;

                                RefreshList(db.getAllDelivery());


                            }

                        }
                        else
                        {
                            Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                            intent.putExtra("lat",((DeliveryItem) parent.getItemAtPosition(position)).gpsLat);
                            intent.putExtra("long",((DeliveryItem) parent.getItemAtPosition(position)).gpsLong);

                            startActivity(intent);

                        }
                    }
                }
        );



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
        }
        return true;
    }
}
