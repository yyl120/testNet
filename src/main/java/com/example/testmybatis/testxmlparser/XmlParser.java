package com.example.testmybatis.testxmlparser;

import com.example.testmybatis.Domain.MessageCode;
import org.dom4j.*;
import org.springframework.stereotype.Component;

@Component
public class XmlParser {
    public Document xmldoc;


    public  XmlBody XmlDeserialize(String xml) {
        XmlBody body = new XmlBody();
        try {
            xml = xml.trim();
            if (xml.equals("")) {

                body.Code = MessageCode.XML解析失败;
                body.Info = "Xml规则字符串不能为空";
            } else {
                try {
                    xmldoc = DocumentHelper.parseText(xml);
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                //xmldoc.setPreserveWhitespace(true);
                //xmldoc.PreserveWhitespace = true;
                //xmldoc.LoadXml(xml);
                //XmlNode node = xmldoc.SelectSingleNode("/body/head/userid");
                /*Element et = xmldoc.getRootElement();
                Element userid = et.element("hrader").element("userid");*/
                //System.out.println("1");
                Node node=xmldoc.selectSingleNode("/body/head/userid");
            //    System.out.println(node.getText());;
              //  System.out.println("111"+node.getName());
                if (node == null) {
                    body.Code = MessageCode.未提供安全验证用户名userid;
                    return body;
                }

                body.getHead().setUserID(node.getText());
              //  System.out.println("111"+body);
              //  Node node = xmldoc.selectSingleNode();
                //body.getHead().setUserID(userid.asXML());
                // node = xmldoc.SelectSingleNode("/body/head/password");
                //Element password = et.element("hrader").element("password");
                node=xmldoc.selectSingleNode("/body/head/password");
                if (node == null) {
                    body.Code = MessageCode.未提供安全验证用户密码password;
                    return body;
                }
               // body.getHead().setPassword(password.asXML());
                body.getHead().setPassword(node.getText());
               // Element trans_no = et.element("hrader").element("trans_no");
                //  node = xmldoc.SelectSingleNode("/body/head/trans_no");
                node=xmldoc.selectSingleNode("/body/head/trans_no");
                if (node == null) {
                    body.Code = MessageCode.未提供消息交易编码trans_no;
                    return body;
                }
                body.getHead().setTransAct(node.getText());
               // body.getHead().setTransAct(trans_no.asXML());

              //  Element islog = et.element("hrader").element("islog");
                // node = xmldoc.SelectSingleNode("/body/head/islog");
               /* if (islog != null) {
                    body.getHead().setIslog(islog.asXML());*/
                    //body.Code = MessageCode.未提供消息交易编码trans_no;
                    //return body;

             /*   if (isReplaceDotToUnline) {
                    // body.Head.TransAct = body.Head.TransAct.Replace('.', '_');
                }*/
                //Element resquest = et.element("body").element("resquest");//.em xmldoc.SelectSingleNode("/body/resquest");
            node=xmldoc.selectSingleNode("/body/resquest");
               // System.out.println("node"+node.asXML());
                if (node != null) {
                 //   System.out.println("node"+(Element)node);
                    body.setResquest( (Element) node);
                } else
               /* {

                    XmlNode requestNode = xmldoc.SelectSingleNode("/body/request");
                    xmldoc.InnerXml = Regex.Replace(xmldoc.InnerXml, "(?s)(?<=</?)request(?=>)", "resquest");

                    body.Resquest = requestNode instanceof XmlElement ? (XmlElement)requestNode : null;
                }*/
                    xml = xmldoc.asXML();// ConvertXmlToString(xmldoc);
                body.Code = MessageCode.成功;
            }
        } catch (RuntimeException ex) {
            body.Code = MessageCode.XML解析失败;
            body.Info = ex.getMessage();
        }
       // System.out.println("code+++"+body.Code);
        System.out.println(body);
        return body;

    }


}