package com.example.testmybatis.insert.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KtdocPrescription {//门诊医生处方
    private String  ORG_CODE;
    private String  PRES_ID;
    private String  CLINIC_ID;
    private String  RECORD_TIME;
    private String  TYPE;
    private String  EMP_ID;
    private String  CATEGORY;
}
