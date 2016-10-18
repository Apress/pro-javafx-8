package projavafx.fxmlbasicfeatures;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FXMLBasicFeaturesBean {
    private String name;
    private String address;
    private boolean flag;
    private int count;
    private Color foreground;
    private Color background;
    private Double price;
    private Double discount;
    private List<Integer> sizes;
    private Map<String, Double> profits;
    private Long inventory;
    private List<String> products = new ArrayList<String>();
    private Map<String, String> abbreviations = new HashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public List<Integer> getSizes() {
        return sizes;
    }

    public void setSizes(List<Integer> sizes) {
        this.sizes = sizes;
    }

    public Map<String, Double> getProfits() {
        return profits;
    }

    public void setProfits(Map<String, Double> profits) {
        this.profits = profits;
    }

    public Long getInventory() {
        return inventory;
    }

    public void setInventory(Long inventory) {
        this.inventory = inventory;
    }

    public List<String> getProducts() {
        return products;
    }

    public Map<String, String> getAbbreviations() {
        return abbreviations;
    }

    @Override
    public String toString() {
        return "FXMLBasicFeaturesBean{" +
            "name='" + name + '\'' +
            ",\n\taddress='" + address + '\'' +
            ",\n\tflag=" + flag +
            ",\n\tcount=" + count +
            ",\n\tforeground=" + foreground +
            ",\n\tbackground=" + background +
            ",\n\tprice=" + price +
            ",\n\tdiscount=" + discount +
            ",\n\tsizes=" + sizes +
            ",\n\tprofits=" + profits +
            ",\n\tinventory=" + inventory +
            ",\n\tproducts=" + products +
            ",\n\tabbreviations=" + abbreviations +
            '}';
    }
}
