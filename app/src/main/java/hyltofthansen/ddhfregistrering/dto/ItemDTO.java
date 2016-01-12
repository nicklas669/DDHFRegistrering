package hyltofthansen.ddhfregistrering.dto;


import android.graphics.Bitmap;
import android.util.Log;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.dao.DownloadImageTask;

public class ItemDTO {
    private int itemid, postnummer;
    private String itemheadline, itemdescription, itemreceived,
            itemdatingfrom, itemdatingto, donator, producer;
    private ArrayList<Bitmap> images = new ArrayList<Bitmap>();


    public ItemDTO(int itemid, String itemheadline, String itemdescription,
                   String itemreceived, String itemdatingfrom, String itemdatingto,
                   String donator, String producer, int postnummer) {
        this.itemid = itemid;
        this.itemheadline = itemheadline;
        this.itemdescription = itemdescription;
        this.itemreceived = itemreceived;
        this.itemdatingfrom = itemdatingfrom;
        this.itemdatingto = itemdatingto;
        this.donator = donator;
        this.producer = producer;
        this.postnummer = postnummer;
    }

    // Constructor med item ID og billeder
    public ItemDTO(int itemid, String itemheadline, String itemdescription,
                   String itemreceived, String itemdatingfrom, String itemdatingto,
                   String donator, String producer, int postnummer, ArrayList<Bitmap> images) {
        this.itemid = itemid;
        this.itemheadline = itemheadline;
        this.itemdescription = itemdescription;
        this.itemreceived = itemreceived;
        this.itemdatingfrom = itemdatingfrom;
        this.itemdatingto = itemdatingto;
        this.donator = donator;
        this.producer = producer;
        this.postnummer = postnummer;
        this.images = images;
    }

    // Constructor uden Item ID men med billeder
    public ItemDTO(String itemheadline, String itemdescription,
                   String itemreceived, String itemdatingfrom, String itemdatingto,
                   String donator, String producer, int postnummer, ArrayList<Bitmap> images) {
        this.itemid = -1;
        this.itemheadline = itemheadline;
        this.itemdescription = itemdescription;
        this.itemreceived = itemreceived;
        this.itemdatingfrom = itemdatingfrom;
        this.itemdatingto = itemdatingto;
        this.donator = donator;
        this.producer = producer;
        this.postnummer = postnummer;
        this.images = images;
    }

    // Constructor uden item ID og billede
    public ItemDTO(String itemheadline, String itemdescription,
                   String itemreceived, String itemdatingfrom, String itemdatingto,
                   String donator, String producer, int postnummer) {
        this.itemid = -1;
        this.itemheadline = itemheadline;
        this.itemdescription = itemdescription;
        this.itemreceived = itemreceived;
        this.itemdatingfrom = itemdatingfrom;
        this.itemdatingto = itemdatingto;
        this.donator = donator;
        this.producer = producer;
        this.postnummer = postnummer;
    }

    public ItemDTO(int itemid, String itemheadline, String imgurl) {
        this.itemid = itemid;
        this.itemheadline = itemheadline;
        if (imgurl != "null") {
            //Log.e("ItemDTO", itemid +" har image: "+defaultimage);
            new DownloadImageTask(images).execute(imgurl);
        }
        //Log.e("ItemDTO", itemid +" har IKKE image!");
    }

    public ItemDTO(int itemid, String itemheadline, String imgurl, BaseAdapter listAdapter) {
        this.itemid = itemid;
        this.itemheadline = itemheadline;
        // Hvis der er et billede tilknytte genstanden
        if (imgurl != "null") {
            //Log.e("ItemDTO", itemid +" har image: "+defaultimage);
            new DownloadImageTask(images, listAdapter).execute(imgurl);
        }
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getDonator() {
        return donator;
    }

    public void setDonator(String donator) {
        this.donator = donator;
    }

    public String getItemdatingfrom() {
        return itemdatingfrom;
    }

    public void setItemdatingfrom(String itemdatingfrom) {
        this.itemdatingfrom = itemdatingfrom;
    }

    public String getItemdatingto() {
        return itemdatingto;
    }

    public void setItemdatingto(String itemdatingto) {
        this.itemdatingto = itemdatingto;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public String getItemheadline() {
        return itemheadline;
    }

    public void setItemheadline(String itemheadline) {
        this.itemheadline = itemheadline;
    }

    public String getItemreceived() {
        return itemreceived;
    }

    public void setItemreceived(String itemreceived) {
        this.itemreceived = itemreceived;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getpostnummer() {
        return postnummer;
    }

    public void setpostnummer(int postnummer) {
        this.postnummer = postnummer;
    }

    public Bitmap getImage(int index) {
        return images.get(index);
    }

    public int getImageArraySize() {
        return images.size();
    }




    @Override
    public String toString() {
        return "itemid: "+itemid+", itemheadline: "+itemheadline+", itemdescription: "+itemdescription+", itemreceived: "+itemreceived+", itemdatingfrom: "+itemdatingfrom
                +", itemdatingto: "+itemdatingto+", donator: "+donator+", producer: "+producer+", postnummer: "+postnummer;
    }
}
