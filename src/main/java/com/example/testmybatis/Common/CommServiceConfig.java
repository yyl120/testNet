package com.example.testmybatis.Common;


import com.example.testmybatis.service.ServiceConfig;

public class CommServiceConfig extends ServiceConfig
{
    /**
     执行时间写入)
     */
    private String LogExcuteTime;
    public final String getLogExcuteTime()
    {
        return LogExcuteTime;
    }
    public final void setLogExcuteTime(String value)
    {
        LogExcuteTime = value;
    }

    /**
     日志分流个数（从1开始）
     */
    private int LogSplit;
    public final int getLogSplit()
    {
        return LogSplit;
    }
    public final void setLogSplit(int value)
    {
        LogSplit = value;
    }

    /**
     各医院上传识别码(两位数字)
     */
    private String LogIc;
    public final String getLogIc()
    {
        return LogIc;
    }
    public final void setLogIc(String value)
    {
        LogIc = value;
    }

    /**
     日志保留时间
     */
    private int LogSaveDay;
    public final int getLogSaveDay()
    {
        return LogSaveDay;
    }
    public final void setLogSaveDay(int value)
    {
        LogSaveDay = value;
    }

    /**
     是否显示统计数量
     */
    private int ShowCountNumber;
    public final int getShowCountNumber()
    {
        return ShowCountNumber;
    }
    public final void setShowCountNumber(int value)
    {
        ShowCountNumber = value;
    }

    /**
     日志管理分页条数
     */
    private int LogPerpage;
    public final int getLogPerpage()
    {
        return LogPerpage;
    }
    public final void setLogPerpage(int value)
    {
        LogPerpage = value;
    }

    /**
     是否生成日志
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string IsLog {get;set;}
    private String IsLog;
    public final String getIsLog()
    {
        return IsLog;
    }
    public final void setIsLog(String value)
    {
        IsLog = value;
    }

    /**
     是否生成失败日志
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string IsSaveFailure {get;set;}
    private String IsSaveFailure;
    public final String getIsSaveFailure()
    {
        return IsSaveFailure;
    }
    public final void setIsSaveFailure(String value)
    {
        IsSaveFailure = value;
    }

    /**
     权限验证
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string RightValid {get;set;}
    private String RightValid;
    public final String getRightValid()
    {
        return RightValid;
    }
    public final void setRightValid(String value)
    {
        RightValid = value;
    }

    /**
     日志文件路径
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string LogFilePath {get;set;}
    private String LogFilePath;
    public final String getLogFilePath()
    {
        return LogFilePath;
    }
    public final void setLogFilePath(String value)
    {
        LogFilePath = value;
    }

    /**
     本地文件Log文件记录
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string IsLogInfoFile {get;set;}
    private String IsLogInfoFile;
    public final String getIsLogInfoFile()
    {
        return IsLogInfoFile;
    }
    public final void setIsLogInfoFile(String value)
    {
        IsLogInfoFile = value;
    }

    /**
     本地文件Exp文件记录
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string IsLogExpFile {get;set;}
    private String IsLogExpFile;
    public final String getIsLogExpFile()
    {
        return IsLogExpFile;
    }
    public final void setIsLogExpFile(String value)
    {
        IsLogExpFile = value;
    }

    /**
     本地文件保存天数
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string LogFileSave {get;set;}
    private String LogFileSave;
    public final String getLogFileSave()
    {
        return LogFileSave;
    }
    public final void setLogFileSave(String value)
    {
        LogFileSave = value;
    }

    /**
     业务属性表
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string IsValid {get;set;}
    private String IsValid;
    public final String getIsValid()
    {
        return IsValid;
    }
    public final void setIsValid(String value)
    {
        IsValid = value;
    }

    /**
     自动提交行数
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string DianaCount {get;set;}
    private String DianaCount;
    public final String getDianaCount()
    {
        return DianaCount;
    }
    public final void setDianaCount(String value)
    {
        DianaCount = value;
    }

    /**
     业务表
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string BsTable {get;set;}
    private String BsTable;
    public final String getBsTable()
    {
        return BsTable;
    }
    public final void setBsTable(String value)
    {
        BsTable = value;
    }

    /**
     业务属性表
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string BsFeildTable {get;set;}
    private String BsFeildTable;
    public final String getBsFeildTable()
    {
        return BsFeildTable;
    }
    public final void setBsFeildTable(String value)
    {
        BsFeildTable = value;
    }

    /**
     业务属性取值表
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string BsFeildValueTable {get;set;}
    private String BsFeildValueTable;
    public final String getBsFeildValueTable()
    {
        return BsFeildValueTable;
    }
    public final void setBsFeildValueTable(String value)
    {
        BsFeildValueTable = value;
    }

    /**
     SocketActiveMQ寄宿地址
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string SocketActiveMQ {get;set;}
    private String SocketActiveMQ;
    public final String getSocketActiveMQ()
    {
        return SocketActiveMQ;
    }
    public final void setSocketActiveMQ(String value)
    {
        SocketActiveMQ = value;
    }

    /**
     机构号
     */
    //public string OrgCode { get; set; }
    /**
     业务类别编号
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string GroupId {get;set;}
    private String GroupId;
    public final String getGroupId()
    {
        return GroupId;
    }
    public final void setGroupId(String value)
    {
        GroupId = value;
    }
    /**
     业务类别子编号
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement] public string No {get;set;}
    private String No;
    public final String getNo()
    {
        return No;
    }
    public final void setNo(String value)
    {
        No = value;
    }


    /**
     是否开启加密
     */
    private String OpenEncry;
    public final String getOpenEncry()
    {
        return OpenEncry;
    }
    public final void setOpenEncry(String value)
    {
        OpenEncry = value;
    }

