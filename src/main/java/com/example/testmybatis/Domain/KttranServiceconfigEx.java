package com.example.testmybatis.Domain;


import lombok.Data;
import org.dom4j.Document;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 服务配置表实体
 */
@Data
public class KttranServiceconfigEx
{
    private String SvrCode;
    public List<KttranBusinessEx> Businiess;//业务对象组
    public Document XmlDoc;//全局XmlDocument对象集合
    public HashMap Params;//全局哈希参数集合
    private List<KttranCheckDictionary> CheckValids;//需要审核验证的词典
    private String CheckValidArray;//使用逗号隔开的验证词典组合
    public boolean CheckValid = false;
    private boolean disposed = false;
   /*
    public final String getSvrCode()
    {
        return SvrCode;
    }
    public final void setSvrCode(String value)
    {
        SvrCode = value;
    }

    public final List<KttranBusinessEx> getBusiniess()
    {
        return Businiess;
    }
    public final void setBusiniess(List<KttranBusinessEx> value)
    {
        Businiess = value;
    }

    public final Document getXmlDoc()
    {
        return XmlDoc;
    }
    public final void setXmlDoc(Document value)
    {
        XmlDoc = value;
    }

    public final Hashtable getParams()
    {
        return Params;
    }
    public final void setParams(Hashtable value)
    {
        Params = value;
    }
    *//**

     *//*

    public final List<KttranCheckDictionary> getCheckValids()
    {
        return CheckValids;
    }
    public final void setCheckValids(List<KttranCheckDictionary> value)
    {
        CheckValids = value;
    }



    public final String getCheckValidArray()
    {
        return CheckValidArray;
    }
    public final void setCheckValidArray(String value)
    {
        CheckValidArray = value;
    }*/

    public KttranServiceconfigEx()
    {
        setParams(new HashMap());
        setBusiniess(new ArrayList<KttranBusinessEx>());
        setCheckValids(new ArrayList<KttranCheckDictionary>());
        setCheckValidArray("");
    }






}
