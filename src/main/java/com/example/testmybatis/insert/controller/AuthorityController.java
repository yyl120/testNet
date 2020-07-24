package com.example.testmybatis.insert.controller;

import com.example.testmybatis.BusTran.ServiceTrans;
import com.example.testmybatis.insert.dao.AuthorityDao;
import com.example.testmybatis.insert.dao.BusinessDao;
import com.example.testmybatis.insert.dao.UserInfoDao;
import com.example.testmybatis.insert.domain.KttranAuthority;
import com.example.testmybatis.insert.domain.KttranBusiness;
import com.example.testmybatis.testxmlparser.XmlBody;
import com.example.testmybatis.testxmlparser.XmlParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@Api(tags = "selectBusiness")
public class AuthorityController {
    @Autowired
    private AuthorityDao authorityDao;

    @Value("${fw.GroupId}")
    private String GroupId;

    @ApiOperation(value = "查询业务配置信息")
    @PostMapping("/selectAuthority")
    @ResponseBody
    public List<KttranAuthority> selectAuthority() {
        List<KttranAuthority> list = authorityDao.selectAuthority(GroupId);

        return list;

    }
}