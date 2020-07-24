package com.example.testmybatis.insert.dao;

import com.example.testmybatis.Domain.KttranBusinessEx;
import com.example.testmybatis.Domain.KttranBussinessFieldEx;
import com.example.testmybatis.Domain.KttranServiceconfigEx;
import com.example.testmybatis.insert.domain.KttranBusiness;
import com.example.testmybatis.insert.domain.KttranBussinessField;
import com.example.testmybatis.insert.domain.KttranBussinessFieldLmtRang;
import com.example.testmybatis.insert.domain.KttranBussinessFieldValue;
import com.example.testmybatis.testxmlparser.XmlBody;
import com.github.abel533.sql.SqlMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("BusinessDao")
@Mapper
public interface BusinessDao {
    //业务配置表实体
     List<KttranBusiness> selectBusiness(@Param("GroupId") String GroupId);


    List<KttranBusiness> selectAllBusiness();

    //业务属性配置表实体
     List<KttranBussinessField> selectBusinessField(@Param("fGroupId") String fGroupId);

     //业务参数值域设置
     List<KttranBussinessFieldLmtRang> selectBusinessFieldLmtRang(@Param("fGroupId") String fGroupId);

     //业务属性值配置表
    List<KttranBussinessFieldValue> selectBussinessFieldValue(@Param("fGroupId") String fGroupId);
    //public boolean CreateBusObjectData(SqlMapper mapper, List<KttranBusinessEx> buss, XmlBody body, KttranServiceconfigEx svr);


    }

