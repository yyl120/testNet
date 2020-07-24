package com.example.testmybatis.Domain;



public enum MessageCode
{   默认("0001"),
    成功("0000"),

    XML解析失败("0101"),

    未提供消息交易编码trans_no("0102"),

    未提供安全验证用户名userid("0103"),

    未提供安全验证用户密码password("0104"),

    此账户已被停用("0105"),

    无此用户("0106"),

    密码验证失败("0107"),
    无业务权限("0108"),
    业务未注册("0109"),
    //无业务权限("0107"),
    业务不存在("0300"),
    远程服务未启动("0301"),
    业务被停用("0302"),
    不属于当前业务("0303"),
    系统设置错误未配置特殊取值("0400"),
    字段未提供值("0401"),
    字段长度超出范围("0402"),
    字段精度超出范围("0403"),
    非数字值("0404"),
    字段小数长度超出范围("0405"),
    超出值域范围("0406"),
    非日期值("0407"),
    保存数据时出错("0501"),
    查询数据时出错("0502"),
    系统错误("9999"),
    等待超时("9998"),
    服务已经停用("9997"),
    服务不存在("9996"),
    调用服务失败("9995"),
    服务验证失败("9994"),
    业务处理失败("9990");
    private String code;
private String info;
    //private String message;

    MessageCode(String code) {
        this.code = code;
       // this.message = message;
    }

    public String getCode() {
        return this.code;
    }


  //  public String getMessage() {
       // return message;


    @Override
    public String toString() {
        return "[" + this.code + "]" ;//+ this.message;
    }
}


