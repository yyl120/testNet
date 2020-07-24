package com.example.testmybatis.Common;


import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;

public class XmlJsonUtils {

    /**
     * JSON(数组)字符串转换成XML字符串
     * （必须引入 xom-1.1.jar）
     * @param jsonString
     * @return
     */
    public  String json2xml(String jsonString) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.setTypeHintsEnabled(false); // 去除 节点中type类型
        String xml = xmlSerializer.write(JSONSerializer.toJSON(jsonString));
        xml = xml.replace("<o>", "").replace("</o>", "");
     //   xml = xml.replace("<e>", "").replace("</e>", "");
        xml = xml.replaceAll("\r\n", "").concat("\r\n");
        System.out.println(xml);
        return xml;
    }

/*
    public static void main(String[] args) {
        XmlJsonUtils json=new XmlJsonUtils();
        String str="{\n" +
                "\t\"body\": {\n" +
                "\t\t\"head\": {\n" +
                "\t\t\t\"password\": \"123\",\n" +
                "\t\t\t\"trans_no\": \"C01.02.00.00\",\n" +
                "\t\t\t\"userid\": \"test\"\n" +
                "\t\t},\n" +
                "\t\t\"resquest\": {\n" +
                "\t\t\t\"C01.02.00.00\": {\n" +
                "\t\t\t\t\"CLINIC_INFOR\": {\n" +
                "\t\t\t\t\t\"CARDID\": \"\",\n" +
                "\t\t\t\t\t\"CLINIC_ID\": \"200603200032000003\",\n" +
                "\t\t\t\t\t\"CLINIC_TIME\": \"2020-06-03 14:02:02\",\n" +
                "\t\t\t\t\t\"DIAGNOSE\": \"肠病毒性脑炎\",\n" +
                "\t\t\t\t\t\"EMP_ID\": \"100016\",\n" +
                "\t\t\t\t\t\"ICD10\": \"A85.000\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"200032\",\n" +
                "\t\t\t\t\t\"REGISTER_ID\": \"200603200032000003\",\n" +
                "\t\t\t\t\t\"REMARKS\": \"\",\n" +
                "\t\t\t\t\t\"TYPE\": \"2\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t\"DISEASE_INFOR\": [{\n" +
                "\t\t\t\t\t\"CLINIC_ID\": \"200603200032000003\",\n" +
                "\t\t\t\t\t\"DEPT_CODE_ORG\": \"200525200032000001\",\n" +
                "\t\t\t\t\t\"DEPT_NAME_ORG\": \"内科\",\n" +
                "\t\t\t\t\t\"DIAGNOSE_TIME\": \"2020-06-03 14:02:32\",\n" +
                "\t\t\t\t\t\"DISEASE_NAME\": \"肠病毒性脑炎\",\n" +
                "\t\t\t\t\t\"EMP_ID\": \"100016\",\n" +
                "\t\t\t\t\t\"EMP_ID_ORG\": \"200032000001\",\n" +
                "\t\t\t\t\t\"EMP_NAME_ORG\": \"牟yx001\",\n" +
                "\t\t\t\t\t\"ICD\": \"A85.000\",\n" +
                "\t\t\t\t\t\"NO\": \"1\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"200032\",\n" +
                "\t\t\t\t\t\"REG_TIME\": \"2020-06-03 13:53:51\",\n" +
                "\t\t\t\t\t\"REMARKS\": \"\"\n" +
                "\t\t\t\t}],\n" +
                "\t\t\t\t\"PERSON_INFOR\": {\n" +
                "\t\t\t\t\t\"BIRTHDAY\": \"1995-06-03\",\n" +
                "\t\t\t\t\t\"CARDID\": \"20060300000002\",\n" +
                "\t\t\t\t\t\"CF_TYPE\": \"01\",\n" +
                "\t\t\t\t\t\"CITY\": \"\",\n" +
                "\t\t\t\t\t\"COMMUNITY\": \"\",\n" +
                "\t\t\t\t\t\"COUNTYCODE\": \"\",\n" +
                "\t\t\t\t\t\"CREATE_TIME\": \"2020-06-03 13:53:51\",\n" +
                "\t\t\t\t\t\"EMAIL\": \"\",\n" +
                "\t\t\t\t\t\"EXIGENCE_NAME\": \"\",\n" +
                "\t\t\t\t\t\"EXIGENCE_PHONE\": \"\",\n" +
                "\t\t\t\t\t\"HEALTH_ID\": \"20060300000002\",\n" +
                "\t\t\t\t\t\"HOME_PHONE\": \"\",\n" +
                "\t\t\t\t\t\"IDCARD\": \"\",\n" +
                "\t\t\t\t\t\"MOBILE\": \"\",\n" +
                "\t\t\t\t\t\"MODIFY_TIME\": \"2020-06-03 13:53:51\",\n" +
                "\t\t\t\t\t\"NAME\": \"哒哒哒\",\n" +
                "\t\t\t\t\t\"ORG_CODE\": \"200032\",\n" +
                "\t\t\t\t\t\"PROVINCE\": \"\",\n" +
                "\t\t\t\t\t\"SEX\": \"1\",\n" +
                "\t\t\t\t\t\"TOWN\": \"\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        json.json2xml(str);
    }
*/

}

