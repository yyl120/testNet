package com.example.testmybatis.Domain;

public class Message
{
    public MessageCode Code;
    public String Info;

    public MessageCode getCode() {
        return Code;
    }

    public void setCode(MessageCode code) {
        Code = code;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public Message(){
    Code=MessageCode.默认;
}
public Message(MessageCode code){
    Code=code;
}


}

