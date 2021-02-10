package com.example.mybatis.demo.mapper;

import com.example.mybatis.demo.model.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressMapper {

    List<Address> findAll();

    Integer insertAddress(Address address);

}
