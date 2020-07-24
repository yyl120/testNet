package com.example.testmybatis.insert.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 业务配置表实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KttranBusiness
{

    private String BsCode; //业务编号
    private String BsName; //业务名(XML映射)
    private String BsComments; //业务描述
    private String SvrCode; //服务编号
    private String BsParentCode; //上级业务
    private BigDecimal BsOrder = null; //顺序
    private String UseStatus; //状态（n关闭/y启动），默认y
    private String BsMemo; //说明
    private String BsStatusSaveTable; //表保存
    private String BsStatusSchTable; //表查询
    private String BsStatusSaveSql; //语句保存
    private String BsStatusSchSql; //语句查询
    private String BsStatusProc; //存储过程
    private String BsRepsql; //输出语句
    private String BsRspsql; //存储语句
    private String BsTable; //数据库表名
    private String BsCountType; //统计方式(1年/2月/3天)
    private String BsSimpleParam; //简单参数结构
    private String BsOutXml; //输出标签
    private String BsCheckValid; //是否审核验证（N不需要/Y需要）
    private String BsMandatory; //是否强制XML（y是/n否） 强制时必须提供该结构，如果未提供则报错；非强制时未取到结构则自动向上级获取，如果自动向上肯定可以获取到结构
    private String BsStruct; //是否按结构处理（y是/n否） 按结构处理,Y时，则必须按结构进行业务逻辑处理,处理数为大于或等于1条；否则如果未找到该结构则不进行处理，处理数大于或等于0条
    private String TableName; //数据库表名
    private BigDecimal DianaCount = null; //自动提交行数
    private String Transaction; //关闭事务
    private String DownFlag; //下发业务标志
    private String GroupId; //业务分离编号  add by mhh 20160411
    private String SerialString; //注册码        add by mhh 20160523
    //private boolean SerialFlag; //注册码状态    add by mhh 20160523
    private String BsResponseHead; //头部返回消息  add by mhh 20160907
    private String BsIsRecord; //记录内容

    public void Init(KttranBusiness bs){}
  //  private String serial;//SERIAL

//    //**业务编号
//     //*
//    public final void setBsCode(String value)
//    {
//        fBsCode = value;
//    }
//    public final String getBsCode()
//    {
//        return fBsCode;
//    }
//    //**业务名(XML映射)
//     //*
//    public final void setBsName(String value)
//    {
//        fBsName = value;
//    }
//    public final String getBsName()
//    {
//        return fBsName;
//    }
//    //**输出标签
//     //*
//    public final void setBsOutXml(String value)
//    {
//        fBsOutXml = value;
//    }
//    public final String getBsOutXml()
//    {
//        return fBsOutXml;
//    }
//    //**
//    // 业务描述
//     //*
//    public final void setBsComments(String value)
//    {
//        fBsComments = value;
//    }
//    public final String getBsComments()
//    {
//        return fBsComments;
//    }
//    //*//**
//     //服务编号
//     //*//*
//    public final void setSvrCode(String value)
//    {
//        fSvrCode = value;
//    }
//    public final String getSvrCode()
//    {
//        return fSvrCode;
//    }
//    //*//**
//     //上级业务
//     //*//*
//    public final void setBsParentCode(String value)
//    {
//        fBsParentCode = value;
//    }
//    public final String getBsParentCode()
//    {
//        return fBsParentCode;
//    }
//
//    public final void setBsOrder(BigDecimal value)
//    {
//        fBsOrder = value;
//    }
//    public final BigDecimal getBsOrder()
//    {
//        return fBsOrder;
//    }
//
//    public final void setUseStatus(String value)
//    {
//        fUseStatus = value;
//    }
//    public final String getUseStatus()
//    {
//        return fUseStatus;
//    }
//
//    public final void setBsMemo(String value)
//    {
//        fBsMemo = value;
//    }
//    public final String getBsMemo()
//    {
//        return fBsMemo;
//    }
//
//    public final void setBsStatusSaveTable(String value)
//    {
//        fBsStatusSaveTable = value;
//    }
//    public final String getBsStatusSaveTable()
//    {
//        return fBsStatusSaveTable;
//    }
//
//
//    public final void setBsStatusSchTable(String value)
//    {
//        fBsStatusSchTable = value;
//    }
//    public final String getBsStatusSchTable()
//    {
//        return fBsStatusSchTable;
//    }
//
//
//    public final void setBsStatusSaveSql(String value)
//    {
//        fBsStatusSaveSql = value;
//    }
//    public final String getBsStatusSaveSql()
//    {
//        return fBsStatusSaveSql;
//    }
//
//
//    public final void setBsStatusSchSql(String value)
//    {
//        fBsStatusSchSql = value;
//    }
//    public final String getBsStatusSchSql()
//    {
//        return fBsStatusSchSql;
//    }
//
//
//    public final void setBsStatusProc(String value)
//    {
//        fBsStatusProc = value;
//    }
//    public final String getBsStatusProc()
//    {
//        return fBsStatusProc;
//    }
//
//
//    public final void setBsRepsql(String value)
//    {
//        fBsRepsql = value;
//    }
//    public final String getBsRepsql()
//    {
//        return fBsRepsql;
//    }
//
//    public final void setBsRspsql(String value)
//    {
//        fBsRspsql = value;
//    }
//    public final String getBsRspsql()
//    {
//        return fBsRspsql;
//    }
//
//    public final void setBsTable(String value)
//    {
//        fBsTable = value;
//    }
//    public final String getBsTable()
//    {
//        return fBsTable;
//    }
//
//    public final void setBsCountType(String value)
//    {
//        fBsCountType = value;
//    }
//    public final String getBsCountType()
//    {
//        return fBsCountType;
//    }
//
//    public final void setBsSimpleParam(String value)
//    {
//        fBsSimpleParam = value;
//    }
//    public final String getBsSimpleParam()
//    {
//        return fBsSimpleParam;
//    }
//
//    public final void setBsCheckValid(String value)
//    {
//        fBsCheckValid = value;
//    }
//    public final String getBsCheckValid()
//    {
//        return fBsCheckValid;
//    }
//
//
//    public final void setBsMandatory(String value)
//    {
//        fBsMandatory = value;
//    }
//    public final String getBsMandatory()
//    {
//        return fBsMandatory;
//    }
//
//    public final void setBsStruct(String value)
//    {
//        fBsStruct = value;
//    }
//    public final String getBsStruct()
//    {
//        return fBsStruct;
//    }
//
//    public final void setTableName(String value)
//    {
//        fTableName = value;
//    }
//    public final String getTableName()
//    {
//        return fTableName;
//    }
//
//    public final void setDianaCount(BigDecimal value)
//    {
//        fDianaCount = value;
//    }
//    public final BigDecimal getDianaCount()
//    {
//        return fDianaCount;
//    }
//
//
//    public final void setTransaction(String value)
//    {
//        fTransaction = value;
//    }
//    public final String getTransaction()
//    {
//        return fTransaction;
//    }
//
//
//    public final void setDownFlag(String value)
//    {
//        fDownFlag = value;
//    }
//    public final String getDownFlag()
//    {
//        return fDownFlag;
//    }
//
//
//    public final void setGroupId(String value)
//    {
//        fGroupId = value;
//    }
//    public final String getGroupId()
//    {
//        return fGroupId;
//    }
//
//    public final void setSerialString(String value)
//    {
//        fSerialString = value;
//    }
//    public final String getSerialString()
//    {
//        return fSerialString;
//    }
//
//
//    public final void setSerialFlag(boolean value)
//    {
//        fSerialFlag = value;
//    }
//    public final boolean getSerialFlag()
//    {
//        return fSerialFlag;
//    }
//
//
//    public final void setBsResponseHead(String value)
//    {
//        fBsResponseHead = value;
//    }
//    public final String getBsResponseHead()
//    {
//        return fBsResponseHead;
//    }
//
//
//    public final void setBsIsRecord(String value)
//    {
//        fBsIsRecord = value;
//    }
//    public final String getBsIsRecord()
//    {
//        return fBsIsRecord;
//    }

}
