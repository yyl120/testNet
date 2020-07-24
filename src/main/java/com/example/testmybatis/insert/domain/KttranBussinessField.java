package com.example.testmybatis.insert.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 业务属性配置表实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KttranBussinessField//对应业务属性配置表  KTTRAN_BUSSINESS_FIELD
{
    //C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region Model
    private String fBsCode; //业务编号
    private String fBsfCode; //属性编号
    private String fBsfName; //属性名称
    private String fBsfComments; //属性描述
    private String fBsfColumn; //数据库字段,默认自动为属性名
    private String fBsfType; //类型,必须符合系统定义的通用类型
    private BigDecimal fBsfLength = null; //长度
    private BigDecimal fBsfPrecision = null; //精度
    private BigDecimal fBsfScale = null; //小数
    private String fBsfDefault; //默认值,未取到值时填充默认值
    private String fBsfMandatory; //是否强制（y是/n否）
    private String fBsfPk; //是否关键字（y是/n否）
    private String fBsfXml; //Xml属性
    private String fBsfAttach; //赋值字段（1需要/2不需要）
    private BigDecimal fBsfOrd = null; //顺序
    private String fBsfOptype; //操作应用类型(01保存,10输出,11两者)
    private String fBsfDirection; //字段方向BSF_DIRECTION;
    private String fIsPreRunSql; //是否提前运行语句返回值(Y是/N否)
    private String fBsfLmtRang; //取值限定(1是/2否)
    private String fBsfDictionary; //对应词典
    private String fBsfEncrypt; //是否加密

    private String fGroupId; //业务分离编号  add by mhh 20160411
    private String fBsfCursorType; //游标类型 add by mhh 20160518

    private String fBsfIsRecord;

    /**
     业务编号
     */
  /*  public final void setBsCode(String value)
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
     属性名称
     *//*
    public final void setBsfName(String value)
    {
        fBsfName = value;
    }
    public final String getBsfName()
    {
        return fBsfName;
    }
    *//**
     属性描述
     *//*
    public final void setBsfComments(String value)
    {
        fBsfComments = value;
    }
    public final String getBsfComments()
    {
        return fBsfComments;
    }
    *//**
     数据库字段,默认自动为属性名
     *//*
    public final void setBsfColumn(String value)
    {
        fBsfColumn = value;
    }
    public final String getBsfColumn()
    {
        return fBsfColumn;
    }
    *//**
     字段类型,(1文本,2数字,3字节流,4日期)  字段类型(1文本/2数字/3字节流/4日期/5游标/6Blog)
     *//*
    public final void setBsfType(String value)
    {
        fBsfType = value;
    }
    public final String getBsfType()
    {
        return fBsfType;
    }
    *//**
     长度
     *//*
    public final void setBsfLength(BigDecimal value)
    {
        fBsfLength = value;
    }
    public final BigDecimal getBsfLength()
    {
        return fBsfLength;
    }
    *//**
     精度
     *//*
    public final void setBsfPrecision(BigDecimal value)
    {
        fBsfPrecision = value;
    }
    public final BigDecimal getBsfPrecision()
    {
        return fBsfPrecision;
    }
    *//**
     小数
     *//*
    public final void setBsfScale(BigDecimal value)
    {
        fBsfScale = value;
    }
    public final BigDecimal getBsfScale()
    {
        return fBsfScale;
    }
    *//**
     默认值,未取到值时填充默认值
     *//*
    public final void setBsfDefault(String value)
    {
        fBsfDefault = value;
    }
    public final String getBsfDefault()
    {
        return fBsfDefault;
    }
    *//**
     是否强制（Y是/N否）
     *//*
    public final void setBsfMandatory(String value)
    {
        fBsfMandatory = value;
    }
    public final String getBsfMandatory()
    {
        return fBsfMandatory;
    }
    *//**
     是否关键字（Y是/N否）
     *//*
    public final void setBsfPk(String value)
    {
        fBsfPk = value;
    }
    public final String getBsfPk()
    {
        return fBsfPk;
    }
    *//**
     XML属性
     *//*
    public final void setBsfXml(String value)
    {
        fBsfXml = value;
    }
    public final String getBsfXml()
    {
        return fBsfXml;
    }
    *//**
     赋值字段（1需要/2不需要）
     *//*
    public final void setBsfAttach(String value)
    {
        fBsfAttach = value;
    }
    public final String getBsfAttach()
    {
        return fBsfAttach;
    }
    *//**
     顺序
     *//*
    public final void setBsfOrd(BigDecimal value)
    {
        fBsfOrd = value;
    }
    public final BigDecimal getBsfOrd()
    {
        return fBsfOrd;
    }

    *//**
     操作应用类型(01保存,10输出,11两者)
     *//*
    public final void setBsfOptype(String value)
    {
        fBsfOptype = value;
    }
    public final String getBsfOptype()
    {
        return fBsfOptype;
    }

    *//**
     字段方向
     *//*
    public final void setBsfDirection(String value)
    {
        fBsfDirection = value;
    }
    public final String getBsfDirection()
    {
        return fBsfDirection;
    }
    *//**
     是否提前运行语句返回值(Y是/N否)
     *//*
    public final void setIsPreRunSql(String value)
    {
        fIsPreRunSql = value;
    }
    public final String getIsPreRunSql()
    {
        return fIsPreRunSql;
    }
    *//**
     取值限定(1是/2否)
     *//*
    public final void setBsfLmtRang(String value)
    {
        fBsfLmtRang = value;
    }
    public final String getBsfLmtRang()
    {
        return fBsfLmtRang;
    }
    *//**
     对应词典
     *//*
    public final void setBsfDictionary(String value)
    {
        fBsfDictionary = value;
    }
    public final String getBsfDictionary()
    {
        return fBsfDictionary;
    }

    *//**
     是否加密
     *//*
    public final void setBsfEncrypt(String value)
    {
        fBsfEncrypt = value;
    }
    public final String getBsfEncrypt()
    {
        return fBsfEncrypt;
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
    }

    *//**
     游标类型
     *//*
    public final void setBsfCursorType(String value)
    {
        fBsfCursorType = value;
    }
    public final String getBsfCursorType()
    {
        return fBsfCursorType;
    }

    *//**
     记录数据(1是/0否)
     *//*
    public final void setBsfIsRecord(String value)
    {
        fBsfIsRecord = value;
    }
    public final String getBsfIsRecord()
    {
        return fBsfIsRecord;
    }
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion Model
}
