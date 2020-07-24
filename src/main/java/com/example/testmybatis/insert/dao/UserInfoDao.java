package com.example.testmybatis.insert.dao;

import com.example.testmybatis.insert.domain.KttranBussinessFieldValue;
import com.example.testmybatis.insert.domain.KttranUserinfo;
import com.example.testmybatis.insert.domain.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("UserInfoDao")
@Mapper
public interface UserInfoDao {

    public void insert(
            @Param("UR_CODE") String UR_CODE,
                       @Param("UR_PASSWORD") String UR_PASSWORD
                      );

    List<KttranUserinfo> selectUserinfo();
   List<String> findFieldByTableName(@Param("TableName") String TableName);
   public void insertFieldByTableName(@Param("TableNames") String TableNames,
   @Param("id") String id, @Param("name") String name, @Param("bir") String bir);

    public void updateFieldByTableName(@Param("TableNames") String TableNames,
                                       @Param("name") String name,
                                       @Param("id") String id);

    public String selectPK(@Param("BS_CODE") String BS_CODE);
    public Test selectTable(@Param("ID") String ID,
                                  @Param("NAME") String NAME);
    public void saveTable(@Param("ID") String ID,
                          @Param("NAME") String NAME,
                          @Param("BIR") String BIR);


}
