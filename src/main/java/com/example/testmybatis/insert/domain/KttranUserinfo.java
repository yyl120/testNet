package com.example.testmybatis.insert.domain;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 用户信息表实体
 */
@Data
public class KttranUserinfo
{

    private BigDecimal fUrId = null; //用户序号
    private String fOrgCode; //机构代码
    private String fUrCode; //登录账号
    private String fUrPassword; //登录密码
    private String fUrName; //用户名
    private LocalDateTime fUrIntime = null; //创建时间
    private String fUseStatus; //使用状态,（y启动/n禁用）
    private String fUrMemo; //备注
    private String fUrMac; //Mac地址
    private String isTest; //测试用户标志
    private String UR_USESTATUS;
    private String UR_PARENT;
    private String UR_PERSON;
    private String UR_PHONE;
    private String UR_ID2;
    private String ISTEST;
    private String IS_LOG;

}
