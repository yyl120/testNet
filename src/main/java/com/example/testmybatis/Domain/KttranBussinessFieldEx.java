package com.example.testmybatis.Domain;


import com.example.testmybatis.insert.domain.KttranBussinessField;
import com.example.testmybatis.insert.domain.KttranBussinessFieldLmtRang;
import com.example.testmybatis.insert.domain.KttranBussinessFieldValue;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 业务属性配置表实体
 */
@Data
public class KttranBussinessFieldEx extends KttranBussinessField
{

    /**
     当前取值情况
     */
    public KttranBussinessFieldValue FieldValue;
    /**
     当前值域范围
     */
    public List<KttranBussinessFieldLmtRang> FieldLmtRang;
    /**
     当前HASH结构KEY值
     */
    public String HashCurrentKey;
    /**
     全局HASH结构KEY值
     */
    public String HashGlobalKey;
    /**
     当前XML路径
     */
    public String XPath;

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion Model

    public final void Init(KttranBussinessField kbf)
    {
        this.setFBsCode(kbf.getFBsCode());//.setFBsCode(kbf.getFBsCode()) = ;
        this.setFBsfAttach(kbf.getFBsfAttach());// = kbf.BsfAttach;
        this.setFBsfCode(kbf.getFBsfCode());// = kbf.BsfCode;
        this.setFBsfColumn(kbf.getFBsfColumn());// = kbf.BsfColumn;
        this.setFBsfComments(kbf.getFBsfComments()) ;//= kbf.BsfComments;
        this.setFBsfDefault(kbf.getFBsfDefault());// = kbf.BsfDefault;
        this.setFBsfLength (kbf.getFBsfLength());//= kbf.BsfLength;
        this.setFBsfMandatory(kbf.getFBsfMandatory());// = kbf.BsfMandatory;
        this.setFBsfName(kbf.getFBsfName());// = kbf.BsfName;
        this.setFBsfOptype(kbf.getFBsfOptype());// = kbf.BsfOptype;
        this.setFBsfOrd(kbf.getFBsfOrd());
        this.setFBsfPk(kbf.getFBsfPk());
        this.setFBsfPrecision(kbf.getFBsfPrecision()) ;
        this.setFBsfScale(kbf.getFBsfScale()) ;
        this.setFBsfType(kbf.getFBsfType());
        this.setFBsfXml(kbf.getFBsfXml()) ;
        this.setFIsPreRunSql(kbf.getFIsPreRunSql()) ;
        this.HashCurrentKey = kbf.getFBsfCode();
        this.setFBsfDirection(kbf.getFBsfDirection()) ;
        this.setFBsfLmtRang(kbf.getFBsfLmtRang()) ;
        this.setFBsfDictionary(kbf.getFBsfDictionary()) ;
        this.setFBsfEncrypt(kbf.getFBsfEncrypt()) ;
        this.setFBsfCursorType(kbf.getFBsfCursorType()) ;
        this.HashGlobalKey = String.format("%1$s.%2$s", kbf.getFBsCode(), kbf.getFBsfCode());
        this.setFBsfIsRecord(kbf.getFBsfIsRecord()) ;

    }

