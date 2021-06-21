package com.example.mybatis.demo.service;

import com.example.mybatis.demo.mapper.AddressMapper;
import com.example.mybatis.demo.mapper.UserMapper;
import com.example.mybatis.demo.model.Address;
import com.example.mybatis.demo.model.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class UserService {

    private final SqlSessionFactory sessionFactory;

    public UserService() throws IOException {
        this.sessionFactory = new SqlSessionFactoryBuilder()
                .build(Resources.getResourceAsStream("mybatis-config.xml"));
    }

    public User findUser(Integer id) {
        return operateWithMapper(UserMapper.class, userMapper -> userMapper.selectUser(id));
    }

    /**
     * 通过使用useGeneratedKeys，可以在数据插入以后把新生成的user_id放入传入的User对象中。
     *
     * @param user 传入的User对象，没有user_id
     * @return 拥有新生成主键的User对象
     */
    public User addUser(User user) {
        Integer inserts = operateWithMapper(UserMapper.class, userMapper -> userMapper.insertUser(user));
        assert inserts == 1;
        return user; // 在该user对象中，userId已经被赋值了
    }

    public Integer updateUser(Integer userId, User user) {
        return operateWithMapper(UserMapper.class, userMapper -> userMapper.updateUser(userId, user));
    }

    public Integer upsertUser(User user) {
        return operateWithMapper(UserMapper.class, userMapper -> userMapper.upsert(user));
    }

    public Integer batchUpsert(List<User> users) {
        return operateWithMapper(UserMapper.class, userMapper -> userMapper.batchUpsert(users));
    }

    public List<User> findUserByNameLike(String userName) {
        return operateWithMapper(UserMapper.class, userMapper -> userMapper.selectUserLikeName(userName));
    }

    public List<Address> findAllAddresses() {
        return operateWithMapper(AddressMapper.class, AddressMapper::findAll);
    }

    public Integer addAddress(Address address) {
        return operateWithMapper(AddressMapper.class, addressMapper -> addressMapper.insertAddress(address));
    }

    public List<Map<String, Object>> findUsersWithScoreLargerThan(double socre) {
        return operateWithMapper(
                UserMapper.class,
                userMapper -> userMapper.selectUserWithColumns("user_name,age", socre)
        );
    }

//    private <T> T operateWithUserMapper(Function<UserMapper, T> func) {
//        try (SqlSession session = sessionFactory.openSession(true)) {
//            final UserMapper userMapper = session.getMapper(UserMapper.class);
//            return func.apply(userMapper);
//        }
//    }
//
//    private <T> T operateWithAddressMapper(Function<AddressMapper, T> function) {
//        try (SqlSession session = sessionFactory.openSession(true)) {
//            final AddressMapper addressMapper = session.getMapper(AddressMapper.class);
//            return function.apply(addressMapper);
//        }
//    }

    private <T, M> T operateWithMapper(Class<M> mapperClass, Function<M, T> function) {
        try (SqlSession session = sessionFactory.openSession(true)) {
            M mapper = session.getMapper(mapperClass);
            return function.apply(mapper);
        }
    }

}
