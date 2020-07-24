package com.example.testmybatis.testxmlparser;



import com.example.testmybatis.Domain.Message;
import lombok.Data;
import org.springframework.stereotype.Component;


//[XmlRoot("head")]
@Data
@Component
public class XmlHead extends Message
{

    private String TransAct;

    private String UserID;//消息交易编码

    private String Password;//安全验证用户密码

    //private String islog;  // log

/*    public String getTransAct() {
        return TransAct;
    }

    public void setTransAct(String transAct) {
        TransAct = transAct;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getIslog() {
        return islog;
    }

    public void setIslog(String islog) {
        this.islog = islog;
    }*/
/* public final String getTransAct()
    {
        return TransAct;
    }
    public final void setTransAct(String value)
    {
        TransAct = value;
    }*/
    /**
     消息交易编码
     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement("userid")] public string UserID {get;set;}


    /**

     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement("password")] public string Password {get;set;}



    /**

     */
//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [XmlElement("islog")] public string islog {get;set;}






}

