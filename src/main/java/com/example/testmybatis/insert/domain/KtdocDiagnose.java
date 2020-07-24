package com.example.testmybatis.insert.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KtdocDiagnose {//门诊医生诊断表

    private String ORG_CODE;//医院编号
    private String CLINIC_ID;//接诊编号
    private String NO;//序号
    private String ICD;//ICD码
    private String DISEASE_NAME;//疾病名称
    private String REMARKS;//备注
    private String EMP_ID;//医生工号
}
