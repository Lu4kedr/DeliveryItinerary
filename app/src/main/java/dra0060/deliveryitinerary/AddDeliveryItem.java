package dra0060.deliveryitinerary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDeliveryItem extends Activity {

    public DBHelper db;
    private Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_item);
        add= (Button)findViewById(R.id.btn_AddItem);
        if(MainActivity.db!=null)
            db=MainActivity.db;
        else
            db=new DBHelper(getApplicationContext());


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryItem nit= new DeliveryItem();

                nit.state=Integer.parseInt((((EditText)findViewById(R.id.tx_State)).getText().toString()));
                nit.district=Integer.parseInt((((EditText)findViewById(R.id.tx_District)).getText().toString()));
                nit.gpsLat=Double.parseDouble((((EditText)findViewById(R.id.tx_GPSLat)).getText().toString()));
                nit.gpsLong=Double.parseDouble((((EditText)findViewById(R.id.tx_GPSLong)).getText().toString()));
                nit.name=(((EditText)findViewById(R.id.tx_name)).getText().toString());
                nit.address=(((EditText)findViewById(R.id.tx_Address)).getText().toString());
                nit.note=(((EditText)findViewById(R.id.tx_Note)).getText().toString());

                db.insertDelivery(nit);
                finish();
            }
        });
    }
}
