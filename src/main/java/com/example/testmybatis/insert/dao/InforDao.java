package com.example.testmybatis.insert.dao;

import com.example.testmybatis.insert.domain.KtdocDiagnose;
import com.example.testmybatis.insert.domain.KtdocWorkLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("InforDao")
@Mapper
public interface InforDao {
    //通过自定义函数生成persoinid；
    public String getFunction(@Param("PERSONID") String PERSONID, @Param("HEALTH_ID") String HEALTH_ID, @Param("ORG_CODE") String ORG_CODE, @Param("IDCARD") String IDCARD, @Param("CARDNUM") String CARDNUM, @Param("CARDID") String CARDID, @Param("NAME") String NAME);

    //修改或保存个人基本信息表
    public void PERSON_INFOR(@Param("PERSONID") String PERSONID, @Param("HEALTH_ID") String HEALTH_ID, @Param("PROVINCE") String PROVINCE, @Param("COUNTYCODE") String COUNTYCODE, @Param("TOWN") String TOWN, @Param("COMMUNITY") String COMMUNITY, @Param("NAME") String NAME,@Param("SEX") String SEX, @Param("BIRTHDAY") String BIRTHDAY, @Param("CF_TYPE") String CF_TYPE, @Param("IDCARD") String IDCARD, @Param("BLOOD") String BLOOD, @Param("MARRIED") String MARRIED, @Param("EMAIL") String EMAIL, @Param("RESIDE") String RESIDE, @Param("HOME_ADDRESS") String HOME_ADDRESS, @Param("HOME_PHONE") String HOME_PHONE, @Param("HOME_ZIPCODE") String HOME_ZIPCODE, @Param("COMPANY") String COMPANY, @Param("COMPANY_ADDRESS") String COMPANY_ADDRESS, @Param("COMPANY_ZIPCODE") String COMPANY_ZIPCODE, @Param("OCCUPATION") String OCCUPATION, @Param("COMPANY_PHONE") String COMPANY_PHONE, @Param("MOBILE") String MOBILE, @Param("EXIGENCE_NAME") String EXIGENCE_NAME, @Param("EXIGENCE_PHONE") String EXIGENCE_PHONE, @Param("CREATE_TIME") String CREATE_TIME, @Param("MODIFY_TIME") String MODIFY_TIME, @Param("DELETE_FLAG") Integer DELETE_FLAG, @Param("REPORT_TIME") String REPORT_TIME, @Param("ORG_CODE") String ORG_CODE, @Param("MODIFY_ORG_CODE") String MODIFY_ORG_CODE, @Param("DETAIL") String DETAIL, @Param("RH_TYPE") String RH_TYPE, @Param("CITY") String CITY, @Param("UPDATE_FLAG") Integer UPDATE_FLAG, @Param("CARDNUM") String CARDNUM, @Param("CARDID") String CARDID);

    //删除一条门诊医生诊断
    public void deleteKtdocDiagnose(@Param("ORG_CODE") String ORG_CODE ,@Param("CLINIC_ID") String CLINIC_ID );

    //删除一条门诊工作日志
    public void deleteKtdocWorkLog(@Param("ORG_CODE") String ORG_CODE ,@Param("CLINIC_ID") String CLINIC_ID );

    //保存一条门诊工作日志
    public void insertKtdocWorkLog(KtdocWorkLog ktdocWorkLog);

public void updateKtdocDiagnse(KtdocDiagnose ktdocDiagnose);//KTDOC_DIAGNOSE
    /*public void insertKtdocWorkLog(@Param("ORG_CODE ") String ORG_CODE,
                                   @Param("CLINIC_ID ") String CLINIC_ID,
                                   @Param("REGISTER_ID ") String REGISTER_ID,
                                   @Param("EMP_ID ") String EMP_ID,
                                   @Param("DEPT_ID ") String DEPT_ID,
                                   @Param("CLINIC_TIME ") String CLINIC_TIME,
                                   @Param("SICKEN_TIME ") String SICKEN_TIME,
                                   @Param("PERSONID ") String PERSONID,
                                   @Param("TYPE ") String TYPE,
                                   @Param("DIAGNOSE ") String DIAGNOSE,
                                   @Param("ICD10 ") String ICD10,
                                   @Param("OPERTATION ") String OPERTATION,
                                   @Param("RESULT ") String RESULT,
                                   @Param("REMARKS ") String REMARKS,
                                   @Param("CARDID ") String CARDID,
                                   @Param("CARDNUM ") String CARDNUM,
                                   @Param("REGISTER_CODE ") String REGISTER_CODE,
                                   @Param("ZYMOSIS ") String ZYMOSIS,
                                   @Param("FLU ") String FLU,
                                   @Param("BRANCH_CODE ") String BRANCH_CODE,
                                   @Param("PARENTS_NAME ") String PARENTS_NAME,
                                   @Param("RISK ") String RISK,
                                   @Param("GREEN_CHANNEL ") String GREEN_CHANNEL,
                                   @Param("TRANS ") String TRANS,
                                   @Param("SPILE ") String SPILE,
                                   @Param("CHRONIC ") String CHRONIC,
                                   @Param("STATE ") String STATE,
                                   @Param("END_TIME ") String END_TIME,
                                   @Param("COMPANY ") String COMPANY,
                                   @Param("SICK_ID ") String SICK_ID,
                                   @Param("START_TIME ") String START_TIME,
                                   @Param("SYMPTOM_CODE ") String SYMPTOM_CODE,
                                   @Param("DEPT_CODE ") String DEPT_CODE






                                   );*/
    /*//修改或保存患者过敏史信息
    public void  KTHRA_ALLERGY_NEW(@Param("ORG_CODE") String ORG_CODE,
                                   @Param("SICK_ID") String SICK_ID,
                                   @Param("AI_SN") String AI_SN,
                                   @Param("HEALTH_ID") String HEALTH_ID,
                                   @Param("AI_TYPE") String AI_TYPE,
                                   @Param("ITEM") String ITEM,
                                   @Param("REASON") String REASON,
                                   @Param("START_DATE") String START_DATE,
                                   @Param("SOURCE_NO") String SOURCE_NO,
                                   @Param("CREATE_ORG_CODE") String CREATE_ORG_CODE,
                                   @Param("CREATE_EMP_ID") String CREATE_EMP_ID,
                                   @Param("CREATE_TIME") String CREATE_TIME,
                                   @Param("REMARK") String REMARK);*/

    //修改或保存患者诊断信息
    public void  KTDOC_DIAGNOSE();
}
