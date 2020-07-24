package com.example.testmybatis.insert.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 业务参数值域设置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KttranBussinessFieldLmtRang
{

    private String fBsCode; //业务编号
    private String fBsfCode; //属性编号
    private BigDecimal fBsfvSn = null; //当前序号
    private String fBsfvLmt; //取值方法(1XML绝对路径/2XML相对路径/3函数/4SQL语句/5存储过程/6其他表字段)
    private String fBsfvCount; //取值语句
    private String fBsfvActive; //当前状态,y激活/n关闭
    private String fBsfvErr; //验证失败后的错误提示
    private String fGroupId; //业务分离编号  add by mhh 20160411
    /**
    /* 业务编号
     *//*
    public final void setBsCode(String value)
    {
        fBsCode = value;
    }
    public final String getBsCode()
    {
        return fBsCode;
    }
    *//**
     属性编号
     *//*
    public final void setBsfCode(String value)
    {
        fBsfCode = value;
    }
    public final String getBsfCode()
    {
        return fBsfCode;
    }
    *//**
     当前序号
     *//*
    public final void setBsfvSn(BigDecimal value)
    {
        fBsfvSn = value;
    }
    public final BigDecimal getBsfvSn()
    {
        return fBsfvSn;
    }
    *//**
     取值方法(1指定值/2词典表/3正则表达式)
     *//*
    public final void setBsfvLmt(String value)
    {
        fBsfvLmt = value;
    }
    public final String getBsfvLmt()
    {
        return fBsfvLmt;
    }
    *//**
     取值语句
     *//*
    public final void setBsfvCount(String value)
    {
        fBsfvCount = value;
    }
    public final String getBsfvCount()
    {
        return fBsfvCount;
    }
    *//**
     当前状态,Y激活/N关闭
     *//*
    public final void setBsfvActive(String value)
    {
        fBsfvActive = value;
    }
    public final String getBsfvActive()
    {
        return fBsfvActive;
    }
    *//**
     验证失败后错误提示
     *//*
    public final void setBsfvErr(String value)
    {
        fBsfvErr = value;
    }
    public final String getBsfvErr()
    {
        return fBsfvErr;
    }
    *//**
     业务分离编号
     *//*
    public final void setGroupId(String value)
    {
        fGroupId = value;
    }
    public final String getGroupId()
    {
        return fGroupId;
    }*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion Model


   public static KttranBussinessFieldLmtRang Copy(KttranBussinessFieldLmtRang kbfr)
    {
        KttranBussinessFieldLmtRang n = new KttranBussinessFieldLmtRang();
        n.setFBsCode(kbfr.getFBsCode());
        n.setFBsfCode(kbfr.getFBsfCode());
        n.setFBsfvSn(kbfr.getFBsfvSn());
        n.setFBsfvLmt(kbfr.getFBsfvLmt());
        n.setFBsfvCount(kbfr.getFBsfvCount());
        n.setFBsfvActive(kbfr.getFBsfvActive());
        n.setFBsfvErr(kbfr.getFBsfvErr());
        return n;
    }
}
