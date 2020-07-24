package com.example.testmybatis.insert.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 业务属性值配置表实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KttranBussinessFieldValue {

    private String fBsCode; //业务编号
    private String fBsfCode; //属性编号
    private BigDecimal fBsfvSn = null; //当前序号
    private String fBsfvMethod; //取值方法(1XML绝对路径/2XML相对路径/3函数/4SQL语句/5存储过程/6其他表字段)
    private String fBsfvCount; //取值语句
    private String fBsfvActive; //当前状态,y激活/n关闭
    private String fGroupId; //业务分离编号  add by mhh 20160411
}