package com.example.testmybatis.insert.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 业务权限表实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KttranAuthority
{

    private String fSvrCode; //业务编号
    private BigDecimal fUrId = null; //用户序号
    private String fAuth; //权限（1不限制）
    private String fIsLog; //日志(0/1记录) add by mhh 20160123
    private String fGroupId; //业务分离编号  add by mhh 20160411

}

