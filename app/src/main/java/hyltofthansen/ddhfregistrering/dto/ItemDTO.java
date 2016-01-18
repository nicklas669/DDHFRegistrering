package hyltofthansen.ddhfregistrering.dto;


import android.graphics.Bitmap;
import android.widget.BaseAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import hyltofthansen.ddhfregistrering.Singleton;
import hyltofthansen.ddhfregistrering.dao.DownloadImageTask;

public class ItemDTO implements Serializable {
    private int itemid, postnummer;
    private String itemheadline = "", itemdescription = "", itemreceived = "",
            itemdatingfrom = "", itemdatingto = "", donator = "", producer = "";
    private ArrayList<Bitmap> images = new ArrayList<Bitmap>();

    public ArrayList<String> getImageURLLists() {
        return imageURLLists;
    }

    private ArrayList<String> imageURLLists = new ArrayList<>();


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

    public ItemDTO(int itemid, String itemheadline, String defaultImageUrl, JSONArray allPics,
                   BaseAdapter listAdapter) {
        //Converting jsonarray to ArrayList for putExtra's pleasure
        if (allPics != null) {
            imageURLLists = new ArrayList(allPics.length());
            for (int i = 0; i < allPics.length(); i++) {
                try {
                    imageURLLists.add(allPics.getJSONObject(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        this.itemid = itemid;
        this.itemheadline = itemheadline;
        // Hvis der er et billede tilknytte genstanden
        if (defaultImageUrl != "null") {
            //Log.e("ItemDTO", itemid +" har image: "+defaultimage);
            Singleton.getInstance().fetchDefaultImage(defaultImageUrl, images, listAdapter);
//            new DownloadImageTask(images, listAdapter).fetchImages(defaultImageUrl);
        }
    }

    public ItemDTO(int itemid,
                   String itemheadline,
                   String itemdescription,
                   String itemreceived,
                   String itemdatingfrom,
                   String itemdatingto,
                   String donator,
                   String producer,
                   int postnummer,
                   JSONArray imageURLLists) {
        this.itemid = itemid;
        this.itemheadline = itemheadline;
        this.itemdescription = itemdescription;
        this.itemreceived = itemreceived;
        this.itemdatingfrom = itemdatingfrom;
        this.itemdatingto = itemdatingto;
        this.donator = donator;
        this.producer = producer;
        this.postnummer = postnummer;
//        this.imageURLLists = imageURLLists;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public String getDonator() {
        if (donator != null) return donator;
        return "";
    }

    public void setDonator(String donator) {
        this.donator = donator;
    }

    public String getItemdatingfrom() {
        if (itemdatingfrom != null) return itemdatingfrom;
        return "";
    }

    public void setItemdatingfrom(String itemdatingfrom) {
        this.itemdatingfrom = itemdatingfrom;
    }

    public String getItemdatingto() {
        if (itemdatingto != null) return itemdatingto;
        return "";
    }

    public void setItemdatingto(String itemdatingto) {
        this.itemdatingto = itemdatingto;
    }

    public String getItemdescription() {
        if (itemdescription != null) return itemdescription;
        return "";
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public String getItemheadline() {
        if (itemheadline != null) return itemheadline;
        return "";
    }

    public void setItemheadline(String itemheadline) {
        this.itemheadline = itemheadline;
    }

    public String getItemreceived() {
        if (itemreceived != null) return itemreceived;
        return "";
    }

    public void setItemreceived(String itemreceived) {
        this.itemreceived = itemreceived;
    }

    public String getProducer() {
        if (producer != null) return producer;
        return "";
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getpostnummer() {
        String temp = String.valueOf(postnummer);
        if (temp != null) return temp;
        return "";
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
