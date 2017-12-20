package dra0060.deliveryitinerary;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity {

    public static String CsvUrl=null;
    private EditText etUrl;
    private Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        etUrl=(EditText)findViewById(R.id.et_url);
        btnSave=(Button)findViewById(R.id.btn_Save_Url) ;

        if(CsvUrl!=null)
            etUrl.setText(CsvUrl);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUrl.getText().length()>5)
                {
                    CsvUrl=etUrl.getText().toString();
                    finish();
                }
            }
        });
    }
}
