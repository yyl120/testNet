package com.example.testmybatis.insert.controller;

import com.example.testmybatis.BusTran.ServiceTrans;
import com.example.testmybatis.cofig.CustomException;
import com.example.testmybatis.insert.dao.BusinessDao;
import com.example.testmybatis.insert.dao.UserInfoDao;
import com.example.testmybatis.insert.domain.*;
import com.example.testmybatis.testxmlparser.XmlBody;
import com.example.testmybatis.testxmlparser.XmlParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
@Api(tags = "selectBusiness")
public class BussinessController {
    @Autowired
   private UserInfoDao userInfos;

    @Autowired
   private BusinessDao businessDao;

    @Resource
    private XmlParser xmlParser;

    @Resource
    private XmlBody body;

    @Resource
    private ServiceTrans serviceTrans ;

    @Value("${fw.GroupId}")
    private String GroupId;

   @ApiOperation(value = "查询业务配置信息")
    @PostMapping("/selectBusiness")
    @ResponseBody
    public List<KttranBusiness> selectBusiness()  {
     List<KttranBusiness> list=businessDao.selectBusiness(GroupId);

     return list;

    }
    @ApiOperation(value = "查询所有业务配置信息")
    @PostMapping("/selectAllBusiness")
    @ResponseBody
    public List<KttranBusiness> selectAllBusiness()  {
        List<KttranBusiness> list=businessDao.selectAllBusiness();

        return list;

    }

    @ApiOperation(value = "查询业务属性配置信息")
    @PostMapping("/selectBusinessField")
    @ResponseBody
    public List<KttranBussinessField> selectBusinessField()  {
        List<KttranBussinessField> list=businessDao.selectBusinessField(GroupId);

        return list;

    }

    @ApiOperation(value = "业务参数值域设置")
    @PostMapping("/selectBusinessFieldLmtRang")
    @ResponseBody
    public List<KttranBussinessFieldLmtRang> selectBusinessFieldLmtRang()  {
        List<KttranBussinessFieldLmtRang> list=businessDao.selectBusinessFieldLmtRang(GroupId);

        return list;

    }
    @ApiOperation(value = "业务属性值配置表")
    @PostMapping("/selectBussinessFieldValue")
    @ResponseBody
    public List<KttranBussinessFieldValue> selectBussinessFieldValue()  {
        List<KttranBussinessFieldValue> list=businessDao.selectBussinessFieldValue(GroupId);

        return list;

    }

    /*@ApiOperation(value = "保存用户信息")
    @PostMapping("/saveUser")
    @ResponseBody
    public String mdllist(@RequestParam(value = "xml") String xml) throws CustomException, IOException, ParseException {
        if(StringUtils.isBlank(xml)||StringUtils.isEmpty(xml)){
            throw new CustomException("xml不为空");
        }


        try {
            serviceTrans.Process(xml);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
       *//* body=xmlParser.XmlDeserialize(xml);
        System.out.println("1"+body);*//*
//<userid>yyl</userid>

      *//*  System.out.println(body.getHead().getUserID());
        System.out.println( body.getHead().getPassword());

        String str=body.getHead().getUserID();
*//*
       *//* str=str.substring(8);
        str=str.substring(0,str.length()-9);
        String str1=body.getHead().getPassword();
        str1=str1.substring(10);
        str1=str1.substring(0,str1.length()-11);
        System.out.println("str"+str);
        System.out.println("str1"+str1);*//*
       // userInfos.insert(str, str1);
        return serviceTrans.Process(xml);
    }*/
}
