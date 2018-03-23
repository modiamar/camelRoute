package com.modiamar.learncamel.learncamelspringboot.domain;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.math.BigDecimal;

@CsvRecord(separator = ",",skipFirstLine = true)
public class Item {
    //    type,sku#,itemdescription,price
    @DataField(pos = 1)
    private String type;
    @DataField(pos = 2)
    private String skuNumber;
    @DataField(pos = 3)
    private String itemDescription;
    @DataField(pos = 4, precision = 2)
    private BigDecimal price;

    public Item(String type, String skuNumber, String itemDescription, BigDecimal price) {
        this.type = type;
        this.skuNumber = skuNumber;
        this.itemDescription = itemDescription;
        this.price = price;
    }

    public Item(){}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "type='" + type + '\'' +
                ", skuNumber=" + skuNumber +
                ", itemDescription='" + itemDescription + '\'' +
                ", price=" + price +
                '}';
    }
}
