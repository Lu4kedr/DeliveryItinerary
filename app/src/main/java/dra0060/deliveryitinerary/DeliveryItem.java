package dra0060.deliveryitinerary;

/**
 * Created by lu4ke on 14.12.2017.
 */

public class DeliveryItem {

    public String name;
    public String address;
    public Double gps1, gps2;
    public String note;
    public int state; //0-no visit, 1 - visited, 2- missed

    public DeliveryItem(){

    }

    public DeliveryItem(String Name, String Address, Double Gps1, Double Gps2 )
    {
        state=0;
        name=Name;
        address=Address;
        gps1=Gps1;
        gps2=Gps2;
    }

    public DeliveryItem(String Name, String Address, Double Gps1, Double Gps2, int State )
    {
        state=State;
        name=Name;
        address=Address;
        gps1=Gps1;
        gps2=Gps2;
    }

}
