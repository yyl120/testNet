package com.example.testmybatis.insert.dao;


import com.example.testmybatis.insert.domain.KttranAuthority;
import com.example.testmybatis.insert.domain.KttranBusiness;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("AuthorityDao")
@Mapper
public interface AuthorityDao {

    List<KttranAuthority> selectAuthority(@Param("fGroupId") String fGroupId);

}
