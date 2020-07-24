package com.example.testmybatis.Domain;


import com.example.testmybatis.insert.domain.KttranBusiness;
import com.example.testmybatis.insert.domain.KttranBussinessField;
import org.dom4j.Node;
import org.dom4j.XPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;




public class KttranBusinessEx extends KttranBusiness {

    private List<KttranBusinessEx> Childs;
    private KttranBusinessEx Parent;
    public List<KttranBussinessFieldEx> Fields;//字段集合
    private List<KttranCheckDictionary> CheckValids;//需要审核验证的词典
    private int Index;//当前业务索引
    private int Count;// 当前业务总数
    private boolean HasContainOutput;//是否包含输出类型
    private boolean HasContainCursor;//是否包含游标输出类型（输出记录集合）
    private String CursorName;//游标名（输出记录集合）
    private String CheckValidArray;//   使用逗号隔开的验证词典组合
    private String XPath;//当前取XML的XPath
    public Hashtable Params;//参数收集表，格式如{BS_CODE}_{BSF_CODE}     防止出现多条记录
    private Node node;
    private XPath xPath;

    public List<KttranBusinessEx> getChilds() {
        return Childs;
    }

    public void setChilds(List<KttranBusinessEx> childs) {
        Childs = childs;
    }

    public KttranBusinessEx getParent() {
        return Parent;
    }

    public void setParent(KttranBusinessEx parent) {
        Parent = parent;
    }

    public List<KttranBussinessFieldEx> getFields() {
        return Fields;
    }

    public void setFields(List<KttranBussinessFieldEx> fields) {
        Fields = fields;
    }

    public List<KttranCheckDictionary> getCheckValids() {
        return CheckValids;
    }

    public void setCheckValids(List<KttranCheckDictionary> checkValids) {
        CheckValids = checkValids;
    }

    public int getIndex() {
        return Index;
    }

