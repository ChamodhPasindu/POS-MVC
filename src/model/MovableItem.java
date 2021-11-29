package model;

public class MovableItem {
    private String itemCode;
    private int count;

    public MovableItem() {
    }

    public MovableItem(String itemCode, int count) {
        this.itemCode = itemCode;
        this.count = count;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