    /**
     DES加密密钥
     */
    private String DESKey;
    public final String getDESKey()
    {
        return DESKey;
    }
    public final void setDESKey(String value)
    {
        DESKey = value;
    }

    private String FilePath = "";
    private static Object _synRoot = new Object();
    private static CommServiceConfig _instance;
    public static CommServiceConfig getInstance()
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
//C# TO JAVA CONVERTER WARNING: There is no Java equivalent to C#'s shadowing via the 'new' keyword:
//ORIGINAL LINE: public static new CommServiceConfig Init(string fileName)
/*
   public static CommServiceConfig Init(String fileName)
    {
        if (_instance == null)
        {
            synchronized (_synRoot)

            {
                if (_instance == null)
                {
                    _instance = XmlHelper.<CommServiceConfig>XmlDeserializeFromFile(fileName, Encoding.UTF8);
                    _instance.FilePath = fileName;
                }
            }
        }
        return _instance;
    }
*/

    /*@Override
    public String Get_MyIbatisDaoConfig_FullPath()
    {
        if (MyIbatisDaoConfigPath.indexOf(":") > -1)
        {
            return MyIbatisDaoConfigPath;
        }
        else
        {
            return SystemInfo.Instance.RunPath + MyIbatisDaoConfigPath.substring(0, MyIbatisDaoConfigPath.lastIndexOf('/') + 1);
        }
    }*/

   /* @Override
    public void Save(String fileName)
    {
        if (tangible.StringHelper.isNullOrEmpty(fileName))
        {
            fileName = SystemInfo.Instance.RunPath + "CommServiceConfig.xml";
        }
        else if (!fileName.contains(":"))
        {
            fileName = SystemInfo.Instance.RunPath + fileName;
        }

        XmlHelper.XmlSerializeToFile(_instance, fileName, UTF8Encoding.UTF8);
    }*/

   /*   @Override
  public void Save()
    {
        Save(_instance.FilePath);

    }*/
}

//Helper class added by C# to Java Converter:



//----------------------------------------------------------------------------------------
//	Copyright ? 2007 - 2019 Tangible Software Solutions, Inc.
//	This class can be used by anyone provided that the copyright notice remains intact.
//
//	This class is used to replicate some .NET string methods in Java.
//----------------------------------------------------------------------------------------


