package com.example.mybatis.demo.model;

import org.apache.ibatis.type.Alias;

@Alias("Address")
public class Address {

    private Integer addressId;
    private String city;
    private String detail;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", city='" + city + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
