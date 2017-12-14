package dra0060.deliveryitinerary;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lw =(ListView) findViewById(R.id.lv_itinerary);
        DeliveryItem[] items = {
                new DeliveryItem("Item1","Address1",0.33333,1.3333,0),
                new DeliveryItem("Item2","Address2",0.33333,1.3333,1),
                new DeliveryItem("Item3","Address3",0.33333,1.3333,2),
                new DeliveryItem("Item2","Address2",0.33333,1.3333,1),
                new DeliveryItem("Item3","Address3",0.33333,1.3333,2),
                new DeliveryItem("Item4","Address4",0.33333,1.3333,0)
        };

        ListAdapter lwAdapter= new CustomAdapter(this, items);
        lw.setAdapter(lwAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
}
