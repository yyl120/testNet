package com.example.testmybatis.service;


public abstract class ServiceConfig
{
    /**
     数据库上下文
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string DbContext {get;set;}
    private String DbContext;
    public final String getDbContext()
    {
        return DbContext;
    }
    public final void setDbContext(String value)
    {
        DbContext = value;
    }
    /**
     数据库引擎名称
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string DbProviderName {get;set;}
    private String DbProviderName;
    public final String getDbProviderName()
    {
        return DbProviderName;
    }
    public final void setDbProviderName(String value)
    {
        DbProviderName = value;
    }
    /**
     数据库引擎
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string DbProvider {get;set;}
    private String DbProvider;
    public final String getDbProvider()
    {
        return DbProvider;
    }
    public final void setDbProvider(String value)
    {
        DbProvider = value;
    }
    /**
     数据连接字符串(不含密码)
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string DbSource {get;set;}
    private String DbSource;
    public final String getDbSource()
    {
        return DbSource;
    }
    public final void setDbSource(String value)
    {
        DbSource = value;
    }
    /**
     程序标题
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string PTitle {get;set;}
    private String PTitle;
    public final String getPTitle()
    {
        return PTitle;
    }
    public final void setPTitle(String value)
    {
        PTitle = value;
    }
    /**
     客户端名称
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string DbUserName {get;set;}
    private String DbUserName;
    public final String getDbUserName()
    {
        return DbUserName;
    }
    public final void setDbUserName(String value)
    {
        DbUserName = value;
    }
    /**
     用户序列码
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string UserSerial {get;set;}
    private String UserSerial;
    public final String getUserSerial()
    {
        return UserSerial;
    }
    public final void setUserSerial(String value)
    {
        UserSerial = value;
    }
    /**
     数据库连接密码
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string DbPassword {get;set;}
    private String DbPassword;
    public final String getDbPassword()
    {
        return DbPassword;
    }
    public final void setDbPassword(String value)
    {
        DbPassword = value;
    }
    /**
     登入时间限定
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string DbLastDate {get;set;}
    private String DbLastDate;
    public final String getDbLastDate()
    {
        return DbLastDate;
    }
    public final void setDbLastDate(String value)
    {
        DbLastDate = value;
    }
    /**
     系统服务
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string SysService {get;set;}
    private String SysService;
    public final String getSysService()
    {
        return SysService;
    }
    public final void setSysService(String value)
    {
        SysService = value;
    }
    /**
     访问服务地址
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement("SocketAddress")] public ServiceCDATA SocketAddress {get;set;}
/*    private ServiceCDATA SocketAddress;
    public final ServiceCDATA getSocketAddress()
    {
        return SocketAddress;
    }
    public final void setSocketAddress(ServiceCDATA value)
    {
        SocketAddress = value;
    }*/
    /**
     服务名称
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string SocketName {get;set;}
    private String SocketName;
    public final String getSocketName()
    {
        return SocketName;
    }
    public final void setSocketName(String value)
    {
        SocketName = value;
    }
    /**
     数据库操作配置地址，不需要更改
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string MyIbatisDaoConfigPath {get;set;}
    private String MyIbatisDaoConfigPath;
    public final String getMyIbatisDaoConfigPath()
    {
        return MyIbatisDaoConfigPath;
    }
    public final void setMyIbatisDaoConfigPath(String value)
    {
        MyIbatisDaoConfigPath = value;
    }

    private String FilePath = "";
    private static Object _synRoot = new Object();
    private static ServiceConfig _instance;
    public static ServiceConfig getInstance()
    {
        if (_instance == null)
        {
            throw new RuntimeException("未初始化配置文件");
        }
        return _instance;
    }
    /**
     初始化配置文件中配置的信息

     @param fileName
     @return
     */
   /* public static ServiceConfig Init(String fileName)
    {
        if (_instance == null)
        {
            synchronized (_synRoot)
            {
                if (_instance == null)
                {
                    _instance = XmlHelper.<ServiceConfig>XmlDeserializeFromFile(fileName, Encoding.UTF8);
                    _instance.FilePath = fileName;
                }
            }
        }
        return _instance;
    }*/
    /**
     是否重置信息

     @param Saveed 是否保存
     */
    public void Reflash(boolean Saveed)
    {
        //if (CommServiceConfig.Instance.EsbBsType == EsbBsTypeEnum.通用业务DLL)
        //{
        //    if (!CommServiceConfig.Instance.SvrAddress.Value.Contains(".dll") && !CommServiceConfig.Instance.SvrAddress.Value.Contains(".DLL"))
        //        CommServiceConfig.Instance.SvrAddress += ".DLL";
        //    if (!CommServiceConfig.Instance.SvrAddress.Value.Contains(":"))
        //        CommServiceConfig.Instance.SvrAddress.Value = SystemInfo.RunPath + CommServiceConfig.Instance.SvrAddress.Value;
        //}
       /* if (Saveed)
        {
            ServiceConfig.getInstance().Save();
        }*/
    }

  /*  public abstract String Get_MyIbatisDaoConfig_FullPath();

    public void Save(String fileName)
    {
        XmlHelper.XmlSerializeToFile(_instance, fileName, UTF8Encoding.UTF8);
    }

    public void Save()
    {
        Save(_instance.FilePath);
    }*/
}
