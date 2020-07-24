package com.example.testmybatis.Common;





/**
 系统公用信息结构及处理
 */
public class SystemInfo /*extends BaseSystemInfo*/
{
    private static SystemInfo _instance;

    private int LogSaveDay;
    public final int getLogSaveDay()
    {
        return LogSaveDay;
    }
    public final void setLogSaveDay(int value)
    {
        LogSaveDay = value;
    }

    public CommServiceConfig Config;

 /*   public static SystemInfo Init(String configFileName)
    {
        if (_instance == null)
        {
            _instance = SystemInfo.instance(configFileName);
        }
        return _instance;
    }*/

    private static Object _synRoot = new Object();

    /*private static SystemInfo instance(String configFileName)
    {
        if (_instance == null)
        {
            synchronized (_synRoot)
            {
                if (_instance == null)
                {
                    _instance = new SystemInfo();
                    // 获取机器的基本信息

                    // 运行目录(不要取运行时的环境参数中的路径,直接取自运行文件的所在目录)
                    //string file = Application.ExecutablePath;
                    String file = (System.Web.HttpContext.Current != null) ? System.Web.HttpContext.Current.Request.MapPath("~/bin") : Application.ExecutablePath;
                    _instance.RunFile = file.substring(file.lastIndexOf('\\') + 1);
                    _instance.RunPath = file.substring(0, file.lastIndexOf('\\') + 1);
                    _instance.ConfigFileName = configFileName;
                    //  机器名
                    _instance.ComputerName = System.Windows.Forms.SystemInformation.ComputerName;
                    _instance.Config = CommServiceConfig.Init(configFileName);
                    _instance.SetDBBase(_instance.Config);
                    

                }
            }
        }
        return _instance;
    }*/

    public static SystemInfo getInstance()
    {
        return SystemInfo._instance;
    }
}
