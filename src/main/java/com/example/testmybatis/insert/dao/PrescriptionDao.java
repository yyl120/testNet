package com.example.testmybatis.insert.dao;

import com.example.testmybatis.insert.domain.KtdocElectroniccaseUp;
import com.example.testmybatis.insert.domain.KtdocPrescription;
import com.example.testmybatis.insert.domain.KtdocPrescriptionList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("PrescriptionDao")
@Mapper
public interface PrescriptionDao {
//处方信息
    public void deletektdocPrescription(@Param("ORG_CODE") String ORG_CODE , @Param("PRES_ID") String PRES_ID);

    public void deletektdocPrescriptionList(@Param("ORG_CODE") String ORG_CODE , @Param("PRES_ID") String PRES_ID);

    public void updateOrInsertdocPrescription(KtdocPrescription ktdocPrescription);

    public String limitORG_CODE(@Param("ORG_CODE") String ORG_CODE);

    public String limitEMP_ID(@Param("ORG_CODE") String ORG_CODE,@Param("EMP_ID") String EMP_ID) ;


    //门诊医生处方明细批量插入或更新
//,@Param("ORG_CODE") String ORG_CODE , @Param("PRES_ID") String PRES_ID
    public void updateOrInsertPrescriptionList(@Param("list")List<KtdocPrescriptionList> list);

//门诊电子病历
public void updateOrInsertElectroniccaseUp(KtdocElectroniccaseUp electroniccaseUp);
}
