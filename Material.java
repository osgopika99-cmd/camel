package com.camel.kaoto.POJO;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

import java.util.UUID;

@CsvRecord(separator = ",", skipFirstLine = true)
public class Material {

    private String id = UUID.randomUUID().toString();
    ;

    @DataField(pos = 1, trim = true)
    private String material;

    @DataField(pos = 2, trim = true)
    private Integer quantity;

    @DataField(pos = 3, trim = true)
    private String color;

    public Material() {
    }

    public String getId() {
        return this.id;
    }

    public String getMaterial() {
        return this.material;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public String getColor() {
        return this.color;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String toString() {
        return "Material(id=" + this.getId() + ", material=" + this.getMaterial() + ", quantity=" + this.getQuantity() + ", color=" + this.getColor() + ")";
    }
}
