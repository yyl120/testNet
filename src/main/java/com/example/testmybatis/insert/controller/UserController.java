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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
@Api(tags = "UserInfo")
public class UserController {
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


   @ApiOperation(value = "查询用户信息")
    @PostMapping("/selectUserinfo")
    @ResponseBody
    public List<KttranUserinfo> selectUserinfo()  {
     List<KttranUserinfo> list=userInfos.selectUserinfo();

     return list;

    }

    @ApiOperation(value = "JSON输入")
    @PostMapping("/inputJSON")
    @ResponseBody
    public String inputJSON(@RequestParam(value = "json") String json) throws CustomException, IOException, ParseException {
        if (StringUtils.isBlank(json) || StringUtils.isEmpty(json)) {
            throw new CustomException("json不为空");

        }
        return serviceTrans.ProcessJson(json);

    }



    @ApiOperation(value = "XML输入")
    @PostMapping("/inputXML")
    @ResponseBody
    public String inputXML(@RequestParam(value = "xml") String xml) throws CustomException, IOException, ParseException {
        if(StringUtils.isBlank(xml)||StringUtils.isEmpty(xml)){
            throw new CustomException("xml不为空");
        }


       /* try {
            serviceTrans.Process(xml);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }*/
       /* body=xmlParser.XmlDeserialize(xml);
        System.out.println("1"+body);*/
//<userid>yyl</userid>

      /*  System.out.println(body.getHead().getUserID());
        System.out.println( body.getHead().getPassword());

        String str=body.getHead().getUserID();
*/
       /* str=str.substring(8);
        str=str.substring(0,str.length()-9);
        String str1=body.getHead().getPassword();
        str1=str1.substring(10);
        str1=str1.substring(0,str1.length()-11);
        System.out.println("str"+str);
        System.out.println("str1"+str1);*/
       // userInfos.insert(str, str1);
        return serviceTrans.Process(xml);
    }
}
