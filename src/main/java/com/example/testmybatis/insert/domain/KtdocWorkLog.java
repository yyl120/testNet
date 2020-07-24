package com.example.testmybatis.insert.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KtdocWorkLog {  //门诊工作日志
    private String ORG_CODE;//医院编号
    private String CLINIC_ID;//接诊编号
    private String REGISTER_ID;//门诊号
    private String EMP_ID;//医生工号
    private String DEPT_ID;//科室id
    private String CLINIC_TIME;//接诊时间
    private String SICKEN_TIME;//发病日期
    private String PERSONID;//病人编号
    private String TYPE;//接诊类型 1急诊2初诊3复诊4会诊
    private String DIAGNOSE;//诊断
    private String ICD10;//ICD码
    private String OPERTATION;//主要处理
    private String RESULT;//病人去向
    private String REMARKS;//备注
    private String CARDID;//卡号
    private String CARDNUM;//卡编号(保险号)
    private String REGISTER_CODE;//门诊号(整合)
    private String ZYMOSIS;//传染病
    private String FLU;//流感
    private String BRANCH_CODE;//分点编码
    private String PARENTS_NAME;//家长姓名
    private String RISK;//危重
    private String GREEN_CHANNEL;//绿色通道
    private String TRANS;//转诊单
    private String SPILE;//插管
    private String CHRONIC;//慢病
    private String STATE;//完成标记 Y表示完成
    private String END_TIME;//诊毕时间
    private String COMPANY;//
    private String SICK_ID;//病人编号
    private String START_TIME;//接诊时间
    private String SYMPTOM_CODE;//疾病症状编码（江苏专用）
    private String DEPT_CODE;//

}
