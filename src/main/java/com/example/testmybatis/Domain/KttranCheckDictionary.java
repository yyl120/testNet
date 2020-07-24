package com.example.testmybatis.Domain;



public class KttranCheckDictionary
{
    /**
     表类型（根据对外发布的编码获得表类型）
     */
    private String CheckType;
    public final String getCheckType()
    {
        return CheckType;
    }
    public final void setCheckType(String value)
    {
        CheckType = value;
    }
    /**
     表用户（对应医院不同业务的各套字典）
     */
    private String CheckUserCode;
    public final String getCheckUserCode()
    {
        return CheckUserCode;
    }
    public final void setCheckUserCode(String value)
    {
        CheckUserCode = value;
    }
    /**
     医院代码
     */
    private String CheckOrgCode;
    public final String getCheckOrgCode()
    {
        return CheckOrgCode;
    }
    public final void setCheckOrgCode(String value)
    {
        CheckOrgCode = value;
    }
    /**
     审核结果
     */
    private String CheckResult;
    public final String getCheckResult()
    {
        return CheckResult;
    }
    public final void setCheckResult(String value)
    {
        CheckResult = value;
    }
    /**
     对应的业务代码值
     */
    private String BsCode;
    public final String getBsCode()
    {
        return BsCode;
    }
    public final void setBsCode(String value)
    {
        BsCode = value;
    }

    /**
     对应的业务字段代码值
     */
    private String BsfCode;
    public final String getBsfCode()
    {
        return BsfCode;
    }
    public final void setBsfCode(String value)
    {
        BsfCode = value;
    }
}