     /*bfe.setFBsCode(kbf.getFBsCode());//.setFBsCode(kbf.getFBsCode()) = ;
        bfe.setFBsfAttach(kbf.getFBsfAttach());// = kbf.BsfAttach;
        bfe.setFBsfCode(kbf.getFBsfCode());// = kbf.BsfCode;
        bfe.setFBsfColumn(kbf.getFBsfColumn());// = kbf.BsfColumn;
        bfe.setFBsfComments(kbf.getFBsfComments()) ;//= kbf.BsfComments;
        bfe.setFBsfDefault(kbf.getFBsfDefault());// = kbf.BsfDefault;
        bfe.setFBsfLength (kbf.getFBsfLength());//= kbf.BsfLength;
        bfe.setFBsfMandatory(kbf.getFBsfMandatory());// = kbf.BsfMandatory;
        bfe.setFBsfName(kbf.getFBsfName());// = kbf.BsfName;
        bfe.setFBsfOptype(kbf.getFBsfOptype());// = kbf.BsfOptype;
        bfe.setFBsfOrd(kbf.getFBsfOrd());
        bfe.setFBsfPk(kbf.getFBsfPk());
        bfe.setFBsfPrecision(kbf.getFBsfPrecision()) ;
        bfe.setFBsfScale(kbf.getFBsfScale()) ;
        bfe.setFBsfType(kbf.getFBsfType());
        bfe.setFBsfXml(kbf.getFBsfXml()) ;
        bfe.setFIsPreRunSql(kbf.getFIsPreRunSql()) ;
    bfe.HashCurrentKey = kbf.getFBsfCode();
        bfe.setFBsfDirection(kbf.getFBsfDirection()) ;
        bfe.setFBsfLmtRang(kbf.getFBsfLmtRang()) ;
        bfe.setFBsfDictionary(kbf.getFBsfDictionary()) ;
        bfe.setFBsfEncrypt(kbf.getFBsfEncrypt()) ;
        bfe.setFBsfCursorType(kbf.getFBsfCursorType()) ;
    bfe.HashGlobalKey = String.format("%1$s.%2$s", kbf.getFBsCode(), kbf.getFBsfCode());
        bfe.setFBsfIsRecord(kbf.getFBsfIsRecord()) ;*/
    public static KttranBussinessFieldEx Copy(KttranBussinessFieldEx kbf)
    {
        KttranBussinessFieldEx n = new KttranBussinessFieldEx();
        n.setFBsCode(kbf.getFBsCode());//.setFBsCode(kbf.getFBsCode()) = ;
        n.setFBsfAttach(kbf.getFBsfAttach());// = kbf.BsfAttach;
        n.setFBsfCode(kbf.getFBsfCode());// = kbf.BsfCode;
        n.setFBsfColumn(kbf.getFBsfColumn());// = kbf.BsfColumn;
        n.setFBsfComments(kbf.getFBsfComments()) ;
        n.setFBsfDefault(kbf.getFBsfDefault());// = kbf.BsfDefault;
        n.setFBsfLength (kbf.getFBsfLength());//= kbf.BsfLength;
        n.setFBsfMandatory(kbf.getFBsfMandatory());// = kbf.BsfMandatory;
        n.setFBsfName(kbf.getFBsfName());// = kbf.BsfName;
        n.setFBsfOptype(kbf.getFBsfOptype());// = kbf.BsfOptype;
        n.setFBsfOrd(kbf.getFBsfOrd());
        n.setFBsfPk(kbf.getFBsfPk());
        n.setFBsfPrecision(kbf.getFBsfPrecision()) ;
        n.setFBsfScale(kbf.getFBsfScale()) ;
        n.setFBsfType(kbf.getFBsfType());
        n.setFBsfXml(kbf.getFBsfXml()) ;
        n.setFIsPreRunSql(kbf.getFIsPreRunSql()) ;
       // this.HashCurrentKey = kbf.getFBsfCode();
        n.setFBsfDirection(kbf.getFBsfDirection()) ;
        n.setFBsfLmtRang(kbf.getFBsfLmtRang()) ;
        n.setFBsfDictionary(kbf.getFBsfDictionary()) ;
        n.setFBsfEncrypt(kbf.getFBsfEncrypt()) ;
        n.setFBsfCursorType(kbf.getFBsfCursorType()) ;
        n.HashGlobalKey = String.format("%1$s.%2$s", kbf.getFBsCode(), kbf.getFBsfCode());
        n.setFBsfIsRecord(kbf.getFBsfIsRecord()) ;
        n.FieldValue = kbf.FieldValue;
        //n.FieldLmtRang = kbf.FieldLmtRang;
        n.HashCurrentKey = kbf.HashCurrentKey;
        n.HashGlobalKey = kbf.HashGlobalKey;
        n.XPath = kbf.XPath;

        if ("1".equals(kbf.getFBsfLmtRang()) && kbf.FieldLmtRang != null)
        {
            n.FieldLmtRang = new ArrayList<KttranBussinessFieldLmtRang>();
            for (KttranBussinessFieldLmtRang rang : kbf.FieldLmtRang)
            {
                n.getFieldLmtRang().add(KttranBussinessFieldLmtRang.Copy(rang));
            }
        }
        return n;
    }



}