    public void setIndex(int index) {
        Index = index;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public boolean isHasContainOutput() {
        return HasContainOutput;
    }

    public void setHasContainOutput(boolean hasContainOutput) {
        HasContainOutput = hasContainOutput;
    }

    public boolean isHasContainCursor() {
        return HasContainCursor;
    }

    public void setHasContainCursor(boolean hasContainCursor) {
        HasContainCursor = hasContainCursor;
    }

    public String getCursorName() {
        return CursorName;
    }

    public void setCursorName(String cursorName) {
        CursorName = cursorName;
    }

    public String getCheckValidArray() {
        return CheckValidArray;
    }

    public void setCheckValidArray(String checkValidArray) {
        CheckValidArray = checkValidArray;
    }

    public String getXPath() {
        return XPath;
    }

    public void setXPath(String XPath) {
        this.XPath = XPath;
    }

    public Hashtable getParams() {
        return Params;
    }

    public void setParams(Hashtable params) {
        Params = params;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public org.dom4j.XPath getxPath() {
        return xPath;
    }

    public void setxPath(org.dom4j.XPath xPath) {
        this.xPath = xPath;
    }

    public KttranBusinessEx() {
        Params=new Hashtable();

        //setParams(new Hashtable());
        getParams().put("SYS_IBATIS_COUNT", 0);
        Childs=new ArrayList<KttranBusinessEx>();
        Fields=new ArrayList<KttranBussinessFieldEx>();
        CheckValids=new ArrayList<KttranCheckDictionary>();
        setCheckValidArray("");
    }


    public final void Init(KttranBusiness kb) {

        this.setBsCode(kb.getBsCode());
        this.setBsComments(kb.getBsComments());
        this.setBsCountType(kb.getBsCountType());
        this.setBsMemo(kb.getBsMemo());
        this.setBsName(kb.getBsName());
        this.setBsOrder(kb.getBsOrder());
        this.setBsParentCode(kb.getBsParentCode());
        this.setBsRepsql(kb.getBsRepsql());
        this.setBsRspsql(kb.getBsRspsql());
        this.setBsStatusProc(kb.getBsStatusProc());
        this.setBsStatusSaveSql(kb.getBsStatusSaveSql());
        this.setBsStatusSaveTable(kb.getBsStatusSaveTable());
        this.setBsStatusSchSql(kb.getBsStatusSchSql());
        this.setBsStatusSchTable(kb.getBsStatusSchTable());
        this.setBsTable(kb.getBsTable());
        this.setUseStatus(kb.getUseStatus());
        this.setSvrCode(kb.getSvrCode());
        this.setBsSimpleParam(kb.getBsSimpleParam());
        this.setBsOutXml(kb.getBsOutXml());
        this.setBsTable(kb.getBsTable());
        this.setBsCheckValid(kb.getBsCheckValid());
        this.setBsStruct(kb.getBsStruct());
        this.setDianaCount(kb.getDianaCount());
        this.setTransaction(kb.getTransaction());
    }

    public static KttranBusinessEx Copy(KttranBusinessEx kb) {
        KttranBusinessEx n = new KttranBusinessEx();
        n.setBsCode(kb.getBsCode());
        n.setBsComments(kb.getBsComments());
        n.setBsCountType(kb.getBsCountType());
        n.setBsMemo(kb.getBsMemo());
        n.setBsName(kb.getBsName());
        n.setBsOrder(kb.getBsOrder());
        n.setBsParentCode(kb.getBsParentCode());
        n.setBsRepsql(kb.getBsRepsql());
        n.setBsRspsql(kb.getBsRspsql());
        n.setBsStatusProc(kb.getBsStatusProc());
        n.setBsStatusSaveSql(kb.getBsStatusSaveSql());
        n.setBsStatusSaveTable(kb.getBsStatusSaveTable());
        n.setBsStatusSchSql(kb.getBsStatusSchSql());
        n.setBsStatusSchTable(kb.getBsStatusSchTable());
        n.setBsTable(kb.getBsTable());
        n.setUseStatus(kb.getUseStatus());
        n.setSvrCode(kb.getSvrCode());
        n.setHasContainOutput(kb.isHasContainOutput());
        n.setHasContainCursor(kb.isHasContainCursor());
        n.setCursorName(kb.getCursorName());
        n.setParent(kb.getParent());
        n.setIndex(kb.getIndex());
        n.setCount(kb.getCount());
        n.setBsSimpleParam(kb.getBsSimpleParam());
        n.setBsOutXml(kb.getBsOutXml());
        n.setBsCheckValid(kb.getBsCheckValid());
        n.setCheckValidArray(kb.getCheckValidArray());
        n.setBsMandatory(kb.getBsMandatory());
        n.setBsStruct(kb.getBsStruct());
        n.setXPath(kb.getXPath());
        n.setDianaCount(kb.getDianaCount());
        n.setTransaction(kb.getTransaction());
        if (kb.getCheckValids() != null) {
            for (KttranCheckDictionary item : kb.getCheckValids()) {
                n.getCheckValids().add(item);
            }
        }

       if (kb.getFields() != null) {

            for (KttranBussinessFieldEx field : kb.getFields()) {
             KttranBussinessFieldEx ex=   KttranBussinessFieldEx.Copy(field);
                n.Fields.add(ex);

            }
        }
        if (kb.getChilds()!=null){
            for (KttranBusinessEx child : kb.getChilds()){
                n.getChilds().add(Copy(child));
            }
        }
        return n;
    }
}

    //private boolean disposed = false;

  /*  public final void Dispose() throws IOException
    {
        Dispose(true);
        GC.SuppressFinalize(this);
    }*/


   /* protected void Dispose(boolean disposing) throws IOException {

        if (!this.disposed)
        {
            if (disposing)
            {
                // 释放托管资源

                if (getChilds() != null)
                {
                    for (KttranBusinessEx ex : getChilds())
                    {
                        ex.close();
                    }
                }
                if (getFields() != null)
                {
                    for (KttranBussinessFieldEx field : getFields())
                    {
                        field.Dispose();
                    }
                    getFields().clear();
                    this.setFields(null);
                }
                if (getCheckValids() != null)
                {
                    getCheckValids().clear();
                    this.setCheckValids(null);
                }
                if (getParams() != null)
                {
                    getParams().clear();
                    setParams(null);
                }
                this.setNode(null);
                this.setParent(null);
            }
            // 释放非托管资源，如果disposing为false,
            // 只有托管资源被释放
            // 注意这里不是线程安全的
        }
        disposed = true;
    }*/



