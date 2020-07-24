package com.example.testmybatis.testxmlparser;


import com.example.testmybatis.Domain.Message;
import lombok.Data;
import org.dom4j.Element;
import org.springframework.stereotype.Component;


@Data
@Component
public class XmlBody extends Message
{

    private XmlHead Head;
    private String Orgcode;
    private Element Resquest;



//public string Response { get; set; }

   /* public final XmlSchema GetSchema()
    {
        return null;
    }*/

    public XmlBody()
    {
        setHead(new XmlHead());
    }





}
