package dra0060.deliveryitinerary;

import android.app.Activity;
import android.os.Bundle;

public class AddDeliveryItem extends Activity {

    public DBHelper db; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_item);
    }
}
