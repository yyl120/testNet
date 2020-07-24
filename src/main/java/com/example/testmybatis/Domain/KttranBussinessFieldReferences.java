package com.example.testmybatis.Domain;


import java.io.IOException;

/**
 需要提前处理的对象
 */
public class KttranBussinessFieldReferences
{
    private KttranBussinessFieldEx Feild;
    /**
     当前业务
     */
    private KttranBusinessEx Business;
    /**
     引用的业务,动态获取
     */
    private KttranBusinessEx ReferencesBusiness;
    private String ReferencesFeildName;
    private boolean disposed = false;



    public final KttranBussinessFieldEx getFeild()
    {
        return Feild;
    }
    public final void setFeild(KttranBussinessFieldEx value)
    {
        Feild = value;
    }



    public final KttranBusinessEx getBusiness()
    {
        return Business;
    }
    public final void setBusiness(KttranBusinessEx value)
    {
        Business = value;
    }



    public final KttranBusinessEx getReferencesBusiness()
    {
        return ReferencesBusiness;
    }
    public final void setReferencesBusiness(KttranBusinessEx value)
    {
        ReferencesBusiness = value;
    }


    public final String getReferencesFeildName()
    {
        return ReferencesFeildName;
    }
    public final void setReferencesFeildName(String value)
    {
        ReferencesFeildName = value;
    }








}

