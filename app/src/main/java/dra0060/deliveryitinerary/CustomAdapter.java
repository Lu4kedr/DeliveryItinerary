package dra0060.deliveryitinerary;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by lu4ke on 14.12.2017.
 */

class CustomAdapter extends ArrayAdapter<DeliveryItem> {


    DeliveryItem oneItem;
    int pos;
    Context context;
    MainActivity ma;


    CustomAdapter(Context context, DeliveryItem[] items)
    {
        super(context,R.layout.custom_row,items);
        ma=(MainActivity) context;
        this.context=context;


    }



    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View customView = convertView;

        if( customView==null)
        {
            customView=LayoutInflater.from(context).inflate(R.layout.custom_row,parent,false);
        }
        pos=position;
        oneItem=getItem(position);
        TextView item =(TextView) customView.findViewById(R.id.txt_Item);
        TextView address =(TextView) customView.findViewById(R.id.txt_address);
        TextView note =(TextView) customView.findViewById(R.id.txt_note);
        LinearLayout layout =(LinearLayout) customView.findViewById(R.id.layout);
        Button visited = (Button) customView.findViewById(R.id.btn_visited);

        Button unVisited = (Button) customView.findViewById(R.id.btn_unVisited);

        item.setText(oneItem.name);
        address.setText(oneItem.address);
        note.setText(oneItem.note);


        switch (oneItem.state)
        {
            case 0:
                layout.setBackgroundColor(Color.BLUE);
                break;
            case 1:
                layout.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                layout.setBackgroundColor(Color.RED);
                break;
        }
        layout.getBackground().setAlpha(20);


        layout.setOnTouchListener(new OnSwipeTouchListener(context)
        {
            @Override
            public void onSwipeLeft() {
               // Toast.makeText(getContext(),"TEST",Toast.LENGTH_SHORT).show();
                ((ListView)parent).performItemClick(null,position,-2);
            }
            @Override
            public  void  onSwipeRight(){
                ((ListView)parent).performItemClick(null,position,-1);
            }
            public void onTouch()
            {
                ((ListView)parent).performItemClick(null,position,oneItem.ID);
            }
        });


        visited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView)parent).performItemClick(v,position,-1);
            }
        });

        unVisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListView)parent).performItemClick(v,position,-2);
            }
        });
        return customView;
        }


}
