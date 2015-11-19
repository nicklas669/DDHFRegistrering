package hyltofthansen.ddhfregistrering;


public class ItemDTO {
    private int postnummer;
    private String itemheadline, itemdescription, itemreceived,
            itemdatingfrom, itemdatingto, donator, producer;

    public ItemDTO(String itemheadline, String itemdescription,
                   String itemreceived, String itemdatingfrom, String itemdatingto,
                   String donator, String producer, int postnummer) {
        this.itemheadline = itemheadline;
        this.itemdescription = itemdescription;
        this.itemreceived = itemreceived;
        this.itemdatingfrom = itemdatingfrom;
        this.itemdatingto = itemdatingto;
        this.donator = donator;
        this.producer = producer;
        this.postnummer = postnummer;
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




    @Override
    public String toString() {
        //this.itemdatingto = itemdatingto;
        //this.donator = donator;
        //this.producer = producer;
        // this.postnummer = postnummer;
        return "itemheadline: "+itemheadline+", itemdescription: "+itemdescription+", itemreceived: "+itemreceived+", itemdatingfrom: "+itemdatingfrom;
    }
}
