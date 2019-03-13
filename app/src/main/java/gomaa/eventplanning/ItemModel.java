package gomaa.eventplanning;

public class ItemModel {

    private String itemName ;
    private String imgUrl ;
    private String itemSalary;
    private String itemDescribtion;
    private String mKey;



    public ItemModel(String itemName, String imgUrl, String itemSalary, String itemDescribtion) {

        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.itemSalary = itemSalary;
        this.itemDescribtion = itemDescribtion;
    }

    public ItemModel()
    {
    }

    /*public ItemModel(String itemName,String imgUrl , String itemSalary) {
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.itemSalary = itemSalary;
    }*/

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getItemSalary() {
        return itemSalary;
    }

    public void setItemSalary(String itemSalary) {
        this.itemSalary = itemSalary;
    }

    public String getItemDescribtion() {
        return itemDescribtion;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public void setItemDescribtion(String itemDescribtion) {
        this.itemDescribtion = itemDescribtion;
    }
}
