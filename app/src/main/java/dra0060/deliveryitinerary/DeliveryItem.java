package dra0060.deliveryitinerary;

/**
 * Created by lu4ke on 14.12.2017.
 */

public class DeliveryItem {


    public int ID=-666;
    public String name;
    public String address;
    public Double gpsLat, gpsLong;
    public String note;
    public int state; //0-no visit, 1 - visited, 2- missed
    public int district;

    public DeliveryItem(){

    }

    public DeliveryItem(int id, String Name,String note, String Address, Double Gps1, Double Gps2, int State, int district )
    {
        ID=id;
        state=State;
        name=Name;
        address=Address;
        gpsLat=Gps1;
        gpsLong=Gps2;
        this.note=note;
        this.district=district;
    }
    public DeliveryItem(String Name,String note, String Address, Double Gps1, Double Gps2, int State, int district )
    {

        state=State;
        name=Name;
        address=Address;
        gpsLat=Gps1;
        gpsLong=Gps2;
        this.note=note;
        this.district=district;
    }

    public DeliveryItem(String Name, String Address, Double Gps1, Double Gps2 )
    {
        state=0;
        name=Name;
        address=Address;
        gpsLat=Gps1;
        gpsLong=Gps2;
    }

    public DeliveryItem(String Name, String Address, Double Gps1, Double Gps2, int State )
    {

        state=State;
        name=Name;
        address=Address;
        gpsLat=Gps1;
        gpsLong=Gps2;
    }

}
