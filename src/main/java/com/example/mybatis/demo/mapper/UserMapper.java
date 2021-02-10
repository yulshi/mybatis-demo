package com.example.mybatis.demo.mapper;

import com.example.mybatis.demo.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.Alias;

import java.util.List;
import java.util.Map;

@Alias("UserMapper")
public interface UserMapper {

    /**
     * 因为在UserMapper.xml文件中已经有selectUser的定义，所以这里的@Select注解需要被注释掉，不然会报错
     */
    //@Select("select user_id, user_name, age from user where user_id=#{userId}")
    User selectUser(Integer userId);

    /**
     * 在对应的User Mapper.xml文件中定义了对应的SQL语句，并且通过使用useGeneratedKeys，在传入的User
     * 对象中，会在插入完成以后把新生成的user_id赋值给userId
     */
    // 如果希望使用注解的方式获取主键的值，可以使用@SelectKey注解进行操作
    //    @SelectKey(
    //            statement = "SELECT LAST_INSERT_ID()",
    //            keyColumn = "user_id",
    //            keyProperty = "userId",
    //            before = false,
    //            resultType = Integer.class
    //    )
    //    @Insert({
    //            "INSERT INTO user(user_name, age) VALUES",
    //            "(#{userName}, #{age})"
    //    })
    Integer insertUser(User user);

    // 下面这种方式，如果传入的字段值为null，就会把数据库中的字段设置为null，如果想不更新null的字段，则需要使用<if>来进行判断，
    // 请参考UserMapper.xml文件中的用法
    //    @Update({
    //            "UPDATE user SET user_name = #{user.userName},",
    //            "age = #{user.age}, score = #{user.score}",
    //            "WHERE user_id=#{userId}"
    //    })
    Integer updateUser(@Param("userId") Integer userId, @Param("user") User user);

    // 在执行upsert的时候，与update一样，如果想忽略值为null的传入字段，则需要使用<if>进行处理
    @Insert({"<script>",
            "INSERT INTO user(user_id, user_name, age, score)",
            "   VALUES(#{userId}, #{userName}, #{age}, #{score})",
            "ON DUPLICATE KEY UPDATE",
            "<trim suffixOverrides=','>",
            "   <if test='userName != null'>user_name=#{userName},</if>",
            "   <if test='age != null'>age=#{age},</if>",
            "   <if test='score != null'>score=#{score},</if>",
            "</trim>",
            "</script>"})
    Integer upsert(User user);

    /**
     * 在MySQL中使用如下的SQL语句可以达到批量upsert的功能，在PG中使用另外的一种SQL，下面的方式
     * 并不能忽略null值的传入字段，请参考UserMapper.xml文件
     */
    //    @Insert({
    //            "<script>",
    //            "  INSERT INTO user(user_id, user_name, age, score) VALUES",
    //            "  <foreach item='user' collection='users' separator=','>",
    //            "    (#{user.userId}, #{user.userName}, #{user.age}, #{user.score})",
    //            "  </foreach>",
    //            "  ON DUPLICATE KEY UPDATE",
    //            "    user_name=VALUES(user_name), age=VALUES(age), score=VALUES(score)",
    //            "</script>"
    //    })
    Integer batchUpsert(@Param("users") List<User> users);

    /**
     * 使用自定义的查询列表进行查询
     *
     * @param columns 逗号分割的列名
     * @param score   分数
     * @return 符合查询条件的数据，以Map的方式展示
     */
    @Results({
            @Result(column = "user_name", property = "userName", javaType = String.class),
            @Result(column = "age", property = "age", javaType = Integer.class),
            @Result(column = "score", property = "score", javaType = Double.class)
    })
    @Select({
            "SELECT ${columns} FROM user",
            "WHERE score > #{score}"
    })
    List<Map<String, Object>> selectUserWithColumns(@Param("columns") String columns, @Param("score") double score);

    /**
     * 对于使用 LIKE 关键字的模糊查询，可以使用<bind/>来构造pattern
     */
    @Select({"<script>",
            "<bind name='pattern' value=\"'_' + _parameter + '%'\"/>",
            "SELECT * FROM user WHERE user_name LIKE #{pattern}",
            "</script>"})
    List<User> selectUserLikeName(String userName);

}
