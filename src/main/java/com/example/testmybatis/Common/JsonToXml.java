package com.example.testmybatis.Common;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class JsonToXml {

    public static String jsonToXml(String json) {
        try {
            StringBuffer buffer = new StringBuffer();
            buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            JSONObject jObj = JSON.parseObject(json);
            jsonToXmlstr(jObj, buffer);
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String jsonToXmlstr(JSONObject jObj, StringBuffer buffer) {
        Set<Map.Entry<String, Object>> se = jObj.entrySet();
        for (Iterator<Map.Entry<String, Object>> it = se.iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> en = it.next();
            if (en.getValue().getClass().getName().equals("com.alibaba.fastjson.JSONObject")) {
                buffer.append("<" + en.getKey() + ">");
                JSONObject jo = jObj.getJSONObject(en.getKey());
                jsonToXmlstr(jo, buffer);
                buffer.append("</" + en.getKey() + ">");
            } else if (en.getValue().getClass().getName().equals("com.alibaba.fastjson.JSONArray")) {
                JSONArray jarray = jObj.getJSONArray(en.getKey());
                for (int i = 0; i < jarray.size(); i++) {
                    buffer.append("<" + en.getKey() + ">");
                    JSONObject jsonobject = jarray.getJSONObject(i);
                    jsonToXmlstr(jsonobject, buffer);
                    buffer.append("</" + en.getKey() + ">");
                }
            } else if (en.getValue().getClass().getName().equals("java.lang.String")) {
                buffer.append("<" + en.getKey() + ">" + en.getValue());
                buffer.append("</" + en.getKey() + ">");
            }
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        String xmlstr = jsonToXml("{\n" +
                "\t\"body\": {\n" +
                "\t\t\"head\": {\n" +
                "\t\t\t\"password\": \"123\",\n" +
                "\t\t\t\"trans_no\": \"C01_04_00_00\",\n" +
                "\t\t\t\"userid\": \"yzs\"\n" +
                "\t\t},\n" +
                "\t\t\"resquest\": {\n" +
                "\t\t\t\"C01.04.00.00\": {\n" +
                "\t\t\t\t\"PRE_INFOR\": {\n" +
                "\t\t\t\t\t\"CLINIC_ID\": \"200611100303000001\",\n" +
                "\t\t\t\t\t\"EMP_ID\": \"59871\",\n" +
                "\t\t\t\t\t\"EMP_ID_ORG\": \"100303000001\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"330023014\",\n" +
                "\t\t\t\t\t\"PRES_ID\": \"200611100303000001\",\n" +
                "\t\t\t\t\t\"RECORD_TIME\": \"2020-06-11 08:47:04\",\n" +
                "\t\t\t\t\t\"TYPE\": \"1\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"PRE_LIST\": [{\n" +
                "\t\t\t\t\t\"CREATE_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"GROUPNO\": \"1\",\n" +
                "\t\t\t\t\t\"MEDIC_CLASS\": \"Y\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE\": \"426810\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE_LOCAL\": \"426810\",\n" +
                "\t\t\t\t\t\"MEDIC_DAYS\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_GENERAL_NAME\": \"氯化钠注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_NAME\": \"氯化钠注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_QUANTITY\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_REMARK\": \"\",\n" +
                "\t\t\t\t\t\"MEDIC_SELF\": \"0\",\n" +
                "\t\t\t\t\t\"MEDIC_SPEC\": \"注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_TYPE\": \"426810\",\n" +
                "\t\t\t\t\t\"MEDIC_USECODE\": \"qd\",\n" +
                "\t\t\t\t\t\"MEDIC_USEDOSAGE\": \"250.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_USEMODE\": \"ivgtt\",\n" +
                "\t\t\t\t\t\"MEDIC_USEPLAN\": \"一日1次\",\n" +
                "\t\t\t\t\t\"MEDIC_USEQUANTITY\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MODIFY_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"NO\": \"1\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"100303\",\n" +
                "\t\t\t\t\t\"PRES_ID\": \"200611100303000001\"\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"CREATE_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"GROUPNO\": \"1\",\n" +
                "\t\t\t\t\t\"MEDIC_CLASS\": \"Y\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE\": \"342199\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE_LOCAL\": \"342199\",\n" +
                "\t\t\t\t\t\"MEDIC_DAYS\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_GENERAL_NAME\": \"克林霉素磷酸酯注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_NAME\": \"克林霉素磷酸酯注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_QUANTITY\": \"3.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_REMARK\": \"\",\n" +
                "\t\t\t\t\t\"MEDIC_SELF\": \"0\",\n" +
                "\t\t\t\t\t\"MEDIC_SPEC\": \"注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_TYPE\": \"342199\",\n" +
                "\t\t\t\t\t\"MEDIC_USECODE\": \"qd\",\n" +
                "\t\t\t\t\t\"MEDIC_USEDOSAGE\": \"6.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_USEMODE\": \"ivgtt\",\n" +
                "\t\t\t\t\t\"MEDIC_USEPLAN\": \"一日1次\",\n" +
                "\t\t\t\t\t\"MEDIC_USEQUANTITY\": \"3.0000\",\n" +
                "\t\t\t\t\t\"MODIFY_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"NO\": \"2\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"100303\",\n" +
                "\t\t\t\t\t\"PRES_ID\": \"200611100303000001\"\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"CREATE_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"GROUPNO\": \"2\",\n" +
                "\t\t\t\t\t\"MEDIC_CLASS\": \"Y\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE\": \"432139\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE_LOCAL\": \"432139\",\n" +
                "\t\t\t\t\t\"MEDIC_DAYS\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_GENERAL_NAME\": \"葡萄糖注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_NAME\": \"葡萄糖注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_QUANTITY\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_REMARK\": \"\",\n" +
                "\t\t\t\t\t\"MEDIC_SELF\": \"0\",\n" +
                "\t\t\t\t\t\"MEDIC_SPEC\": \"注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_TYPE\": \"432139\",\n" +
                "\t\t\t\t\t\"MEDIC_USECODE\": \"qd\",\n" +
                "\t\t\t\t\t\"MEDIC_USEDOSAGE\": \"250.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_USEMODE\": \"ivgtt\",\n" +
                "\t\t\t\t\t\"MEDIC_USEPLAN\": \"一日1次\",\n" +
                "\t\t\t\t\t\"MEDIC_USEQUANTITY\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MODIFY_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"NO\": \"1\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"100303\",\n" +
                "\t\t\t\t\t\"PRES_ID\": \"200611100303000001\"\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"CREATE_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"GROUPNO\": \"2\",\n" +
                "\t\t\t\t\t\"MEDIC_CLASS\": \"Y\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE\": \"358540\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE_LOCAL\": \"358540\",\n" +
                "\t\t\t\t\t\"MEDIC_DAYS\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_GENERAL_NAME\": \"利巴韦林注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_NAME\": \"利巴韦林注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_QUANTITY\": \"7.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_REMARK\": \"\",\n" +
                "\t\t\t\t\t\"MEDIC_SELF\": \"0\",\n" +
                "\t\t\t\t\t\"MEDIC_SPEC\": \"注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_TYPE\": \"358540\",\n" +
                "\t\t\t\t\t\"MEDIC_USECODE\": \"qd\",\n" +
                "\t\t\t\t\t\"MEDIC_USEDOSAGE\": \"0.7000\",\n" +
                "\t\t\t\t\t\"MEDIC_USEMODE\": \"ivgtt\",\n" +
                "\t\t\t\t\t\"MEDIC_USEPLAN\": \"一日1次\",\n" +
                "\t\t\t\t\t\"MEDIC_USEQUANTITY\": \"7.0000\",\n" +
                "\t\t\t\t\t\"MODIFY_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"NO\": \"2\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"100303\",\n" +
                "\t\t\t\t\t\"PRES_ID\": \"200611100303000001\"\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"CREATE_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"GROUPNO\": \"2\",\n" +
                "\t\t\t\t\t\"MEDIC_CLASS\": \"Y\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE\": \"424479\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE_LOCAL\": \"424479\",\n" +
                "\t\t\t\t\t\"MEDIC_DAYS\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_GENERAL_NAME\": \"维生素C注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_NAME\": \"维生素C注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_QUANTITY\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_REMARK\": \"\",\n" +
                "\t\t\t\t\t\"MEDIC_SELF\": \"0\",\n" +
                "\t\t\t\t\t\"MEDIC_SPEC\": \"注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_TYPE\": \"424479\",\n" +
                "\t\t\t\t\t\"MEDIC_USECODE\": \"qd\",\n" +
                "\t\t\t\t\t\"MEDIC_USEDOSAGE\": \"10.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_USEMODE\": \"ivgtt\",\n" +
                "\t\t\t\t\t\"MEDIC_USEPLAN\": \"一日1次\",\n" +
                "\t\t\t\t\t\"MEDIC_USEQUANTITY\": \"2.0000\",\n" +
                "\t\t\t\t\t\"MODIFY_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"NO\": \"3\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"100303\",\n" +
                "\t\t\t\t\t\"PRES_ID\": \"200611100303000001\"\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"CREATE_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"GROUPNO\": \"3\",\n" +
                "\t\t\t\t\t\"MEDIC_CLASS\": \"Y\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE\": \"432139\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE_LOCAL\": \"432139\",\n" +
                "\t\t\t\t\t\"MEDIC_DAYS\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_GENERAL_NAME\": \"葡萄糖注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_NAME\": \"葡萄糖注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_QUANTITY\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_REMARK\": \"\",\n" +
                "\t\t\t\t\t\"MEDIC_SELF\": \"0\",\n" +
                "\t\t\t\t\t\"MEDIC_SPEC\": \"注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_TYPE\": \"432139\",\n" +
                "\t\t\t\t\t\"MEDIC_USECODE\": \"qd\",\n" +
                "\t\t\t\t\t\"MEDIC_USEDOSAGE\": \"250.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_USEMODE\": \"ivgtt\",\n" +
                "\t\t\t\t\t\"MEDIC_USEPLAN\": \"一日1次\",\n" +
                "\t\t\t\t\t\"MEDIC_USEQUANTITY\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MODIFY_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"NO\": \"1\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"100303\",\n" +
                "\t\t\t\t\t\"PRES_ID\": \"200611100303000001\"\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"CREATE_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"GROUPNO\": \"3\",\n" +
                "\t\t\t\t\t\"MEDIC_CLASS\": \"Z\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE\": \"462637\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE_LOCAL\": \"462637\",\n" +
                "\t\t\t\t\t\"MEDIC_DAYS\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_GENERAL_NAME\": \"清开灵注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_NAME\": \"清开灵注射液\",\n" +
                "\t\t\t\t\t\"MEDIC_QUANTITY\": \"2.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_REMARK\": \"\",\n" +
                "\t\t\t\t\t\"MEDIC_SELF\": \"0\",\n" +
                "\t\t\t\t\t\"MEDIC_SPEC\": \"静脉注射针剂\",\n" +
                "\t\t\t\t\t\"MEDIC_TYPE\": \"462637\",\n" +
                "\t\t\t\t\t\"MEDIC_USECODE\": \"qd\",\n" +
                "\t\t\t\t\t\"MEDIC_USEDOSAGE\": \"20.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_USEMODE\": \"ivgtt\",\n" +
                "\t\t\t\t\t\"MEDIC_USEPLAN\": \"一日1次\",\n" +
                "\t\t\t\t\t\"MEDIC_USEQUANTITY\": \"2.0000\",\n" +
                "\t\t\t\t\t\"MODIFY_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"NO\": \"2\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"100303\",\n" +
                "\t\t\t\t\t\"PRES_ID\": \"200611100303000001\"\n" +
                "\t\t\t\t}, {\n" +
                "\t\t\t\t\t\"CREATE_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"GROUPNO\": \"4\",\n" +
                "\t\t\t\t\t\"MEDIC_CLASS\": \"Y\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE\": \"328832\",\n" +
                "\t\t\t\t\t\"MEDIC_CODE_LOCAL\": \"328832\",\n" +
                "\t\t\t\t\t\"MEDIC_DAYS\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_GENERAL_NAME\": \"头孢克肟胶囊\",\n" +
                "\t\t\t\t\t\"MEDIC_NAME\": \"头孢克肟胶囊\",\n" +
                "\t\t\t\t\t\"MEDIC_QUANTITY\": \"1.0000\",\n" +
                "\t\t\t\t\t\"MEDIC_REMARK\": \"\",\n" +
                "\t\t\t\t\t\"MEDIC_SELF\": \"0\",\n" +
                "\t\t\t\t\t\"MEDIC_SPEC\": \"硬胶囊\",\n" +
                "\t\t\t\t\t\"MEDIC_TYPE\": \"328832\",\n" +
                "\t\t\t\t\t\"MEDIC_USECODE\": \"bid\",\n" +
                "\t\t\t\t\t\"MEDIC_USEDOSAGE\": \"0.2000\",\n" +
                "\t\t\t\t\t\"MEDIC_USEMODE\": \"po\",\n" +
                "\t\t\t\t\t\"MEDIC_USEPLAN\": \"一日2次\",\n" +
                "\t\t\t\t\t\"MEDIC_USEQUANTITY\": \"2.0000\",\n" +
                "\t\t\t\t\t\"MODIFY_TIME\": \"2020-06-11 08:47:35\",\n" +
                "\t\t\t\t\t\"NO\": \"1\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"100303\",\n" +
                "\t\t\t\t\t\"PRES_ID\": \"200611100303000001\"\n" +
                "\t\t\t\t}]\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}");
        System.out.println(xmlstr);
    }
}