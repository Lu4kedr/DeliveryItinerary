package dra0060.deliveryitinerary;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    public DeliveryItem tmp1=null;
    public int tmp1_pos;
    public boolean reorder;

    public DeliveryItem[] items=null;
    public ListView lw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lw =(ListView) findViewById(R.id.lv_itinerary);
        DeliveryItem[] itemst = {
                new DeliveryItem("Item1","Address1",0.33333,1.3333,0),
                new DeliveryItem("Item2","Address2",0.33333,1.3333,1),
                new DeliveryItem("Item3","Address3",0.33333,1.3333,2),
                new DeliveryItem("Item4","Address2",0.33333,1.3333,1),
                new DeliveryItem("Item5","Address3",0.33333,1.3333,2),
                new DeliveryItem("Item6","Address4",0.33333,1.3333,0)
        };
        items=itemst;

        ListAdapter lwAdapter= new CustomAdapter(this, items);

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
