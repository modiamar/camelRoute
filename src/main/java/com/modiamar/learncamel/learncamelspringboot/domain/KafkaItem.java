package com.modiamar.learncamel.learncamelspringboot.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class KafkaItem {

    private String transactionType;
    private String sku;
    private String itemDescription;
    private BigDecimal price;

    public KafkaItem(String transactionType, String sku, String itemDescription, BigDecimal price) {
        this.transactionType = transactionType;
        this.sku = sku;
        this.itemDescription = itemDescription;
        this.price = price;
    }

    public KafkaItem (){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KafkaItem kafkaItem = (KafkaItem) o;
        return Objects.equals(transactionType, kafkaItem.transactionType) &&
                Objects.equals(sku, kafkaItem.sku) &&
                Objects.equals(itemDescription, kafkaItem.itemDescription) &&
                Objects.equals(price, kafkaItem.price);
    }

    @Override
    public int hashCode() {

        return Objects.hash(transactionType, sku, itemDescription, price);
    }

    @Override
    public String toString() {
        return "KafkaItem{" +
                "transactionType='" + transactionType + '\'' +
                ", sku='" + sku + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", price=" + price +
                '}';
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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
}
