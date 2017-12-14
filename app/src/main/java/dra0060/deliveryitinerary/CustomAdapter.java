package dra0060.deliveryitinerary;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by lu4ke on 14.12.2017.
 */

class CustomAdapter extends ArrayAdapter<DeliveryItem> {

    CustomAdapter(Context context, DeliveryItem[] items)
    {
        super(context,R.layout.custom_row,items);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =LayoutInflater.from(getContext());
        View customView= inflater.inflate(R.layout.custom_row,parent,false);

        DeliveryItem oneItem=getItem(position);
        TextView item =(TextView) customView.findViewById(R.id.txt_Item);
        TextView address =(TextView) customView.findViewById(R.id.txt_address);
        TextView note =(TextView) customView.findViewById(R.id.txt_note);
        LinearLayout layout =(LinearLayout) customView.findViewById(R.id.layout);

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
        return customView;
        }
}
