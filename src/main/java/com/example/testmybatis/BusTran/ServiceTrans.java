package com.example.testmybatis.BusTran;




//import com.example.Core.BaseServiceLib.Common.Loggers;
import com.example.testmybatis.Common.*;
import com.example.testmybatis.Domain.*;
import com.example.testmybatis.insert.dao.*;
import com.example.testmybatis.insert.domain.*;
import com.example.testmybatis.testxmlparser.XmlBody;
import com.example.testmybatis.testxmlparser.XmlParser;
import com.github.abel533.sql.SqlMapper;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;



@Service
public class ServiceTrans  {

    public static AuthorityDao authorityDao;

    public static BusinessDao businessDao;

    public static UserInfoDao userInfoDao;

    public static InforDao inforDao;

    public static PrescriptionDao prescriptionDao;

    @Autowired
    public void setAuthorityService(AuthorityDao authorityDao){
        ServiceTrans.authorityDao=authorityDao;
    }

    @Autowired
   public void setBusinessService(BusinessDao businessDao){
       ServiceTrans.businessDao=businessDao;
   }

    @Autowired
    public void setuserInfoService(UserInfoDao userInfoDao){
        ServiceTrans.userInfoDao=userInfoDao;
    }

    @Autowired
    public void setAuthorityService(InforDao inforDao){
        ServiceTrans.inforDao=inforDao;
    }

    @Autowired
    public void setPrescriptionService(PrescriptionDao prescriptionDao){
        ServiceTrans.prescriptionDao=prescriptionDao;
    }

    @Value("${fw.GroupId}")
   private String GroupId;

    @Value("${fw.OrgCode}")
    private String OrgCode;

    @Value("${fw.No}")
    private String No;

    @Value("${fw.IsValid}")
    private String IsValid;

    @Value("${fw.DianaCount}")
    private String DianaCount;

    @Value("${fw.BsTable}")
    private String BsTable;

    @Value("${fw.BsFeildTable}")
    private String BsFeildTable;

    @Value("${fw.BsFeildValueTable}")
    private String BsFeildValueTable;




    private static Object _synRoot = new Object(); //竞争锁
    private static Hashtable hashDictionary = new Hashtable(); //缓存词典表取值
    private static Object _synDictionaryRoot = new Object(); //词典表竞争锁
    private static Hashtable hasRegex = new Hashtable();
    private static Object _synRegexRoot = new Object(); //正则表达式竞争锁
    private static int _dianacount = 0; //重启条数
    public static SqlMapper _mapper = null; //数据库访问MAPPER
    private static Object thisLock = new Object();
    private static Object processLock = new Object();

    private static KttranBusinessEx  businessEx=new KttranBusinessEx();
    private static List<KttranBusinessEx> busConfig; //系统可访问的业务集合 目录树结构
    private static KttranBussinessFieldEx bussinessFieldEx=new KttranBussinessFieldEx();
    public KttranServiceconfigEx svrCurrent; //运行时生成的当前服务 目录熟结构
    public static List<KttranBusiness> businesses=null;//=businessDao.selectBusiness();
    public static List<KttranBussinessField> bussinessFields=null;
    public static List<KttranBussinessFieldLmtRang> bussinessFieldLmtRangs=null;
    public static List<KttranBussinessFieldValue> bussinessFieldValues=null;


    private String runtype = "0"; //执行方式，0不限制,1执行批处理,2执行查询输出
    public SqlMapper mapper = null; //数据库访问MAPPER
    public XmlParser Parser = new XmlParser(); //XML解析器
    public List<KttranBussinessFieldReferences> busPreRunSql; //运行时需要提前运行的业务对象
    public List<KttranBussinessFieldReferences> feildReferences; //运行时引用其他字段的字段对象
    public List<KttranBussinessFieldReferences> feildDictionary; //运行时需要值域验证

    private static List<KttranUserinfo> users = null; //初始化后生成的当前所有用户
    private static List<KttranAuthority> auths = null; //初始化后当前所有的权限
    private static Map<String, KttranBusiness> bussinesslist = new HashMap<String, KttranBusiness>();
    private static List<KttranBusinessVaild> bsvaild = null;

    private LocalDateTime dateTime1 = LocalDateTime.MIN; //记录提交XML前当前时间
    private boolean istestflag = true; //测试用户标记
    MessageCode msg;
    private static boolean isValid = false;
    private UUID LogNo = UUID.randomUUID();
    private StringBuilder xmlKey = new StringBuilder(""); //add by cd20180125
    private StringBuilder xmlValue = new StringBuilder(""); //add by cd20180125


    /**
     * 获取系统初始化后生成的所有用户对象
     */
    protected final List<KttranUserinfo> getUsers() {
        return ServiceTrans.users;
    }

    /**
     * 获取系统初始化后生成的所有用户权限对象
     */
    protected final List<KttranAuthority> getAuths() {
        return ServiceTrans.auths;
    }


   public String ProcessJson(String json) throws IOException, ParseException {

           String xml= JsonToXml.jsonToXml(json);
       String returnString=Process(xml);
       return returnString;


      /* XmlJsonUtils jsons=new XmlJsonUtils();
       String xml=jsons.json2xml(json);
//xml.replace("<e>","").replace("</e>","");
      // System.out.println("XML:++  "+xml);
           String returnString=Process(xml);

       return returnString;
*/
   }


    /**
     * 数据请求入口
     *
     * @param xml
     * @return
     */
    public String Process(String xml) throws IOException, ParseException {


           String monthday1 = LocalDateTime.now().toString();
        LogNo = UUID.randomUUID();
        dateTime1 = LocalDateTime.now();
        XmlBody request = null;
        StringBuilder strXml = new StringBuilder();
        //string tmpXml = null;
        String actno = null;
        String sresult = null;
        boolean IssuedFlag = false;
        KttranUserinfo currentUser = null;//用户信息实体类

            request = XmlDeserialize(xml);//XmlDeserialize解析XML

            //  log.Info("request:" + request.toString());//输出字符串到日志文件
            actno = request.getHead().getTransAct();

            //tmpXml = AfterXmlDeserialize(request, busConfig);
            //if (!string.IsNullOrEmpty(tmpXml)) return tmpXml;

            svrCurrent = new KttranServiceconfigEx();//服务配置表实体
            svrCurrent.XmlDoc = Parser.xmldoc;      //需要提前处理的对象
            busPreRunSql = new ArrayList<KttranBussinessFieldReferences>();
            feildReferences = new ArrayList<KttranBussinessFieldReferences>();
            feildDictionary = new ArrayList<KttranBussinessFieldReferences>();

            if (request.getCode() == MessageCode.成功) {

                ///#region 用户权限判断
                //日志记录标记
                IssuedFlag = false;
                //机构业务验证
              //  System.out.println("Y"+SystemInfo.getInstance().Config.getRightValid());
               /*if (SystemInfo.getInstance().Config.getRightValid().equals("Y")) {
                    System.out.println("getRightValid++++++Y");

                    //ValidateUser用户验证 2902行                                      .net语法
                    ValidateUser(request.getHead().getUserID(), request.getHead().getPassword(), users, msg);

                    if (request.getCode() == MessageCode.成功) {
                        //log.Exp("ValidateUser:"+request.Code);
                        //业务验证 2965行
                        ValidateBusiness(currentUser.getFUrId(), request.getHead().getTransAct(), auths, msg);
                        //log.Exp("ValidateBusiness:" + request.Code);
                    }
                }*/

                if (request.getCode() == MessageCode.成功) {
                    //用户验证是否测试用户
                    //istestflag = ValidateUserIsTest(request.Head.UserID, users, request as Message);
                    //创建数据对象
                    //log.Info("开始对象创建");
                    //log.Exp("开始对象创建");  mapper 数据库访问的


                   // System.out.println("list"+list);
                   // HashMap<String,List<>> map=new HashMap<>();
                    //rmap.put("a",businessDao.selectBusiness());
                   /* if (CreateBusObjectData1(businesses,request,svrCurrent)){
                        System.out.println("666");
                    }*/
                //    System.out.println("busConfig"+busConfig.toString());

                    Init();
                   // System.out.println("busConfig"+busConfig);
                    if (CreateBusObjectData( busConfig, request, svrCurrent))//732行
                    {
                        //        log.Info("完成对象创建");
                      //  String msg = "";//msg从String
                        //处理XML
                        //       log.Info("开始处理XML");

                       /* tangible.RefObject<String> tempRef_msg = new tangible.RefObject<String>(msg);
                        tangible.RefObject<String> tempRef_msg2 = new tangible.RefObject<String>(msg);*/
                        // ProcessSaveData  1695
                        /*if (select(svrCurrent,strXml,msg)){

                        }*/


                        if (ProcessSaveData( svrCurrent, request) && ProcessResponseData( svrCurrent, strXml, request)) {

                            request.Code = MessageCode.成功;
                            request.Info="成功";
                        } else {

                            request.Code = MessageCode.业务处理失败;
                            request.Info = "业务处理失败";
                        }
                    }
                }
            }
        KttranBusiness  bs= bussinesslist.get(request.getHead().getTransAct());
            if ("Y".equals(bs.getBsResponseHead())){
                strXml.insert(0, String.format("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n<body>\r\n<head>\r\n<ret_code>%1$s</ret_code>\r\n<ret_info>%2$s%3$s</ret_info>\r\n</head>\r\n<response>\r\n", request.Code, request.Code, (request.Code == MessageCode.成功) ? "" : ",CSB处理报错,原因:" + request.Info != null ? request.Info : ""));
                strXml.append("<log_no>" + LogNo + SystemInfo.getInstance().Config.getLogIc().toString() + "</log_no>\r\n</response>\r\n</body>");
            } else {
                strXml.insert(0, String.format("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\r\n<body>\r\n<response>\r\n<ret_code>%1$s</ret_code>\r\n<ret_info>%2$s</ret_info>", request.getCode(),  (request.getCode() == MessageCode.成功) ? request.getInfo(): ",CSB处理报错,原因:" + request.getInfo()));
                strXml.append("<log_no>" + LogNo +  "</log_no>\r\n</response>\r\n</body>");
            }//

            //输出
            sresult = strXml.toString();

        return sresult;
    }

    /**
     * 初始化操作
     */
    public  void Init() {
        if (busConfig == null) {
            synchronized (_synRoot) {
                if (busConfig == null) {
                    //初始化系统信息
                    //SystemInfo.SetSystemInfo();
                    //初始化业务配置
                   // _mapper = SystemInfo.DbDao.GetLocalSqlMap();

                   // ServiceTrans tran = new ServiceTrans();


                    busConfig = ConfTables();

                   // if (!StringHelper.isNullOrEmpty(SystemInfo.getInstance().Config.setIsValid();) && IsValid.equals("1")) {
                     if (true){//IsValid验证
                        isValid = true;
                    }
                    int idcount = 0;
                     //_dianacount  重启条数
                    OutObject<Integer> tempOut_idcount = new OutObject<Integer>();
                    /*if (tryParseInt(SystemInfo.Instance.Config.DianaCount, tempOut_idcount)) {
                        idcount = tempOut_idcount.argValue;
                        _dianacount = idcount;
                    } else {
                        idcount = tempOut_idcount.argValue;
                    }*/
                }
            }
        }
    }



    public final boolean Decode(String BsCode, String OrgCode, String DesCode) {
        String key = BsCode + OrgCode;
        if (DesCode.equals(key)) {
            return true;
        } else {
            return false;
        }
    }

    //C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region ConfTables生成系统可访问的业务集合
 public final List<KttranBusinessEx> ConfTables() {
        boolean b = false;
        List<KttranBusinessEx> table = null;
        try {
            //初始化各路服务
            System.out.println("GroupId"+GroupId);
            KttranAuthority tempVar = new KttranAuthority();
            tempVar.setFGroupId(GroupId)  ;
            auths=authorityDao.selectAuthority(tempVar.getFGroupId());
            System.out.println(auths);
             users=userInfoDao.selectUserinfo();

            KttranBusiness tempVar2 = new KttranBusiness();
            tempVar2.setGroupId(GroupId);//.GroupId = CommServiceConfig.Instance.GroupId;
            List<KttranBusiness> tmpList =businessDao.selectBusiness(tempVar2.getGroupId()); //mapper.<KttranBusiness>QueryForList("GeAll_KttranBusiness", tempVar2);


            //bsvaild = mapper.QueryForList<KttranBusinessVaild>("GetAll_KttranBusinessVaild", null);

            KttranBusiness tempVar3 = new KttranBusiness();
            tempVar3.setGroupId(GroupId);
            List<KttranBusiness> table3 =businessDao.selectAllBusiness();//mapper.<KttranBusiness>QueryForList("GeAll_KttranBusiness", tempVar3);
            //log.Info("ServiceTrans Init:初始化KttranBusiness,共" + table3.Count + "条数据");

            for (KttranBusiness business : tmpList) {
                //business.setSerialString(true);//.SerialFlag = true; //Decode(business.BsCode, "00294042", business.SerialString);
                bussinesslist.put(business.getBsCode(), business);
            }
           // System.out.println("1");
            KttranBussinessField tempVar4 = new KttranBussinessField();
            tempVar4.setFGroupId(GroupId);
            List<KttranBussinessField> table4 =businessDao.selectBusinessField(tempVar4.getFGroupId());
            //log.Info("ServiceTrans Init:初始化KttranBussinessField,共" + table4.Count + "条数据");

            KttranBussinessFieldValue tempVar5 = new KttranBussinessFieldValue();
            tempVar5.setFGroupId(GroupId);
            List<KttranBussinessFieldValue> table5 =businessDao.selectBussinessFieldValue(tempVar5.getFGroupId());
            //log.Info("ServiceTrans Init:初始化KttranBussinessFieldValue,共" + table5.Count + "条数据");

            KttranBussinessFieldLmtRang tempVar6 = new KttranBussinessFieldLmtRang();
            tempVar6.setFGroupId(GroupId);
            List<KttranBussinessFieldLmtRang> table6 =businessDao.selectBusinessFieldLmtRang(tempVar6.getFGroupId());
            //log.Info("ServiceTrans Init:初始化KttranBussinessFieldLmtRang,共" + table6.Count + "条数据");


            table = new ArrayList<KttranBusinessEx>();
            b = ConfBusinessTables(table, table3, table4, table5, table6, null, null);

            table3 = null;
            table4 = null;
            table5 = null;
        } catch (RuntimeException ex) {
             ex.getStackTrace();
        }
        if (b == false) {
            throw new RuntimeException("初始化失败 ConfTables,请检查日志 ");
        }
     //System.out.println("table+  "+table);
        return table;
    }


    public final boolean ConfBusinessTables(List<KttranBusinessEx> table, List<KttranBusiness> kb,
                                            List<KttranBussinessField> kbf, List<KttranBussinessFieldValue> kbfv,
                                            List<KttranBussinessFieldLmtRang> kbfl, KttranBusinessEx Parent,
                                            KttranBusinessEx Root) {
        String bscode = ((Parent == null) ? null: Parent.getBsCode());//StringUtils.equals(bscode,p.getBsParentCode()
       // System.out.println("bscode "+bscode);//bscode!=null && p.getBsParentCode()!=null &&
      //bscode==p.getBsParentCode()
             List<KttranBusiness> optional=kb.stream().filter(p -> (bscode==null ? bscode==p.getBsParentCode():bscode.equals(p.getBsParentCode()))).collect(Collectors.toList());//多条数据
             for(KttranBusiness bus:optional){
                 KttranBusinessEx x = new KttranBusinessEx();
                 x.Init(bus);
                 x.setParent(Parent);
                 //x.setParent(Parent);
                 if (x.getParent() == null) {
                     Root = x;
                     if (Root.getCheckValids() == null) {
                         Root.setCheckValids(new ArrayList<KttranCheckDictionary>());
                     }
                 }
                 List<KttranBussinessField>  optional1=kbf.stream().filter(p -> bus.getBsCode().equals(p.getFBsCode())).collect(Collectors.toList());
                 for (KttranBussinessField y1:optional1) {
                 //KttranBussinessField y1=optional1.get();
                    // System.out.println("y1"+y1);
                   //  System.out.println("y1 "+y1);
                     KttranBussinessFieldEx y2 = new KttranBussinessFieldEx();
                     y2.Init(y1);
               //      System.out.println("y2  "+y2);
               //  System.out.println("y2.getFBsfDirection()  "+y2.getFBsfDirection());
                     //              1
                   //  System.out.println("y2.getFBsfDirection()  "+y2.getFBsfDirection());

                     if (y2.getFBsfDirection().equals("4") || y2.getFBsfDirection().equals("5")) //输出设置 为1的多
                     {
                         x.setHasContainOutput(true);//HasContainOutput = true;
                         KttranBusinessEx setoutput = x;
                         while (setoutput.getParent() != null) {
                             setoutput = setoutput.getParent();
                             setoutput.setHasContainOutput(true);
                         }
                     }//字段方向(1输入/2输出/3输入输出)

                     if (y2.getFBsfDirection().equals("2") || y2.getFBsfDirection().equals("3")) //输出设置
                     {
                         x.setHasContainOutput(true);//HasContainOutput = true;
                         KttranBusinessEx setoutput = x;
                         while (setoutput.getParent() != null) {
                             setoutput = setoutput.getParent();
                             setoutput.setHasContainOutput(true);
                         }
                     }//是否关键字（y是/n否）                当前取值情况                         //取值方法(1XML绝对路径/2XML
                   //  System.out.println("y2.getFBsfPk()"+y2.getFBsfPk());    !StringHelper.isNullOrEmpty(y2.FieldValue.getFBsfvMethod()) &&
                     if ("Y".equals(y2.getFBsfPk()) && y2.FieldValue != null &&
                             (y2.FieldValue.getFBsfvMethod().equals("3") || y2.FieldValue.getFBsfvMethod().equals("4"))) //如果为关键字，值由函数生成
                     {        //               取值方法
                         y2.setFIsPreRunSql("Y");
                     }
                //     System.out.println("y2.getFBsfType()"+y2.getFBsfType());
                     if ("5".equals(y2.getFBsfType())) {
                         x.setHasContainCursor(true);
                         x.setCursorName(y2.getFBsfName());
                     } else {
                         x.setHasContainCursor(false);
                     }
                //    System.out.println("y2.getFBsfAttach()"+y2.getFBsfAttach());
                     if ("1".equals(y2.getFBsfAttach())) {//赋值字段（1需要/2不需要）

                         List<KttranBussinessFieldValue>   kvx=kbfv.stream().filter(kv -> bus.getBsCode().equals(kv.getFBsCode())
                                 && y1.getFBsfCode().equals(kv.getFBsfCode()) && "Y".equals(kv.getFBsfvActive())).collect(Collectors.toList());//.collect(Collectors.toList());
                        // System.out.println(kvx);
                         for (KttranBussinessFieldValue fv:kvx){
                             y2.FieldValue=fv;
                         }
                     }
                         //y2.FieldValue= kvx.get();
                         //System.out.println("kvx.get()  "+kvx.get());

                         /* for(:kbfv){
                             if (bus.getBsCode().equals(kv.getFBsCode()) && y1.getFBsfCode().equals(kv.getFBsCode()) && "Y".equals(kv.getFBsfvActive())){

                             }*/

                           //Stream<KttranBussinessFieldValue> stream =kbfv.stream().filter(d -> d.getFBsCode() == bus.getBsCode() && d.getFBsfCode() == y1.getFBsfCode() && "Y".equals(d.getFBsfvActive()));
                            //y2.setFieldValue((KttranBussinessFieldValue)stream)  ;//*//强转 不知是否有问题

                //     System.out.println("y2.getFBsfLmtRang()"+y2.getFBsfLmtRang());
                     if ("1".equals(y2.getFBsfLmtRang())) {//取值限定(1是/2否) ("1")
                         List<KttranBussinessFieldLmtRang>   kls=kbfl.stream().filter(kl -> bus.getBsCode().equals(kl.getFBsCode())
                                 && y1.getFBsfCode().equals(kl.getFBsfCode()) && "Y".equals(kl.getFBsfvActive())).collect(Collectors.toList());
                            y2.FieldLmtRang=kls;
                     //    System.out.println("klx   "+kls);

                         }


                     x.Fields.add(y2);
                    // System.out.println("x"+x);
                     if (!StringHelper.isNullOrEmpty(y2.getFBsfDictionary())) {
                         KttranCheckDictionary checkdict = new KttranCheckDictionary();
                         checkdict.setCheckType(y2.getFBsfDictionary());//.setFCheckType() = y2.BsfDictionary;
                         checkdict.setBsfCode(y2.getFBsCode());
                         checkdict.setBsfCode(y2.getFBsfCode());
                         if (Root.getCheckValids().stream().filter(p -> p.getCheckType() == y2.getFBsfDictionary()) == null) {
                             Root.getCheckValids().add(checkdict);
                             Root.setCheckValidArray(Root.getCheckValidArray() + checkdict.getCheckType() + ",")  ;
                         }
                     }

                 }
                 table.add(x);

                 //*System.out.println("x.getChilds() "+x.getChilds());

                 //System.out.println("x  "+x);
                // System.out.println("Root  "+Root);
                 if (ConfBusinessTables(x.getChilds(), kb, kbf, kbfv, kbfl, x, Root) == false) {
                     return false;
                 }
             }
             return true;
    }


    ///#region 解析XML

    /**
     * 解析XML，发生在Process方法中，开始对输入的XML转换成可识别的对象
     *
     * @param xml
     * @return
     */
    public XmlBody XmlDeserialize(String xml) {
        return Parser.XmlDeserialize(xml);
    }

    /**
     * XML解析后发生的事件，如果不为空则自动进入循环，否则直接将结果输出
     *
     * @param xmlbody  当前XML解析后的对象
     * @param business 所有业务结构
     * @return
     */
    public String AfterXmlDeserialize(XmlBody xmlbody, List<KttranBusinessEx> business) {
        return null;
    }

    ///#region CreateData创建数据

        /**
         * 创建数据
         *
         * @param buss 系统可访问的业务集合
         * @param body 客户提供的XML结构对象
         * @param svr  运行时生成的当前服务
         * @return 是否成功创建
         */                                                       //业务配置表实体                        服务配置表
    public boolean CreateBusObjectData( List<KttranBusinessEx> buss, XmlBody body, KttranServiceconfigEx svr) {
        try {
          //  System.out.println("buss+"+buss);
            System.out.println("code "+body.getHead().getTransAct());
            List<KttranBusinessEx> operation=buss.stream().filter(p -> body.getHead().getTransAct().equals(p.getBsCode())).collect(Collectors.toList());//多条数据
            //System.out.println(operation);
            if (operation.size()==0){
                body.Code=MessageCode.业务不存在;
                body.Info="请检查你的交易码";
            }
           List<KttranBusinessEx> result= operation.stream().filter(p ->"Y".equals(p.getUseStatus())).collect(Collectors.toList());
            if (result.size()==0){
                body.Code=MessageCode.业务被停用;
                body.Info="业务暂停停用";
            }

            svr.setSvrCode(body.getHead().getTransAct());
            body.Code = MessageCode.默认;
                 for (KttranBusinessEx bus: operation) {

                     if (bus.getDianaCount().intValue() > 0) { //自动提交行数
                         _dianacount = bus.getDianaCount().intValue();
                     }
                     //收集需要的验证词典项目
                     if ("Y".equals(bus.getBsCheckValid())) {//是否审核验证
                         for (KttranCheckDictionary item : bus.getCheckValids()) {//bus.getCheckValids()需要审核验证的词典
                             List<KttranCheckDictionary> items = svr.getCheckValids().stream().filter(p -> item.getCheckType().equals(p.getCheckType())).collect(Collectors.toList());
                             if (items.size() == 0) {
                                 svr.getCheckValids().add(item);
                                 svr.setCheckValidArray(svr.getCheckValidArray() + item.getCheckType() + ",");
                             }
                        /*if (svr.getCheckValids().SingleOrDefault(p -> p.CheckType == item.CheckType) == null) {
                            svr.CheckValids.Add(item);
                            svr.CheckValidArray = svr.CheckValidArray + item.CheckType + ",";
                        }*/
                         }   //假装验证通过
                         svr.CheckValid = true;
                     }

                     if ("Y".equals(bus.getBsSimpleParam())) {//简单参数结构
                         //log.Info("创建对象-设置值-业务结构点开始BsSimpleParam=Y");
                         bus.setNode(body.getResquest());//解决部分无参值和无结构
                         //处理当前业务下的XML属性节点

                         KttranBusinessEx newbus = KttranBusinessEx.Copy(bus);//.KttranBusinessEx.Copy(bus);
                         newbus.setNode(body.getResquest());
                         newbus.setIndex(0);//.Index = 0;
                         newbus.setXPath("/body/resquest/");//.XPath = "/body/resquest/";
                         newbus.setParent(null);//.Parent = null;
                         // svr.Businiess.Add(newbus);
                         svr.Businiess.add(newbus);
//                                                       body instanceof Message ?  body : null
                         if (SetObject(newbus, newbus.getNode(), body, svr, ("/body" + ((body.getResquest() == null) ? "" : "/resquest")), true) == false) {
                             return false;
                         }

                         //log.Info("创建对象-设置值-业务结构点结束");
                     } else {
                         if (body.getResquest() == null) {
                             body.Code = MessageCode.业务处理失败;
                             body.Info = "XML结构存在问题,未找到/body/resquest结构";
                             return false;
                         }
                         // bus.getBsName()    C01.02.00.00
                         List<Node> nodes = body.getResquest().selectNodes(bus.getBsName());

                         // XmlNodeList nodes = body.Resquest.SelectNodes(bus.BsName);
                         String xPath = "/body/resquest/" + bus.getBsName();
                         if (nodes == null || nodes.size() == 0) {//size()  .net Count()
                             body.Code = MessageCode.业务处理失败;
                             body.Info = "XML结构存在问题,未找到" + xPath + "结构";
                             return false;
                         }

                         int i = 0;
                         for (Node node : nodes) {
                             //log.Info("创建对象-设置值-业务结构点开始BsSimpleParam=N");
                            // KttranBusinessEx newbus = KttranBusinessEx.Copy(bus);
                             KttranBusinessEx newbus = bus;
                             newbus.setNode(node);// = node;
                             newbus.setIndex(i);// = i;
                             newbus.setXPath(xPath);// = xPath;
                             newbus.setParent(null);// = null;
                             i++;
                           //  System.out.println(node);
                             //处理当前业务下的XML属性节点
                             if (SetObject(newbus, node, body , svr, xPath, false) == false) {
                                 return false;
                             }
                             //log.Info("创建对象-设置值-业务结构点结束");
                             svr.Businiess.add(newbus);
                         }
                     }
                 }

                if (isValid) {
                    //词典审核验证处理
                    if (svr.CheckValid && ValidCheckDictionary(body, svr) == false) {
                        return false;
                    }

                    if (SetObjectSp( svr, body) == false) {
                        return false;
                    }
                    //值域验证处理
                    if (VaildLmtRang(body) == false) {
                        return false;
                    }
                } else {

                    if (SetObjectSp( svr, body) == false) {
                        return false;
                    }
                }


        } catch (RuntimeException | ParseException ex) {
            // log.Info(ex.getMessage() + "123");
            return false;
        }
        return true;
    }

           /* Iterable<KttranBusinessEx> results =buss.listIterator();
            .<KttranBusinessEx>Where(s -> s.BsCode == body.Head.TransAct);*///判断业务编号
    //log.Info("创建对象-取到对象");
           /* if (results.size() == 0) {
                body.Code = MessageCode.业务不存在;
                body.Info = "请检查您的交易码";
                return false;
            }
            if (results.Count(s -> s.UseStatus.equals("Y")) == 0) {
                body.Code = MessageCode.业务被停用;
                body.Info = "业务暂停使用";
                return false;
            }*/


    /**
     * 设置直接可读取的字段值
     *
     * @param result 当前业务对象
     * @param item   当前业务对象对应的XML节点
     * @param msg    需要返回的消息
     * @param svr    运行时生成的当前服务
     * @return 是否设置值成功
     */
    public boolean SetObject(KttranBusinessEx result, Node item, Message msg, KttranServiceconfigEx svr, String xpath, boolean isSimpleParam) throws ParseException {
        //log.Info("创建对象-设置值-循环树-开始");
        if (result.getFields() != null) {
            //log.Exp("result_count;" + result.Fields.Count);
            for (KttranBussinessFieldEx field : result.getFields()) {//业务属性配置表实体
                field.HashGlobalKey = String.format("%1$s[%2$s]", xpath + "/" + field.HashCurrentKey, result.getIndex());
                //xpath/field.HashCurrentKey[result.getIndex()]
                //System.out.println(field.HashGlobalKey+"  +" +result.getIndex());
                //        是否提前运行语句返回值(Y是/N否)            是否关键字（Y是/N否）             赋值字段（1需要/2不需要）        值方法(1XML绝对路径/2XML相对路径/3函数/4SQL语句/5存储过程/6其他表字段)
                if ("Y".equals(field.getFIsPreRunSql()) || ("Y".equals(field.getFBsfPk()) && "1".equals(field.getFBsfAttach()) && "3".equals(field.FieldValue.getFBsfvMethod()))) {
                    KttranBussinessFieldReferences feild2 = new KttranBussinessFieldReferences();//需要提前处理的对象
                    feild2.setBusiness(result);
                    feild2.setFeild(field);
                    busPreRunSql.add(feild2);
                    //log.Exp("HashGlobalKey:" + field.HashGlobalKey);
                }
                String Value = "";
                String spValue = ""; //特殊取值
                Object _value = null;
                if ("1".equals(field.getFBsfAttach())) {//需要赋值的字段（1需要/2不需要）
                    ///#region 特殊取值
                    if (field.FieldValue == null) {//当前取值情况
                        msg.setCode(MessageCode.系统设置错误未配置特殊取值);
                    } else {
                        if ("1".equals(field.FieldValue.getFBsfvMethod())) //XML绝对路径
                        {
                            Object tempVar = svr.XmlDoc.selectSingleNode(field.FieldValue.getFBsfvCount());
                            System.out.println("temp"+tempVar);
                            Element e = (Element)tempVar;// instanceof Element ? (Element) tempVar : null;

                            //add by cd20180125BEGIN
                            if ("1".equals(field.getFBsfIsRecord())) { //{记录数据(1是/0否)
                                if (e == null) //节点未上传的情况
                                {
                                    xmlKey.append(field.getFBsfName() + "|");
                                    xmlValue.append("|");
                                } else {
                                    String XmlNode = e.getName();//e.Name; //获得节点标签名
                                    String XmlValue = e.getText(); //获取节点类容
                                    xmlKey.append(XmlNode + "|"); // "1223";
                                    xmlValue.append(XmlValue + "|");
                                }
                            }
                            //add by cd20180125END
                            //                                                     getBsfMandatory是否强制（Y是/N否）
                            if (e == null && "Y".equals(field.getFBsfMandatory())) {
                                msg.Code=MessageCode.字段未提供值;
                                msg.Info="字段未提供值1";
                                //  msg = String.format("字段名:%1$s.%2$s.数据集名称:%3$s.字段名称:%4$s.交易号:%5$s.日志号:%6$s",
                                //        result.getBsName(), field.getBsfName(), result.getBsComments(), field.getBsfComments(), field.BsCode, LogNo);
                                return false;
                            }
                            Value = ((e == null) ? "" : e.getText());//getFBsfvMethod 取值方法(2XML相对路径)
                        } else if (item != null && field.FieldValue.getFBsfvMethod().equals("2") && !StringHelper.isNullOrEmpty(field.FieldValue.getFBsfvMethod())) //XML相对路径
                        {

                            Node node = item;

                            //add by cd20180125BEGIN
                            if ("1".equals(field.getFBsfIsRecord())) {//记录数据
                                Object tempVar2 = node.selectSingleNode(field.FieldValue.getFBsfvCount());//.s.SelectSingleNode(field.FieldValue.BsfvCount);
                                Element ee = tempVar2 instanceof Element ? (Element) tempVar2 : null;
                                if (ee == null) //节点未上传的情况
                                {
                                    xmlKey.append(field.getFBsfName() + "|");
                                    xmlValue.append("|");
                                } else {
                                    String XmlNode = ee.getName();//.Name; //获得节点标签名
                                    String XmlValue = ee.getText();//.InnerText; //获取节点类容
                                    xmlKey.append(XmlNode + "|"); // "1223";
                                    xmlValue.append(XmlValue + "|");
                                }
                            }
                            //add by cd20180125END

                            if (node == null) {
                                msg.setCode(MessageCode.XML解析失败);
                                //  msg = String.format("字段名:%1$s.%2$s未能找到其对应的相对路径.数据集名称:%3$s.字段名称:%4$s.交易号:%5$s.日志号:%6$s", result.BsName, field.BsfName, result.BsComments, field.BsfComments, field.BsCode, LogNo);
                                return false;
                            }
                            while (field.FieldValue.getFBsfvCount().startsWith("../")) {//取值语句是否以   ../  开头
                                field.FieldValue.setFBsfvCount(field.FieldValue.getFBsfvCount().substring(3));
                                node = node.getParent() != null ? node.getParent() : result.getParent().getNode();//得到父节点
                            }//node.selectSingleNode(field.FieldValue.getFBsfvCount());
                            Object tempVar3 = node.selectSingleNode(field.FieldValue.getFBsfvCount());
                            Element e = tempVar3 instanceof Element ? (Element) tempVar3 : null;
                            if (e == null && "Y".equals(field.getFBsfMandatory())) {
                                msg.Code=MessageCode.字段未提供值;
                                msg.Info="字段未提供值2";
                                //  msg.Info = String.format("字段名:%1$s.%2$s.数据集名称:%3$s.字段名称:%4$s.交易号:%5$s.日志号:%6$s", result.BsName, field.BsfName, result.BsComments, field.BsfComments, field.BsCode, LogNo);
                                return false;
                            }
                            Value = ((e == null) ? "" : e.getText());
                        } else if ("3".equals(field.FieldValue.getFBsfvMethod())) //函数 直接生成XML
                        {
                            //Value = field.FieldValue.BsfvCount;
                            if (item != null) {
                                Node e = item.selectSingleNode(field.getFBsfName());
                                if (e != null) {
                                    Value = e.getText(); //优先取XML值
                                }

                                //add by cd20180125BEGIN
                                if ("1".equals(field.getFBsfIsRecord())) {//记录数据 1记录
                                    String XmlNode = e.getName(); //获得节点标签名
                                    String XmlValue = ""; //获取节点类容
                                    xmlKey.append(XmlNode + "|"); // "1223";
                                    xmlValue.append(XmlValue + "|");
                                }
                                //add by cd20180125END
                            }
                            spValue = field.FieldValue.getFBsfvCount();
                            //log.Exp(field.FieldValue.BsfvMethod + "--" + Value.ToString());
                            //_value = spValue;
                            //Value = "";
                            //_value = "";
                        } else if ("4".equals(field.FieldValue.getFBsfvMethod())) //SQL语句 直接生成XML
                        {
                            //Value = "(" + field.FieldValue.BsfvCount + ")";//运算过程已经生成XML文件
                            if (item != null) {
                                Node e = item.selectSingleNode(field.getFBsfName());//.SelectSingleNode(field.BsfName);
                                if (e != null) {
                                    Value = e.getText(); //优先取XML值
                                }

                                //add by cd20180125BEGIN
                                if ("1".equals(field.getFBsfIsRecord())) {
                                    String XmlNode = e.getName(); //获得节点标签名
                                    String XmlValue = ""; //获取节点类容
                                    xmlKey.append(XmlNode + "|"); // "1223";
                                    xmlValue.append(XmlValue + "|");
                                }
                                //add by cd20180125BEGIN
                            }
                            spValue = field.FieldValue.getFBsfvCount();//得到取值语句
                            if ("Y".equals(result.getBsStatusProc())) {
                                _value = spValue;
                            }
                        } else if ("5".equals(field.FieldValue.getFBsfvMethod())) //存储过程 暂未实现
                        {
                            if (item != null) {
                                Node e = item.selectSingleNode(field.getFBsfName());//.SelectSingleNode(field.BsfName);
                                if (e != null) {
                                    Value = e.getText(); //优先取XML值
                                }
                            }
                            //string statement = string.Format("GetValue_CommSql_{0}_{1}_{2}",field.BsCode,field.BsfCode,field.FieldValue.BsfvSn);
                            //mapper.QueryForObject(statement,
                            //XmlElement e = svr.XmlDoc.SelectSingleNode(field.FieldValue.BsfvCount) as XmlElement;
                            //Value = e.Value;
                            spValue = field.FieldValue.getFBsfvCount();
                      //CLINIC_ID由其他字段生成
                        } else if ("6".equals(field.FieldValue.getFBsfvMethod())) //其他字段 暂未实现
                        {
                            //if (item != null)
                            //{
                            //    XmlNode e = item.SelectSingleNode(field.BsfName);
                            //    if (e != null) Value = e.InnerText;
                            //}
                            spValue = field.FieldValue.getFBsfvCount();

                            KttranBussinessFieldReferences feild2 = new KttranBussinessFieldReferences();
                            feild2.setBusiness(result);
                            feild2.setFeild(field);
                            feildReferences.add(feild2);
                            continue;
                        }
                    }
   //                              是否xml属性
                } else if ("Y".equals(field.getFBsfXml()) && item != null) {//Xml属性
                  //  System.out.println("field.getFBsfName() "+field.getFBsfName());

                    Node e = item.selectSingleNode(field.getFBsfName());//属性名称
                  //  System.out.println("e  +"+e);
                    if (e != null) {
                        Value = e.getText();
                    }

                    //add by cd20180125BEGIN  value=232
                    if ("1".equals(field.getFBsfIsRecord())) {
                        String XmlNode = e.getName(); //获得节点标签名
                        String XmlValue = e.getText(); //获取节点类容
                        xmlKey.append(XmlNode + "|"); // "1223";
                        xmlValue.append(XmlValue + "|");
                    }
                    //add by cd20180125BEGIN
                }

               // System.out.println("value++"+Value);
                boolean isNullOrEmpty = StringHelper.isNullOrEmpty(Value);
               // System.out.println(isNullOrEmpty);
               // System.out.println(StringHelper.isNullOrEmpty(spValue));
                //强制提供值
                if (("Y".equals(field.getFBsfMandatory()) || "Y".equals(field.getFBsfPk())) && isNullOrEmpty && StringHelper.isNullOrEmpty(spValue)) {
                    msg.setCode(MessageCode.字段未提供值);
                    msg.Info="字段未提供值3";
                    // msg.Info = String.format("字段名:%1$s.%2$s.数据集名称:%3$s.字段名称:%4$s.交易号:%5$s.日志号:%6$s", result.BsName, field.BsfName, result.BsComments, field.BsfComments, field.BsCode, LogNo);
                    return false;
                }
                if (isNullOrEmpty && field.getFBsfDefault() != null) {
                    Value = field.getFBsfDefault(); //给予默认值
                    isNullOrEmpty = false;
                }

                if (!isNullOrEmpty && _value == null) {// 取值语句
                    if ("1".equals(field.getFBsfType())) {//字段类型(1文本/2数字/3字节流/4日期/5游标/6Blog)

                        ///#region 文本类型处理   value=232
                        if (field.getFBsfLength() != null && Value.length() > field.getFBsfLength().intValue()) {//bigdecimal转int
                            msg.setCode(MessageCode.字段长度超出范围);
                            msg.Info = "字段长度超出范围";
                            ;
                            //  msg.Info = String.format("字段名:%1$s.%2$s.数据集名称:%3$s.字段名称:%4$s.交易号:%5$s.日志号:%6$s", result.BsName, field.BsfName, result.BsComments, field.BsfComments, field.BsCode, LogNo);
                            return false;
                        }
                        _value = Value != null ? Value : "";
                        //_value = (Value ?? "").Replace("&lt;", "<").Replace("&gt;", ">").Replace("&amp;", "&").Replace("&apos;", "’").Replace("&quot;", "\"");

                        ///#endregion
                    } else if ("2".equals(field.getFBsfType())) //{字段类型(1文本/2数字/3字节流/4日期/5游标/6Blog)
                    {
                        ///#region 数字类型处理

                        if (Value.equals("")) {
                            _value = null;
                        }

                        BigDecimal iv ;
                       // OutObject<BigDecimal> tempOut_iv = new OutObject<BigDecimal>();
                        if (!isNumeric(Value)) {
                          //  iv = tempOut_iv.argValue;
                            msg.setCode(MessageCode.非数字值);
                            msg.Info = "非数字值";

                            return false;
                        } else {
                            iv=new BigDecimal(Value);
                            //iv = tempOut_iv.argValue;
                        }
                        /*BigDecimal iv = new BigDecimal(0);
                        OutObject<BigDecimal> tempOut_iv = new OutObject<BigDecimal>();
                        if (!BigDecimal.valueOf(Value)) { //String 转  BigDecimal 不成功就返回false
                            iv = tempOut_iv.argValue;
                            msg.setCode(MessageCode.非数字值);// = ;
                            //   msg.Info = String.format("字段名:%1$s.%2$s,值为:%3$s.数据集名称:%4$s.字段名称:%5$s.交易号:%6$s.日志号:%7$s", result.BsName, field.BsfName, Value, result.BsComments, field.BsfComments, field.BsCode, LogNo);
                            return false;
                        } else {
                            iv = tempOut_iv.argValue;
                        }*/
                        String[] vv = Value.split("[.]", -1);
                        if (vv.length > 0 && vv[0].length() > field.getFBsfPrecision().intValue()) {
                            msg.setCode(MessageCode.字段精度超出范围);
                            msg.Info = "字段精度超出范围";

                            ;
                            //  msg.Info = String.format("字段名:%1$s.%2$s,输入值:%3$s,限定精度:%4$s.数据集名称:%5$s.字段名称:%6$s.交易号:%7$s.日志号:%8$s", result.BsName, field.BsfName, iv, field.BsfPrecision, result.BsComments, field.BsfComments, field.BsCode, LogNo);
                            return false;
                        }
                        if (vv.length > 1 && vv[1].length() > field.getFBsfScale().intValue()) {
                            msg.setCode(MessageCode.字段小数长度超出范围);
                            msg.Info = "字段小数长度超出范围";

                            //msg.Info = String.format("字段名:%1$s.%2$s,输入值:%3$s,限定小数长度:%4$s.数据集名称:%5$s.字段名称:%6$s.交易号:%7$s.日志号:%8$s", result.BsName, field.BsfName, iv, field.BsfScale, result.BsComments, field.BsfComments, field.BsCode, LogNo);
                            return false;
                        }
                        _value = iv;
                    }

                    ///#endregion
                    else if ("4".equals(field.getFBsfType())) {//字段类型为日期
                        ///#region 时间类型转化和处理
                        LocalDateTime date;
                        // String str=Value.replace('T',' ').replace('/','-');
                        if (DateTime((Value.replace('T', ' ').replace('/', '-')))) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            date = LocalDateTime.parse(Value.replace('T', ' ').replace('/', '-'),formatter);
                        } else if (IsDate(Value.replace('/', '-'))) {
                            LocalDate localDate = LocalDate.parse(Value.replace('/', '-'), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            date = localDate.atStartOfDay();
                            /*DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            date = LocalDateTime.parse(localDateTime,formatter);*/
                        } else if (Value.length() == 8) {
                            if (!isValidDate1(Value)) {
                                msg.setCode(MessageCode.非日期值);
                                msg.Info = "非日期值";

                            } else {
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String timeFormat = sdf.format(format.parse(Value));
                                System.out.println(timeFormat);
                                try {
                                    LocalDate localDate = LocalDate.parse(timeFormat, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                                    LocalDateTime localDateTime = localDate.atStartOfDay();
                                    String src = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    date = LocalDateTime.parse(src, df);
                                    System.out.println(date);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        } /*else if (Value.length() == 15) {
                            OutObject<LocalDateTime> tempOut_date2 = new OutObject<LocalDateTime>();
                            if (!LocalDateTime.TryParseExact(Value, "yyyyMMddTHHmmss", CultureInfo.InvariantCulture, DateTimeStyles.None, tempOut_date2)) {
                                date = tempOut_date2.argValue;
                                msg.setCode(MessageCode.非日期值);
                                // msg.Info = String.format("字段名:%1$s.%2$s,输入值:%3$s.数据集名称:%4$s.字段名称:%5$s.交易号:%6$s.日志号:%7$s", result.BsName, field.BsfName, Value, result.BsComments, field.BsfComments, field.BsCode, LogNo);
                                return false;
                            } else {
                                date = tempOut_date2.argValue;
                            }
                        } */ else if (Value.length() == 15) {
                            if (!isValidDate2(Value)) {
                                msg.setCode(MessageCode.非日期值);
                                msg.Info = "非日期值";

                            } else {
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String timeFormat = sdf.format(format.parse(Value));
                                System.out.println(timeFormat);
                                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                date = LocalDateTime.parse(timeFormat, df);//2019-11-01T10:01:01
                                System.out.println(date);
                            }
                        } else if (Value.length() == 14) {
                            if (!isValidDate3(Value)) {
                                msg.setCode(MessageCode.非日期值);
                                msg.Info = "非日期值";

                            } else {
                                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String timeFormat = sdf.format(format.parse(Value));
                                System.out.println(timeFormat);
                                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                date = LocalDateTime.parse(timeFormat, df);//2019-11-01T10:01:01
                                System.out.println(date);
                            }
                        }
                            _value = Value;

                    }
                }
                //_value  FBsfvCount取值语句的处理 /body/resquest/CS.03.01.01/SOURCE_NO
                if (isValid) {
                    ///#region 值域检验处理   取值限定(1是/2否)
                    if ("1".equals(field.getFBsfLmtRang()) && field.FieldLmtRang != null) {
                        if (_value == null && "N".equals(field.getFBsfMandatory())) {
                        } else {
                            for (KttranBussinessFieldLmtRang lmtRang : field.FieldLmtRang) {//业务参数值域设置
                                boolean chkRange = true;
                                //add qsw 返回正则表达式未匹配内容
                                String unRegStr = "";//取值方法(1指定值/2词典表/3正则表达式)
                                if ("1".equals(lmtRang.getFBsfvLmt())) //指定值
                                {
                                    chkRange = String.format("%1$s,", lmtRang.getFBsfvCount()).contains(String.format("%1$s,", _value));
                                } else if ("2".equals(lmtRang.getFBsfvLmt()) || "3".equals(lmtRang.getFBsfvLmt())) //词典
                                {
                                    KttranBussinessFieldReferences feild2 = new KttranBussinessFieldReferences();
                                    feild2.setBusiness(result);
                                    feild2.setFeild(field);
                                    feildDictionary.add(feild2);
                                } else if ("4".equals(lmtRang.getFBsfvLmt())) //正则表达式
                                   /*{
                                        String key = String.format("%1$s", lmtRang.getBsfvCount());
                                        Regex reg = hasRegex.get(key) instanceof Regex ? (Regex) hasRegex.get(key) : null;
                                        if (reg == null) {
                                            reg = new Regex(lmtRang.getBsfvCount(), RegexOptions.Compiled);
                                            synchronized (_synRegexRoot) {
                                                hasRegex.put(key, reg);
                                            }
                                        }
                                        chkRange = reg.IsMatch(_value.toString());

                                        //处理正则表达式
                                        if (chkRange == false) {
                                            if (StringHelper.isNullOrEmpty(_value.toString())) {
                                                String[] splitStr = _value.toString().trim().split("[|]", -1);
                                                for (int i = 0; i < splitStr.length; i++) {
                                                    if (!StringHelper.isNullOrEmpty(splitStr[i])) {
                                                        if (!reg.IsMatch(splitStr[i])) {
                                                            unRegStr += splitStr[i] + "|";
                                                        }
                                                    }
                                                }
                                                if (StringHelper.isNullOrEmpty(unRegStr)) {
                                                    unRegStr += "（格式不正确，可能多竖杠）|";
                                                }
                                            }
                                        }
                                    }*/
                                    if (chkRange == false) {
                                        if ("4".equals(lmtRang.getFBsfvLmt())) {
                                            msg.setCode(MessageCode.超出值域范围);
                                            msg.Info="超出值域范围";

                                            // msg.Info = String.format("字段名:%1$s.%2$s,输入值:%3$s.数据集名称:%4$s.字段名称:%5$s.交易号:%6$s.日志号:%7$s.未匹配值域:%8$s.", result.BsName, field.BsfName, Value, result.BsComments, field.BsfComments, field.BsCode, LogNo, tangible.StringHelper.isNullOrEmpty(unRegStr) ? "" : unRegStr.substring(0, unRegStr.length() - 1));

                                        } else {
                                            msg.setCode(MessageCode.超出值域范围);
                                            msg.Info="超出值域范围";

                                            //msg.Info = String.format("字段名:%1$s.%2$s,输入值:%3$s.数据集名称:%4$s.字段名称:%5$s.交易号:%6$s.日志号:%7$s", result.BsName, field.BsfName, Value, result.BsComments, field.BsfComments, field.BsCode, LogNo);

                                        }
                                        return false;
                                    }
                            }
                        }
                    }

                }

                if ("2".equals(field.getFBsfEncrypt())) {//是否加密   先不加
                     //_value = AESEncryption.Encrypt(_value.toString());
                }
                //  result.getParams()[field.HashCurrentKey];
               // System.out.println("-value"+_value);
                if (_value!=null) {
                    result.Params.put(field.HashCurrentKey, _value);//.setParams()[field.HashCurrentKey] = _value; //当前结构参数表
                    svr.Params.put(field.HashGlobalKey, _value);//[field.HashGlobalKey].Params[field.HashGlobalKey] = _value;
                }
                //log.Info("创建对象-设置值-循环树-xxx");
            }
        }

        //log.Info("创建对象-设置值-循环树-结束");
        if (result.getChilds().size() > 0) {
            List<KttranBusinessEx> nlist =new ArrayList<KttranBusinessEx>();
            for (KttranBusinessEx child : result.getChilds()) {

                //isSimpleParam 控制所有业务点不走结构 child.BsSimpleParam 控制当前业务点不走结构 N
                if (!isSimpleParam && !"Y".equals(child.getBsSimpleParam())) {
                    int i = 0;


                    List<Node> list = item.selectNodes(child.getBsName()); //.SelectNodes(child.BsName);

                    String xPath = xpath + "/" + child.getBsName();
                    if (list == null || list.size() == 0) {//C#是Count
                        if ("N".equals(child.getBsMandatory())) {
                            if ("N".equals(child.getBsStruct())) { continue; }
                            //KttranBusinessEx nchild = KttranBusinessEx.Copy(child);
                            KttranBusinessEx nchild = child;
                            nchild.setNode(null);
                            nchild.setIndex(i);
                            nchild.setXPath(xPath);
                            nchild.setParent(result);
                            /*nchild.XPath = xPath;
                            nchild.Parent = result;*/
                            i++;
                            //处理当前业务下的XML属性节点
                            if (SetObject(nchild, null, msg, svr, xPath, isSimpleParam) == false) {
                                return false;
                            }
                            nlist.add(nchild);
                            continue;
                        }
                        msg.setCode(MessageCode.业务处理失败);
                        msg.Info = "XML结构存在问题,未找到结构" + xPath;
                        return false;
                    }

                    for (Node node : list) {
                        child.Params.clear();
                        KttranBusinessEx nchild = KttranBusinessEx.Copy(child);
                        nchild.setNode(node);
                        nchild.setIndex(i);
                        nchild.setXPath(xPath);
                        nchild.setParent(result);
                        /*nchild.Node = node;
                        nchild.Index = i;
                        nchild.XPath = xPath;
                        nchild.Parent = result;*/
                        i++;
                        //处理当前业务下的XML属性节点
                        if (SetObject(nchild, node, msg, svr, xPath, isSimpleParam) == false) {
                            return false;
                        }

                        nlist.add(nchild);
                    }
                } else //简单参数执行
                {
                    //  KttranBusinessEx nchild = KttranBusinessEx.Copy(child);
                    KttranBusinessEx nchild = child;
                    nchild.setNode(item);
                    nchild.setParent(result);
                    /*nchild.Parent = result;
                    nchild.Node = item;*/
                    if (SetObject(nchild, item, msg, svr, xpath, isSimpleParam) == false) {
                        return false;
                    }
                    nlist.add(nchild);
                }
            }
            result.getChilds().clear(); //重构子项为了解决子项多条记录的问题
            for (KttranBusinessEx ktbus : nlist) {
                //result.Childs.Add(ktbus);
                result.getChilds().add(ktbus);
            }
        }

        return true;
    }

    /*
     * 判断是否为数字*/
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[0-9]+(.[0-9]+)?$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /*
     * 匹配时间*/
    public static boolean DateTime(String strSource) {
        Pattern pattern = Pattern.compile("^\\d\\d\\d\\d-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$");
        Matcher isNum = pattern.matcher(strSource);
        // return Regex(strSource, "^\\d\\d\\d\\d-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$");
        if (isNum.matches()) {
            // System.out.println("true");
            return true;

        } else {
            // System.out.println("false");
            return false;
        }
    }

    /*
     * 匹配日期*/
    public static boolean IsDate(String strSource) {
        Pattern pattern = Pattern.compile("^\\d\\d\\d\\d-\\d{1,2}-\\d{1,2}$");
        Matcher isNum = pattern.matcher(strSource);
        // return Regex(strSource, "^\\d\\d\\d\\d-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$");
        if (isNum.matches()) {
            // System.out.println("true");
            return true;

        } else {
            // System.out.println("false");
            return false;
        }

    }

    /*匹配yyyyMMdd*/
    public boolean isValidDate1(String str) {

        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);

            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
        return true;
    }

    /*匹配yyyyMMddTHHmmss*/
    public boolean isValidDate2(String str) {

        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);

            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
        return true;
    }

    /*匹配yyyyMMddHHmmss*/
    public boolean isValidDate3(String str) {

        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);

            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return false;
        }
        return true;
    }

    /**
     * 值域验证
     *
     * @param msg
     * @return
     */
    public boolean VaildLmtRang(Message msg) {
        boolean chkRange = true;
        for (KttranBussinessFieldReferences feildex : feildDictionary) {//遍历得到需要提前处理的对象
            for (KttranBussinessFieldLmtRang lmtRang : feildex.getFeild().FieldLmtRang) {//业务参数值域设置
                Object _value = feildex.getBusiness().Params.get(feildex.getFeild().HashCurrentKey);//.setParams(feildex.getFeild().HashCurrentKey);  //...Params; //原始值


                if ("E12_01_00_00".equals(lmtRang.getFBsCode())) {
                    if (lmtRang.getFBsfvLmt().equals("2")) { //取值方法(1指定值/2词典表/3)

                        if ("SEX".equals(lmtRang.getFBsfCode())) {
                            String EMP_ID = feildex.getBusiness().Params.get("EMP_ID").toString();
                            String ORG_CODE = feildex.getBusiness().Params.get("ORG_CODE").toString();
                            String str = prescriptionDao.limitEMP_ID(ORG_CODE, EMP_ID);
                            if (!"1".equals(str)) {
                                msg.setCode(MessageCode.业务处理失败);
                                msg.Info = "EMP_ID不在值域限定范围内";
                                return false;
                            }
                        }
                        if ("CF_TYPE".equals(lmtRang.getFBsfCode())) {
                            String ORG_CODE = feildex.getBusiness().Params.get("ORG_CODE").toString();
                            String str = prescriptionDao.limitORG_CODE(ORG_CODE);
                            if (!"1".equals(str)) {
                                msg.setCode(MessageCode.业务处理失败);
                                msg.Info = "ORG_CODE不在值域限定范围内";
                                return false;
                            }
                        }
                    } else if (lmtRang.getFBsfvLmt().equals("3")) //动态词典
                    {
                        if ("EMP_ID".equals(lmtRang.getFBsfCode())) {
                            String EMP_ID = feildex.getBusiness().Params.get("EMP_ID").toString();
                            String ORG_CODE = feildex.getBusiness().Params.get("ORG_CODE").toString();
                            String str = prescriptionDao.limitEMP_ID(ORG_CODE, EMP_ID);
                            if (!"1".equals(str)) {
                                msg.setCode(MessageCode.业务处理失败);
                                msg.Info = "EMP_ID不在值域限定范围内";
                                return false;
                            }
                        }
                        if ("ORG_CODE".equals(lmtRang.getFBsfCode())) {
                            String ORG_CODE = feildex.getBusiness().Params.get("ORG_CODE").toString();
                            String str = prescriptionDao.limitORG_CODE(ORG_CODE);
                            if (!"1".equals(str)) {
                                msg.setCode(MessageCode.业务处理失败);
                                msg.Info = "ORG_CODE不在值域限定范围内";
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return chkRange;
    }

    /**
     * 审核验证
     * <p>
     * 例如: a,b,c
     *
     * @return
     */
    public boolean ValidCheckDictionary(XmlBody body, KttranServiceconfigEx service) {
        Object o = 0;
        String error = "";
        KttranCheckDictionary dic = new KttranCheckDictionary();
        dic.setCheckUserCode(body.getHead().getUserID());
        dic.setCheckType(service.getCheckValidArray());// = service.CheckValidArray;

        List list = new ArrayList<String>();
        RefObject<List> tempRef_list = new RefObject<List>(list);
        RefObject<String> tempRef_error = new RefObject<String>(error);
        if (CreateListForSelfFun(mapper, service.getSvrCode(), "Check_KttranCheckDictionary", dic, tempRef_list, tempRef_error)) {
            error = tempRef_error.argValue;
            list = tempRef_list.argValue;
            for (KttranCheckDictionary dict : service.getCheckValids()) {
                boolean check = false;
                for (Object s : list) {
                    if (s.equals(dict.getCheckType())) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    body.Code = MessageCode.业务处理失败;
                    body.Info = String.format("词典%1$s未审核", dict.getCheckType());
                    return false;
                }
            }
            return true;
        } else {
            error = tempRef_error.argValue;
            list = tempRef_list.argValue;
        }
        body.Code = MessageCode.业务处理失败;
        body.Info = "调阅审核验证时发生错误," + error;
        return false;
    }

    //                                                  服务配置表实体
    public boolean SetObjectSp( KttranServiceconfigEx svr, Message msg) {
        String smsg = "";
        //log.Info(feildReferences.Count);
        for (KttranBussinessFieldReferences feild2 : busPreRunSql) {


          if ("PERSONID".equals(feild2.getFeild().HashCurrentKey)){
             String PERSONID= (String) feild2.getBusiness().Params.get("PERSONID");
              String HEALTH_ID= (String) feild2.getBusiness().Params.get("HEALTH_ID");
              String ORG_CODE= (String) feild2.getBusiness().Params.get("ORG_CODE");
              String IDCARD= (String) feild2.getBusiness().Params.get("IDCARD");
              String CARDNUM= (String) feild2.getBusiness().Params.get("CARDNUM");
              String CARDID= (String) feild2.getBusiness().Params.get("CARDID");
              String NAME= (String) feild2.getBusiness().Params.get("NAME");

              String persionid = inforDao.getFunction(PERSONID,HEALTH_ID,ORG_CODE,IDCARD,CARDNUM,CARDID,NAME);
              System.out.println("1++"+persionid);
              feild2.getBusiness().Params.put(feild2.getFeild().HashCurrentKey,persionid);//[feild2.Feild.HashCurrentKey] = feild2.ReferencesBusiness.Params[feild2.ReferencesFeildName];

          }
        }
        for (KttranBussinessFieldReferences feild2 : feildReferences) {
            String referencesName = "";
            String[] feilds = feild2.getFeild().FieldValue.getFBsfvCount().split("[.]", -1);
            KttranBusinessEx parent = null;
            referencesName = feilds[feilds.length - 1];
            feild2.setReferencesFeildName(referencesName);
            if (feilds.length == 2) {
               // parent=feild2.getBusiness().getParent();
                parent = feild2.getBusiness().getParent();
                boolean hasNode = false;
                for (KttranBusinessEx node : parent.getChilds()) {
                    if (feilds[0].equals(node.getBsCode())) {
                        feild2.setReferencesBusiness(node);
                        //feild2.getBusiness().getParams()[feild2.getFeild().HashCurrentKey] = node.getParams()[referencesName];
                        feild2.getBusiness().Params.put(feild2.getFeild().HashCurrentKey, node.Params.get(referencesName));
                        hasNode = true;
                        break;
                    }
                }
                if (!hasNode) {
                    msg.Code = MessageCode.业务处理失败;
                    msg.Info = "取其他表值时未找到相应路径,路径:" + feild2.getFeild().FieldValue.getFBsfvCount();
                    return false;
                }
            } else if (feilds.length == 1) {
                KttranBusinessEx node = feild2.getBusiness();
                feild2.setReferencesBusiness(node);
                //feild2.getBusiness().Params[feild2.Feild.HashCurrentKey] = node.Params[referencesName];
                feild2.getBusiness().Params.put(feild2.getFeild().HashCurrentKey, node.Params.get(referencesName));
            } else {
                for (KttranBusinessEx bus : svr.Businiess) {

                }
                KttranBusinessEx node = feild2.getBusiness();
                feild2.setReferencesBusiness(node);// = node;
                //feild2.Business.Params[feild2.Feild.HashCurrentKey] = node.Params[referencesName];
                feild2.getBusiness().Params.put(feild2.getFeild().HashCurrentKey, node.Params.get(referencesName));
            }
        }

       /* for (KttranBussinessFieldReferences feild2 : busPreRunSql) {//运行时需要提前处理的业务
            Object o = feild2.getBusiness().Params.get(feild2.getFeild().HashCurrentKey);//[feild2.Feild.HashCurrentKey];
            if (feild2.getFeild().FieldValue != null && !StringHelper.isNullOrEmpty(feild2.getFeild().FieldValue.getFBsfvCount())) {
                String statement = String.format("GetValue_CommSql_%1$s_%2$s_%3$s", feild2.getFeild().getBsCode(), feild2.getFeild().getBsfCode(),
                        feild2.getFeild().FieldValue.getFBsfvSn());
                DataTable table = new DataTable();
                RefObject<DataTable> tempRef_table = new RefObject<DataTable>(table);
                RefObject<String> tempRef_smsg = new RefObject<String>(smsg);
                if (!CreateDataTableForSelfFun(mapper, feild2.getBusiness().getBsCode(), statement, feild2.getBusiness().getParams(), tempRef_table, tempRef_smsg)) {
                    smsg = tempRef_smsg.argValue;
                    table = tempRef_table.argValue;
                    msg.Code = MessageCode.业务处理失败;
                    msg.Info = smsg;
                    return false;
                } else {
                    smsg = tempRef_smsg.argValue;
                    table = tempRef_table.argValue;
                    if (table != null && table.Rows.Count > 0) //新增table不为空判断
                    {
                        o = table.Rows[0][0];
                    } else {
                        o = feild2.Business.Params[feild2.Feild.BsfCode]; //mhh 20170111 1730 注释
                    }
                }
            }
            feild2.Business.Params[feild2.Feild.HashCurrentKey] = o;
        }*/


        return true;
    }


    ///#region ProcessData处理数据


    ///#region ProcessSaveData处理输入数据并进行存储业务

    /**
     * 处理输入数据并进行存储业务
     */
    public boolean ProcessSaveData( KttranServiceconfigEx svr, XmlBody body) {
        //log.Info("进入数据批量处理");
        //执行你的事务，如果不成功自动回滚
       /* boolean ret = false;
        int batcherCount = 0;
        SqlBatcher m_Batcher = new SqlBatcher(CommServiceConfig.getInstance().getDbProviderName());

        if (svr.Businiess[0].Transaction.equals("Y")) {
            mapper.LocalSession.BeginTransaction();*/
        if (svr.getSvrCode().equals("C01_04_00_00")) {
            if (ProcessSavePrescription(svr.Businiess, svrCurrent,body)) ;
            return true;
        } else if (svr.getSvrCode().equals("C01_02_00_00")) {

            if (ProcessSaveChildData(svr.Businiess, svrCurrent)) ;
            return true;
        }else if (svr.getSvrCode().equals("E12_01_00_00")) {

            if (ProcessSaveElectroniccaseUp(svr.Businiess, svrCurrent,body)) ;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean ProcessSaveElectroniccaseUp( List<KttranBusinessEx> buss, KttranServiceconfigEx svr,XmlBody body) {
        KtdocElectroniccaseUp electroniccaseUp=new KtdocElectroniccaseUp();

        for (KttranBusinessEx child:buss) {
                        Iterator<String> itr = child.Params.keySet().iterator();
                        while (itr.hasNext()) {
                            String str = (String) itr.next();
                            if (str.equals("CLINIC_ID")) {
                                electroniccaseUp.setCLINIC_ID((child.Params.get(str) == null) ? null : (String)child.Params.get(str).toString());
                            }
                            if (str.equals("NO")) {
                                electroniccaseUp.setNO((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("DEPT_CODE")) {
                                electroniccaseUp.setDEPT_CODE((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("REGISTER_ID")) {
                                electroniccaseUp.setREGISTER_ID((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("EMP_ID")) {
                                electroniccaseUp.setEMP_ID((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("PAT_NO")) {
                                electroniccaseUp.setPAT_NO((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("NAME")) {
                                electroniccaseUp.setNAME((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("SEX")) {
                                electroniccaseUp.setSEX((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("BIRTHDAY")) {
                                electroniccaseUp.setBIRTHDAY((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("AGE")) {
                                electroniccaseUp.setAGE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }

                            if (str.equals("AGE2")) {
                                electroniccaseUp.setAGE2((child.Params.get(str) == null) ? null : (String)child.Params.get(str).toString());
                            }
                            if (str.equals("CF_TYPE")) {
                                electroniccaseUp.setCF_TYPE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("IDCARD")) {
                                electroniccaseUp.setIDCARD((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("CLINIC_TIME")) {
                                electroniccaseUp.setCLINIC_TIME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("IF_FIRSTVISIT")) {
                                electroniccaseUp.setIF_FIRSTVISIT((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("ALLERGIC_HISTORY")) {
                                electroniccaseUp.setALLERGIC_HISTORY((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("CHIEF_COMPLAINT")) {
                                electroniccaseUp.setCHIEF_COMPLAINT((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("HPC")) {
                                electroniccaseUp.setHPC((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("PMH")) {
                                electroniccaseUp.setPMH((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("PHYSIQUE")) {
                                electroniccaseUp.setPHYSIQUE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }

                            if (str.equals("CLINIC_DISPOSE")) {
                                electroniccaseUp.setCLINIC_DISPOSE((child.Params.get(str) == null) ? null : (String)child.Params.get(str).toString());
                            }
                            if (str.equals("DIAGNOSE_NAME")) {
                                electroniccaseUp.setDIAGNOSE_NAME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("DIAGNOSE_MEASUR")) {
                                electroniccaseUp.setDIAGNOSE_MEASUR((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("KTDOC_REMARK")) {
                                electroniccaseUp.setKTDOC_REMARK((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("ORG_CODE")) {
                                electroniccaseUp.setORG_CODE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("UPLOAD_TIME")) {
                                electroniccaseUp.setUPLOAD_TIME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("CARDID")) {
                                electroniccaseUp.setCARDID((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("PERSONID")) {
                                electroniccaseUp.setPERSONID((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("CARDNUM")) {
                                electroniccaseUp.setCARDNUM((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("CREATE_TIME")) {
                                electroniccaseUp.setCREATE_TIME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("COMPANY")) {
                                electroniccaseUp.setCOMPANY((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                        }

                       /* String ORG_CODE= prescriptionDao.limitORG_CODE(electroniccaseUp.getORG_CODE());
                        if (!"1".equals(ORG_CODE)){
                            body.Code=MessageCode.超出值域范围;
                            body.Info="ORG_CODE不在限定范围内";
                        }
                        String EMP_ID =prescriptionDao.limitEMP_ID(electroniccaseUp.getORG_CODE(),electroniccaseUp.getEMP_ID());
                        if (!"1".equals(EMP_ID)){
                            body.Code=MessageCode.超出值域范围;
                            body.Info="EMP_ID不在限定范围内";
                        }*/
                        prescriptionDao.updateOrInsertElectroniccaseUp(electroniccaseUp);
                    }





        return true;
    }


    //StringBuilder sql = new StringBuilder();
    public boolean ProcessSavePrescription( List<KttranBusinessEx> buss, KttranServiceconfigEx svr,XmlBody body) {
        KtdocPrescription prescription=new KtdocPrescription();
        List<KtdocPrescriptionList> lists=new ArrayList<>();
        for (KttranBusinessEx ke:buss) {
            if (ke.getChilds().size() > 0) {
                for (KttranBusinessEx child : ke.getChilds()) {
                    System.out.println(child);
                    if (child.getBsCode().equals("C01_04_00_01")) {
                        Iterator<String> itr = child.Params.keySet().iterator();
                        while (itr.hasNext()) {
                            String str = (String) itr.next();
                            if (str.equals("EMP_ID")) {
                                prescription.setEMP_ID((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("CLINIC_ID")) {
                                prescription.setCLINIC_ID((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("ORG_CODE")) {
                                prescription.setORG_CODE((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("RECORD_TIME")) {
                                prescription.setRECORD_TIME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("PRES_ID")) {
                                prescription.setPRES_ID((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("TYPE")) {
                                prescription.setTYPE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                        }
                        String ORG_CODE= prescriptionDao.limitORG_CODE(prescription.getORG_CODE());
                        if (!"1".equals(ORG_CODE)){
                            body.Code=MessageCode.超出值域范围;
                            body.Info="ORG_CODE不在限定范围内";
                        }
                        String EMP_ID =prescriptionDao.limitEMP_ID(prescription.getORG_CODE(),prescription.getEMP_ID());
                        if (!"1".equals(EMP_ID)){
                            body.Code=MessageCode.超出值域范围;
                            body.Info="EMP_ID不在限定范围内";
                        }
                        prescriptionDao.deletektdocPrescription(prescription.getORG_CODE(),prescription.getPRES_ID());
                        prescriptionDao.deletektdocPrescriptionList(prescription.getORG_CODE(),prescription.getPRES_ID());
                      prescriptionDao.updateOrInsertdocPrescription(prescription);
                    }else if (child.getBsCode().equals("C01_04_00_02")) {
                        KtdocPrescriptionList prescriptionList=new KtdocPrescriptionList();
                        Iterator<String> itr = child.Params.keySet().iterator();
                        while (itr.hasNext()) {

                            String str = itr.next();
                            if (str.equals("NO")) {
                                prescriptionList.setNO((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("MEDIC_SPEC")) {
                                prescriptionList.setMEDIC_SPEC((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("MEDIC_TYPE")) {
                                prescriptionList.setMEDIC_TYPE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("MEDIC_USECODE")) {
                                prescriptionList.setMEDIC_USECODE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("GROUPNO")) {
                                prescriptionList.setGROUPNO((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("MEDIC_CODE")) {
                                prescriptionList.setMEDIC_CODE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("MEDIC_USEDOSAGE")) {
                                prescriptionList.setMEDIC_USEDOSAGE((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("MEDIC_USEPLAN")) {
                                prescriptionList.setMEDIC_USEPLAN((child.Params.get(str) == null) ? null : (String)child.Params.get(str));
                            }
                            if (str.equals("MEDIC_SELF")) {
                                prescriptionList.setMEDIC_SELF((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("EMP_ID")) {
                                prescriptionList.setEMP_ID((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("MEDIC_CLASS")) {
                                prescriptionList.setMEDIC_CLASS((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("ORG_CODE")) {
                                prescriptionList.setORG_CODE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("MEDIC_NAME")) {
                                prescriptionList.setMEDIC_NAME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("MEDIC_CODE_LOCAL")) {
                                prescriptionList.setMEDIC_CODE_LOCAL((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("PRES_ID")) {
                                prescriptionList.setPRES_ID((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("CREATE_TIME")) {
                                prescriptionList.setCREATE_TIME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("MEDIC_USEQUANTITY")) {
                                prescriptionList.setMEDIC_USEQUANTITY((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("MEDIC_QUANTITY")) {
                                prescriptionList.setMEDIC_QUANTITY((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }
                            if (str.equals("MODIFY_TIME")) {
                                prescriptionList.setMODIFY_TIME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("MEDIC_DAYS")) {
                                prescriptionList.setMEDIC_DAYS((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                            }

                        }
                        lists.add(prescriptionList);


                    }

                }
            }
        }
        String ORG_CODE=lists.get(0).getORG_CODE();
        String PRES_ID=lists.get(0).getPRES_ID();
        System.out.println(lists);
        prescriptionDao.updateOrInsertPrescriptionList(lists);//,ORG_CODE,PRES_ID

        return true;
    }
    public boolean ProcessSaveChildData( List<KttranBusinessEx> buss, KttranServiceconfigEx svr) {
        KtbasePerson ktbasePerson=new KtbasePerson();
        kthraAllergyNew kan=new kthraAllergyNew();
        KtdocWorkLog ktdocWorkLog=new KtdocWorkLog();
        KtdocDiagnose kd=new KtdocDiagnose();

        String ID=null;
        String NAME=null;
        String BIR=null;
        String HEALTH_ID=null;
        String CAD_ID=null;
        for (KttranBusinessEx ke:buss) {
            if (ke.getChilds().size() > 0) {
                for (KttranBusinessEx child : ke.getChilds()) {
                    if (child.getBsCode().equals("C01_02_00_01")) {
                        Iterator<String> itr = child.Params.keySet().iterator();
                        while (itr.hasNext()) {
                            String str = (String) itr.next();
                            if (str.equals("CF_TYPE")) {
                                ktbasePerson.setCF_TYPE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("HEALTH_ID")) {
                                ktbasePerson.setHEALTH_ID((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("CAD_ID")) {
                                CAD_ID = str;
                            }
                            if (str.equals("ORG_CODE")) {
                                ktbasePerson.setORG_CODE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("SEX")) {
                                ktbasePerson.setSEX((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("CREATE_TIME")) {
                                ktbasePerson.setCREATE_TIME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("MODIFY_TIME")) {
                                ktbasePerson.setMODIFY_TIME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("BIRTHDAY")) {
                                ktbasePerson.setBIRTHDAY((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("NAME")) {
                                ktbasePerson.setNAME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                        }
                        String persionid = inforDao.getFunction(ktbasePerson.getPERSONID(), ktbasePerson.getHEALTH_ID(), ktbasePerson.getORG_CODE(),
                                ktbasePerson.getIDCARD(), ktbasePerson.getCARDNUM(), ktbasePerson.getCARDID(), ktbasePerson.getNAME());
                        System.out.println(persionid);
                        inforDao.PERSON_INFOR(persionid, ktbasePerson.getHEALTH_ID(), null, null, null,
                                null, ktbasePerson.getNAME(), ktbasePerson.getSEX(), ktbasePerson.getBIRTHDAY(), ktbasePerson.getCF_TYPE(), null
                                , null, null, null, null, null, null,
                                null, null, null, null, null,
                                null, null, null, null, ktbasePerson.getCREATE_TIME(),
                                null, null, null, ktbasePerson.getORG_CODE(), null, null, null, null, null, null, null);
                    } else if (child.getBsCode().equals("C01_02_00_02")) {
                        Iterator<String> itr = child.Params.keySet().iterator();
                        while (itr.hasNext()) {
                            String str = itr.next();
                            if (str.equals("ORG_CODE")) {
                                ktdocWorkLog.setORG_CODE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("CLINIC_ID")) {
                                ktdocWorkLog.setCLINIC_ID((child.Params.get(str) == null) ? null : (String) child.Params.get(str));

                            }

                            if (str.equals("CLINIC_TIME")) {
                                ktdocWorkLog.setCLINIC_TIME((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                                if (str.equals("DIAGNOSE")) {
                                    ktdocWorkLog.setDIAGNOSE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                                }
                                if (str.equals("EMP_ID")) {
                                    ktdocWorkLog.setEMP_ID((child.Params.get(str) == null) ? null : child.Params.get(str).toString());
                                }
                                if (str.equals("ICD10")) {
                                    ktdocWorkLog.setICD10((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                                }
                                if (str.equals("REGISTER_ID")) {
                                    ktdocWorkLog.setREGISTER_ID((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                                }
                                if (str.equals("TYPE")) {
                                    ktdocWorkLog.setTYPE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                                }
                            }

                        System.out.println(ktdocWorkLog);
                        inforDao.deleteKtdocDiagnose(ktdocWorkLog.getORG_CODE(),ktdocWorkLog.getCLINIC_ID());
                        inforDao.deleteKtdocWorkLog(ktdocWorkLog.getORG_CODE(),ktdocWorkLog.getCLINIC_ID());
                        inforDao.insertKtdocWorkLog(ktdocWorkLog);

//ORG_CODE,
//	CLINIC_ID,
//	NO,
//	ICD,
//	DISEASE_NAME,
//	REMARKS,
//	EMP_ID
                    }else if (child.getBsCode().equals("C01_02_00_03")){
                        Iterator<String> itr = child.Params.keySet().iterator();
                        while (itr.hasNext()) {
                            String str = itr.next();
                            if (str.equals("ORG_CODE")) {
                                kd.setORG_CODE((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("CLINIC_ID")) {
                                kd.setCLINIC_ID((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("NO")) {
                                kd.setNO((child.Params.get(str) == null) ? null :  child.Params.get(str).toString());
                            }
                            if (str.equals("ICD")) {
                                kd.setICD((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("DISEASE_NAME")) {
                                kd.setDISEASE_NAME((child.Params.get(str) == null) ? null : (String)child.Params.get(str));
                            }
                            if (str.equals("REMARKS")) {
                                kd.setREMARKS((child.Params.get(str) == null) ? null : (String) child.Params.get(str));
                            }
                            if (str.equals("EMP_ID")) {
                                kd.setEMP_ID((child.Params.get(str) == null) ? null : (String) child.Params.get(str).toString());
                            }

                        }
                        inforDao.updateKtdocDiagnse(kd);
                    }

                }
            }
        }
        for (KttranBusinessEx bus : buss) {

            //log.Exp("Save_CommSql_" + bus.BsCode + " BS_COMMENT:" + bus.BsComments);
            if (bus.getBsStatusSaveTable().equals("Y") || bus.getBsStatusSaveSql().equals("Y")) {
//利用循环遍历出key和value
                Iterator<String> itr = bus.Params.keySet().iterator();
                while (itr.hasNext()){
                    String str = (String)itr.next();
                    if (str.equals("ID")){
                        ID=str;
                    }
                    if (str.equals("NAME")){
                        NAME=str;
                    }
                    if (str.equals("BIR")){
                        BIR=str;
                    }
                }
                System.out.println(ID+" 111  "+NAME+ "   "+BIR);
                String id = (bus.Params.get(ID) == null) ? null: (String)bus.Params.get(ID);
                String name = (bus.Params.get(NAME) == null) ? null: (String)bus.Params.get(NAME);
                String bir = (bus.Params.get(BIR) == null) ? null: (String)bus.Params.get(BIR);


                userInfoDao.saveTable(id,name,bir);

                /*bus.Params.forEach((k, v) -> System.out.println("key: " + k + " value:" + v));
                String pk=userInfoDao.selectPK(bus.getBsCode());
                System.out.println("pk"+pk);
                List<String> list=userInfoDao.findFieldByTableName(bus.getBsTable());
                sql.append("update "+bus.getBsTable()+" set ");

                 Map<String,Object> map=new HashMap<>();
                for (String str:list){
                    System.out.println(bus.Params.get(str));
                    if (bus.Params.get(str)!=null && bus.Params.get(str)!="" && !str.equals(pk)){
                        sql.append(str+"='"+bus.Params.get(str)+"' ");
                        map.put(str,bus.Params.get(str));
                    }
                   }
                for (String str:list) {
                    if (str.equals(pk)) {
                        sql.append("where " + str + "='" + bus.Params.get(str) + "'");

                    }
                }
             //   DBUtils.executeUpdate(sql.toString());
                System.out.println(sql);
                System.out.println(map);
                String strs=map.toString();*/
                
                //String sql="update  LPJ_TABLE_UPDATE_TEST set name='yylss' where id=1111";
               /* for(Iterator<String> itr = bus.Params.keySet().iterator();itr.hasNext();){
                    String key = itr.next();
                    if (bus.Params.get(key)!=null) {
                        String value = (String) bus.Params.get(key);
                        System.out.println(key + " (for) " + value);
                    }
                }*/

                //userInfoDao.updateFieldByTableName(bus.getBsTable(),)


                //  List<String> list = DatabaseUtil.getColumnNames(bus.getBsTable());

                /*List<String> lists=userInfoDao.findFieldByTableName(bus.getBsTable());

                System.out.println(lists);
                //  List<String> list = DatabaseUtil.getColumnNames(bus.getBsTable());
String name=null;
String id=null;
                for (String str : lists) {
                  //  System.out.println("str "+str);

                    String v =(String) bus.Params.get(str);

                    System.out.println("v  "+v);
                }*/

              //  userInfoDao.updateFieldByTableName(bus.getBsTable(),(String)bus.Params.get("NAME"),(String)bus.Params.get("ID"));

                /*IMappedStatement statement = mapper.GetMappedStatement("Save_CommSql_" + bus.BsCode);
                //log.Exp("Save_CommSql_" + bus.BsCode);
                if (!mapper.IsSessionStarted) {
                    mapper.OpenConnection(); //确保LocalSession
                }
                if (mapper.LocalSession.Connection == null) {
                    mapper.LocalSession.OpenConnection(); //确保Connection
                }

                IPreparedCommand pc = statement.PreparedCommand;

                RequestScope scope = statement.Statement.Sql.GetRequestScope(statement, bus.Params, mapper.LocalSession);
                if (scope.PreparedStatement.DbParameters != null && scope.PreparedStatement.DbParameters.Length > 0) {
                    StringCollection dbParametersName = scope.PreparedStatement.DbParametersName;
                    IDbDataParameter[] dbParameters = scope.PreparedStatement.DbParameters;
                    int count = dbParametersName.Count;
                    ParameterMap parameterMap = mapper.ParameterMaps["MyIBatisData.generate_params_" + bus.BsCode] instanceof ParameterMap ? (ParameterMap) mapper.ParameterMaps["MyIBatisData.generate_params_" + bus.BsCode] : null;
                    if (parameterMap != null) {
                        for (int i = 0; i < count; i++) {
                            IDbDataParameter parameter = dbParameters[i];
                            ParameterProperty mapping = scope.ParameterMap.GetProperty(i);
                            ParameterProperty gparam = parameterMap.GetProperty(mapping.PropertyName);
                            // ParameterProperty gparam = GetPropertyByName(mapping.PropertyName, parameterMap);
                            if (gparam == null) {
                                continue;
                            }
                            mapping.CallBackName = gparam.CallBackName;
                            mapping.CLRType = gparam.CLRType;
                            mapping.ColumnName = gparam.ColumnName;
                            mapping.DbType = gparam.DbType;
                            mapping.Direction = gparam.Direction;
                            mapping.DirectionAttribute = gparam.DirectionAttribute;
                            //mapping.NullValue = gparam.NullValue;
                            mapping.Precision = gparam.Precision;
                            mapping.PropertyName = gparam.PropertyName;
                            mapping.Scale = gparam.Scale;
                            mapping.Size = gparam.Size;
                            mapping.TypeHandler = gparam.TypeHandler; //自定义类型读取处理器

                            parameter.Direction = mapping.Direction;
                            if (gparam.Size > -1) {
                                parameter.Size = mapping.Size;
                            }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Precision > Byte.MinValue)
                            if (gparam.Precision > Byte.MIN_VALUE) {
                                parameter.Precision = mapping.Precision;
                            }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Scale > Byte.MinValue)
                            if (gparam.Scale > Byte.MIN_VALUE) {
                                parameter.Scale = mapping.Scale;
                            }
                            if (gparam.DbType.equals("decimal")) {
                                parameter.DbType = DbType.Decimal;
                            }
                            //add by mhh 20160311
                            if (gparam.DbType.equals("varchar") && CommServiceConfig.Instance.DbProviderName.equals("MYSQL")) {
                                parameter.DbType = DbType.String;
                            }
                        }
                    }
                }


                pc.Create(scope, mapper.LocalSession, statement.Statement, bus.Params); //生成对象IDbCommand
                m_Batcher.AddToBatch(GetInnerDbCommand(scope.IDbCommand));
                runtype = "1";
                batcherCount.argValue++;

                if (batcherCount.argValue > 0 && _dianacount > 0 && batcherCount.argValue > _dianacount) {
                    m_Batcher.ExecuteBatch(); //已经自动开启事务
                    m_Batcher.ClearBatch();
                    batcherCount.argValue = 0;
                }
            }

            ProcessSaveChildData(mapper, bus.Childs, svr, m_Batcher, batcherCount);*/
            }
        }
        return true;
    }
   /* public int GetDbParameNameIndex(RequestScope scope, String name) {
        int i = 0;
        for (String tname : scope.PreparedStatement.DbParametersName) {
            if (name.equals(tname)) {
                return i;
            }
            i++;
        }
        return -1;
    }*/

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region ProcessResponseData处理数据实现XML标准格式返回用户

    /**
     * 处理数据实现XML标准格式返回用户
     *
     * @param svr    运行时生成的当前服务
     * @param strXml 生成的标准XML字符串
     * @return 返回是否成功
     */
    public boolean ProcessResponseData( KttranServiceconfigEx svr, StringBuilder strXml, XmlBody body) {
       if (runtype == "0" || runtype == "2")
        {
        //log.Info("进入数据输出处理");
       boolean r = ProcessResponseChildData( svr.getBusiniess(), strXml, body);
        //log.Info("完成数据输出处理");
        return r;
        }
return true;

    }
/*
    public boolean select( KttranServiceconfigEx svr, StringBuilder strXml, String msg) {
        boolean r = ProcessResponseChildData1( svr.getBusiniess(), strXml, msg);
        //log.Info("完成数据输出处理");
        return r;

    }
*/
/*
    public boolean ProcessResponseChildData1( List<KttranBusinessEx> buss, StringBuilder strXml, String msg) {
        for (KttranBusinessEx bus : buss){
            bus.getBsTable();
            Object param=bus.Params;
           String sql="select * from "+bus.getBsTable()+" where id="+
        }
    }
*/

        /**
         * 处理数据实现XML标准格式返回用户
         *
         *
         * @param buss   运行时生成的可用业务集合
         * @param strXml 生成的标准XML字符串
         * @return 返回是否成功
         */

    public boolean ProcessResponseChildData( List<KttranBusinessEx> buss, StringBuilder strXml, XmlBody body) {
        for (KttranBusinessEx bus : buss) {
            if (bus.isHasContainOutput()) //包含输出字段
            {

                if (!StringHelper.isNullOrEmpty(bus.getBsOutXml())) //业务的大节点
                {
                    strXml.append(String.format("<%1$s>\r\n", bus.getBsOutXml()));
                }
//                                语句查询    N                            表查询  Y                      存储过程 N
                if (bus.getBsStatusSchSql().equals("Y") || bus.getBsStatusSchTable().equals("Y") || bus.getBsStatusProc().equals("Y")) //存储语句||表查询||存储过程[都可以产生游标数据]
                {
                  /*  tangible.RefObject<DataTable> tempRef_table = new tangible.RefObject<DataTable>(table);
                    if (!CreateDataTable(mapper, bus, tempRef_table, msg)) //产生记录集失败
                    {
                        table = tempRef_table.argValue;
                        return false;
                    } else {
                        table = tempRef_table.argValue;
                    }
                    if (bus.HasContainCursor || !tangible.StringHelper.isNullOrEmpty(bus.CursorName)) //有游标||游标名称不为空
                    {
                        //ProcessResponseDataTable(table, strXml, null, bus.CursorName);
                        ProcessResponseDataTable(table, strXml, null, bus.CursorName, bus); //modify by mhh 20160518
                    } else {
                        ProcessResponseDataTable(table, strXml, null, bus.BsOutXml);
                    }*/
                }
               /* for (KttranBussinessFieldEx feild : bus.Fields) //写字段
                {
                    if (feild.BsfDirection.equals("4") || feild.BsfDirection.equals("5")) //判断是否向外输出
                    {
                        if (!feild.BsfType.equals("5")) //游标类型
                        {
                            strXml.append(String.format("<%1$s>%2$s</%3$s>", feild.BsfName, bus.Params[feild.HashCurrentKey], feild.BsfName));
                        }
                    }
                    if (feild.BsfDirection.equals("2") || feild.BsfDirection.equals("3")) //判断是否向外输出
                    {
                        if (!feild.BsfType.equals("5")) //游标类型
                        {
                            strXml.append(String.format("<%1$s>%2$s</%3$s>", feild.BsfName, bus.Params[feild.HashCurrentKey], feild.BsfName));
                        }
                    }
                }
                if (!ProcessResponseChildData(mapper, bus.Childs, strXml, msg)) {
                    return false;
                }
                if (!StringHelper.isNullOrEmpty(bus.getBsOutXml())) {
                    strXml.append(String.format("</%1$s>\r\n", bus.getBsOutXml()));
                }*/
                //}
               // table.Dispose();
            } else if (bus.getBsStatusSchSql().equals("Y")) {
                /*DataTable table = new DataTable();
                if (!tangible.StringHelper.isNullOrEmpty(bus.BsOutXml)) {
                    strXml.append(String.format("<%1$s>\r\n", bus.BsOutXml));
                }
                tangible.RefObject<DataTable> tempRef_table2 = new tangible.RefObject<DataTable>(table);
                if (!CreateDataTable(mapper, bus, tempRef_table2, msg)) {
                    table = tempRef_table2.argValue;
                    return false;
                } else {
                    table = tempRef_table2.argValue;
                }
                //不指定游标不输出
                if (bus.HasContainCursor || !tangible.StringHelper.isNullOrEmpty(bus.CursorName)) {
                    ProcessResponseDataTable(table, strXml, null, bus.CursorName, bus); //modify by mhh 20160518
                } else {
                    ProcessResponseDataTable(table, strXml, null, null);
                }

                if (!ProcessResponseChildData(mapper, bus.Childs, strXml, msg)) {
                    return false;
                }
                if (!tangible.StringHelper.isNullOrEmpty(bus.BsOutXml)) {
                    strXml.append(String.format("</%1$s>\r\n", bus.BsOutXml));
                }
                table.Dispose();*/
            } else if (bus.getBsStatusSchTable().equals("Y") || bus.getBsStatusProc().equals("Y")) {
                String ID=null;
                String NAME=null;
                String id=null;
                String name=null;

                    //log.Exp("Save_CommSql_" + bus.BsCode + " BS_COMMENT:" + bus.BsComments);
//利用循环遍历出key和value
                        Iterator<String> itr = bus.Params.keySet().iterator();
                        while (itr.hasNext()){
                            String str = (String)itr.next();
                            if (str.equals("ID")){
                                ID=str;
                                 id = (bus.Params.get(ID) == null) ? null: (String)bus.Params.get(ID);

                            }
                            if (str.equals("NAME")){
                                NAME=str;
                                 name = (bus.Params.get(NAME) == null) ? null: (String)bus.Params.get(NAME);

                            }

                        }

               Test list= userInfoDao.selectTable(id,name);
                System.out.println(list);
                strXml.append("\n");
                strXml.append(String.format("<ID>%1$s</ID>",list.getId()));
               strXml.append("\n");
                strXml.append(String.format("<NAME>%1$s</NAME>\r\n", list.getName()));



                /*List<String> lists=userInfoDao.findFieldByTableName(bus.getBsTable());
              //Map map=userInfoDao.selectTable();
                System.out.println(lists);
              //  List<String> list = DatabaseUtil.getColumnNames(bus.getBsTable());

                for (String str : lists) {
                    System.out.println("str "+str);
                    String v =(String) bus.Params.get(str);
                    System.out.println("v  "+v);
                    if (v != null) {// v.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                        strXml.append(String.format("<%1$s>%2$s</%3$s>", str,v,str));

                    }
                }*/
                /*ResultSet rs=null;
                try {
                    DatabaseMetaData db= connection.getMetaData();

                  rs=db.getTables(null,null,null,new String[] {"TABLE"});
                  while (rs.next()){

                  }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                DataTable table = new DataTable();
                if (!StringHelper.isNullOrEmpty(bus.getBsOutXml())) {//输出标签
                    strXml.append(String.format("<%1$s>\r\n", bus.getBsOutXml()));
                }
                RefObject<DataTable> tempRef_table3 = new RefObject<DataTable>(table);
                if (!CreateDataTable(mapper, bus, tempRef_table3, msg)) {//执行 true
                    table = tempRef_table3.argValue;
                    return false;
                } else {
                    table = tempRef_table3.argValue;
                }

                //不指定游标不输出
                if (bus.isHasContainCursor() || !StringHelper.isNullOrEmpty(bus.getCursorName())) {
                    //ProcessResponseDataTable(table, strXml, null, bus.CursorName);
                    //ProcessResponseDataTable(table, strXml, null, bus.getCursorName(), bus); //modify by mhh 20160518
                } else {
                  //  ProcessResponseDataTable(table, strXml, null, null);//不指定游标
                }

                if (!ProcessResponseChildData(mapper, bus.getChilds(), strXml, msg)) {
                    return false;
                }
                if (!tangible.StringHelper.isNullOrEmpty(bus.BsOutXml)) {
                    strXml.append(String.format("</%1$s>\r\n", bus.BsOutXml));
                }
                table.Dispose();
            } else {
                if (!tangible.StringHelper.isNullOrEmpty(bus.BsOutXml)) {
                    strXml.append(String.format("<%1$s>\r\n", bus.BsOutXml));
                }
                if (!ProcessResponseChildData(mapper, bus.Childs, strXml, msg)) {
                    return false;
                }

                if (!tangible.StringHelper.isNullOrEmpty(bus.BsOutXml)) {
                    strXml.append(String.format("</%1$s>\r\n", bus.BsOutXml));
                }
            }
        }*/

            }
        }
        return true;
    }


    /**
     * 将DataTable数据输出到StringBuilder字符串容器中
     *
     * @param table       表数据
     * @param strXml      XML数据
     * @param tableName   表名称
     * @param rowListName 行名称
     */
/*
    public void ProcessResponseDataTable(DataTable table, StringBuilder strXml, String tableName, String rowListName) {
        if (!StringHelper.isNullOrEmpty(tableName)) {
            strXml.append(String.format("<%1$s>\r\n", tableName));
        }
        for (DataRow dr : table.Rows) {
            if (!StringHelper.isNullOrEmpty(rowListName)) //行名称添加
            {
                strXml.append(String.format("<%1$s>\r\n", rowListName));
            }
            for (DataColumn dc : table.Columns) //行数据添加
            {
                if (dc.DataType.FullName.equals("System.Byte[]")) //十六进制处理
                {
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bs = (byte[])dr[dc.ColumnName];
                    byte[] bs = (byte[]) dr.get(dc.ColumnName);
                    strXml.append(String.format("<%1$s>%2$s</%3$s>\r\n", dc.ColumnName, UtilConv.BytesToHexString(bs), dc.ColumnName));
                } else {//v=123
                    String v = (dr.get(dc.ColumnName) == DBNull.Value) ? "" : (dr.get(dc.ColumnName).toString());
                    if (!StringHelper.isNullOrEmpty(v)) {
                        strXml.append(String.format("<%1$s>%2$s</%3$s>\r\n", dc.ColumnName, v.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"), dc.ColumnName));
                    } else {
                        strXml.append(String.format("<%1$s></%2$s>\r\n", dc.ColumnName, dc.ColumnName));
                    }
                }
            }
            if (!StringHelper.isNullOrEmpty(rowListName)) {
                strXml.append(String.format("</%1$s>\r\n", rowListName));
            }
        }
        if (!StringHelper.isNullOrEmpty(tableName)) {
            strXml.append(String.format("</%1$s>\r\n", tableName));
        }
    }
*/

    /**
     * 将DataTable数据输出到StringBuilder字符串容器中
     *
     * @param table       表数据
     * @param strXml      XML数据
     * @param tableName   表名称
     * @param rowListName 行名称
     * @param fieldExs    游标类型
     *                    20160512 add by mhh
     */

/*
    public void ProcessResponseDataTable(DataTable table, StringBuilder strXml, String tableName, String rowListName, KttranBusinessEx businessEx) {
        boolean flag = false;
        for (KttranBussinessFieldEx fieldEx : businessEx.Fields) {
            if (fieldEx.BsfCursorType.equals("N") && fieldEx.BsfType.equals("5")) {
                flag = true;
                break;
            }
        }
        if (!tangible.StringHelper.isNullOrEmpty(tableName)) {
            strXml.append(String.format("<%1$s>\r\n", tableName));
        }
        if (flag) {
            for (DataRow dr : table.Rows) {
                if (!tangible.StringHelper.isNullOrEmpty(rowListName)) //行名称添加
                {
                    strXml.append(String.format("<%1$s>\r\n", rowListName));
                }
                for (DataColumn dc : table.Columns) //行数据添加
                {
                    if (dc.DataType.FullName.equals("System.Byte[]")) //十六进制处理
                    {
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bs = (byte[])dr[dc.ColumnName];
                        byte[] bs = (byte[]) dr.get(dc.ColumnName);
                        strXml.append(String.format("<%1$s>%2$s</%3$s>\r\n", dc.ColumnName, UtilConv.BytesToHexString(bs), dc.ColumnName));
                    } else {
                        String v = (dr.get(dc.ColumnName) == DBNull.Value) ? "" : (dr.get(dc.ColumnName).toString());
                        if (!tangible.StringHelper.isNullOrEmpty(v)) {
                            strXml.append(String.format("<%1$s>%2$s</%3$s>\r\n", dc.ColumnName, v.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"), dc.ColumnName));
                        } else {
                            strXml.append(String.format("<%1$s></%2$s>\r\n", dc.ColumnName, dc.ColumnName));
                        }
                    }
                }
                if (!tangible.StringHelper.isNullOrEmpty(rowListName)) {
                    strXml.append(String.format("</%1$s>\r\n", rowListName));
                }
            }
        } else {
            if (!tangible.StringHelper.isNullOrEmpty(rowListName)) //行名称添加
            {
                strXml.append(String.format("<%1$s>\r\n", rowListName));
            }
            for (DataRow dr : table.Rows) {
                for (DataColumn dc : table.Columns) //行数据添加
                {
                    if (dc.DataType.FullName.equals("System.Byte[]")) //十六进制处理
                    {
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: byte[] bs = (byte[])dr[dc.ColumnName];
                        byte[] bs = (byte[]) dr.get(dc.ColumnName);
                        strXml.append(String.format("<%1$s>%2$s</%3$s>\r\n", dc.ColumnName, UtilConv.BytesToHexString(bs), dc.ColumnName));
                    } else {
                        String v = (dr.get(dc.ColumnName) == DBNull.Value) ? "" : (dr.get(dc.ColumnName).toString());
                        if (!tangible.StringHelper.isNullOrEmpty(v)) {
                            strXml.append(String.format("<%1$s>%2$s</%3$s>\r\n", dc.ColumnName, v.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;"), dc.ColumnName));
                        } else {
                            strXml.append(String.format("<%1$s></%2$s>\r\n", dc.ColumnName, dc.ColumnName));
                        }
                    }
                }
            }
            if (!tangible.StringHelper.isNullOrEmpty(rowListName)) {
                strXml.append(String.format("</%1$s>\r\n", rowListName));
            }
        }
        if (!tangible.StringHelper.isNullOrEmpty(tableName)) {
            strXml.append(String.format("</%1$s>\r\n", tableName));
        }

    }
*/


    ///#region CreateData处理创建对象

    /**
     * 产生记录集合
     *
     * @param mapper 系统可用的mapper操作对象
     * @param bus    运行时生成的可用业务对象
     * @param table  需要产生的数据集合
     * @param msg    返回的消息
     * @return 创建是否成功
     */


/*
    public boolean CreateDataTable(SqlMapper mapper, KttranBusinessEx bus, RefObject<DataTable> table, RefObject<String> msg) {
        //log.Info("进入创建对象处理CreateDataTable");
        boolean ireturn = false;
        //Mapper mapper1=;

        IDbCommand command = null;
        try {//      LPJ_TABLE_UPDATE_TEST
            if (!StringHelper.isNullOrEmpty(bus.getBsTable()) || !StringHelper.isNullOrEmpty(bus.BsRepsql) || !StringHelper.isNullOrEmpty(bus.BsRspsql)) {
                DataSet ds = new DataSet();
                Object param = bus.Params;
                mapper.
                IMappedStatement statement = mapper.GetMappedStatement("Search_CommSql_" + bus.BsCode);

                if (!mapper.IsSessionStarted) {
                    mapper.OpenConnection(); //确保LocalSession
                }
                if (mapper.LocalSession.Connection == null) {
                    mapper.LocalSession.OpenConnection(); //确保Connection
                }


                RequestScope scope = statement.Statement.Sql.GetRequestScope(statement, param, mapper.LocalSession);
                //IDictionary<ParameterProperty, IDbDataParameter> outDirct = new Dictionary<ParameterProperty, IDbDataParameter>();
                if (!bus.getBsStatusProc().equals("Y")) {
                    if (scope.PreparedStatement.DbParameters != null && scope.PreparedStatement.DbParameters.Length > 0) {
                        StringCollection dbParametersName = scope.PreparedStatement.DbParametersName;
                        IDbDataParameter[] dbParameters = scope.PreparedStatement.DbParameters;
                        int count = dbParametersName.Count;
                        ParameterMap parameterMap = mapper.ParameterMaps["MyIBatisData.generate_params_" + bus.BsCode] instanceof ParameterMap ? (ParameterMap) mapper.ParameterMaps["MyIBatisData.generate_params_" + bus.BsCode] : null;
                        if (parameterMap != null) {
                            for (int i = 0; i < count; i++) {
                                IDbDataParameter parameter = dbParameters[i];
                                ParameterProperty mapping = scope.ParameterMap.GetProperty(i);
                                ParameterProperty gparam = parameterMap.GetProperty(mapping.PropertyName);
                                // ParameterProperty gparam = GetPropertyByName(mapping.PropertyName, parameterMap);
                                if (gparam == null) {
                                    continue;
                                }
                                mapping.CallBackName = gparam.CallBackName;
                                mapping.CLRType = gparam.CLRType;
                                mapping.ColumnName = gparam.ColumnName;
                                mapping.DbType = gparam.DbType;
                                mapping.Direction = gparam.Direction;
                                mapping.DirectionAttribute = gparam.DirectionAttribute;
                                //mapping.NullValue = gparam.NullValue;
                                mapping.Precision = gparam.Precision;
                                mapping.PropertyName = gparam.PropertyName;
                                mapping.Scale = gparam.Scale;
                                mapping.Size = gparam.Size;
                                mapping.TypeHandler = gparam.TypeHandler; //自定义类型读取处理器

                                parameter.Direction = mapping.Direction;
                                if (gparam.Size > -1) {
                                    parameter.Size = mapping.Size;
                                }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Precision > Byte.MinValue)
                                if (gparam.Precision > Byte.MIN_VALUE) {
                                    parameter.Precision = mapping.Precision;
                                }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Scale > Byte.MinValue)
                                if (gparam.Scale > Byte.MIN_VALUE) {
                                    parameter.Scale = mapping.Scale;
                                }
                                if (gparam.DbType.equals("decimal")) {
                                    parameter.DbType = DbType.Decimal;
                                }

                                //add by mhh 20160314 mysql
                                if (gparam.DbType.equals("varchar") && CommServiceConfig.Instance.DbProviderName.equals("MYSQL")) {
                                    parameter.DbType = DbType.String;
                                }
                            }
                        }
                    }
                }

                if (mapper.LocalSession != null && mapper.LocalSession.Connection == null) {
                    mapper.LocalSession.OpenConnection();
                }

                //add by mhh 20160321
                if (CommServiceConfig.Instance.DbProviderName.equals("MYSQL")) {
                    (param instanceof Hashtable ? (Hashtable) param : null).Remove("SYS_IBATIS_COUNT");
                }
                //foreach (DictionaryEntry var in bus.Params)
                //    log.Info("键值:" + var.Key.ToString() + "参数值:" + var.Value.ToString());
                statement.PreparedCommand.Create(scope, mapper.LocalSession, statement.Statement, param);
                command = GetInnerDbCommand(scope.IDbCommand);
                //log.Info("执行数据语句" + scope.PreparedStatement.PreparedSql);
                IDbDataAdapter dda = mapper.LocalSession.CreateDataAdapter(command);
                Stopwatch s = new Stopwatch();
                s.Start();
                dda.Fill(ds);
                s.Stop();
                //MODFIY MHH 20150909
                //log.Exp(s.ElapsedMilliseconds.ToString());
                int X = 0;
                for (IDbDataParameter para : scope.IDbCommand.Parameters) {
                    if (para.Direction != ParameterDirection.Input) {
                        */
/*ParameterProperty mapping = scope.ParameterMap.GetProperty(X);
                        //将输出对象写入到Hashtable中
                        tangible.RefObject<Object> tempRef_param = new tangible.RefObject<Object>(param);
                        scope.ParameterMap.SetOutputParameter(tempRef_param, mapping, para.Value);
                        param = tempRef_param.argValue;*//*

                    }
                    X++;
                }
                if (ds != null && ds.Tables.Count > 0) {
                    table.argValue = ds.Tables[0];
                }


            }
            ireturn = true;

            //log.Info("完成创建对象处理CreateDataTable");

        } catch (IBatisNet.Common.Exceptions.IBatisNetException ex) {
            msg.argValue = ex.getMessage();
            ireturn = false;
        } finally {
            if (command != null) {
                if (command.Parameters.Count > 0) {
                    java.lang.reflect.Method mi = command.Parameters[0].getClass().getMethod("Dispose", BindingFlags.Instance.getValue() | BindingFlags.Public.getValue());
                    if (mi != null) {
                        for (int i = 0; i < command.Parameters.Count; i++) {
                            mi.Invoke(command.Parameters[i], null);
                        }
                    }
                }
                command.Dispose();
            }
            if (!mapper.LocalSession.IsTransactionStart) //在事务中不关闭
            {
                mapper.LocalSession.CloseConnection();
            }
        }
        return ireturn;
    }
*/



    /**
     * 产生记录集合
     *
     * @param mapper    系统可用的mapper操作对象
     * @param bscode    当前业务代码
     * @param statename MAP名称
     * @param table     需要产生的数据集合
     * @param msg       返回的消息
     * @return 创建是否成功
     */
/*
    public boolean CreateDataTableForSelfFun(SqlMapper mapper, String bscode, String statename, Object param, RefObject<DataTable> table, RefObject<String> msg) {
        //log.Info("进入创建对象处理CreateDataTableForSelfFun");
        boolean ireturn = false;
        IDbCommand command = null;
        try {
            DataSet ds = new DataSet();
            IMappedStatement statement = mapper.GetMappedStatement(statename);

            if (!mapper.IsSessionStarted) {
                mapper.OpenConnection(); //确保LocalSession
            }
            if (mapper.LocalSession.Connection == null) {
                mapper.LocalSession.OpenConnection(); //确保Connection
            }

            RequestScope scope = statement.Statement.Sql.GetRequestScope(statement, param, mapper.LocalSession);
            //IDictionary<ParameterProperty, IDbDataParameter> outDirct = new Dictionary<ParameterProperty, IDbDataParameter>();
            if (scope.PreparedStatement.DbParameters != null && scope.PreparedStatement.DbParameters.Length > 0) {
                StringCollection dbParametersName = scope.PreparedStatement.DbParametersName;
                IDbDataParameter[] dbParameters = scope.PreparedStatement.DbParameters;
                int count = dbParametersName.Count;
                ParameterMap parameterMap = mapper.ParameterMaps["MyIBatisData.generate_params_" + bscode] instanceof ParameterMap ? (ParameterMap) mapper.ParameterMaps["MyIBatisData.generate_params_" + bscode] : null;
                if (parameterMap != null) {
                    //log.Exp("dbParametersName.Count" + count.ToString());
                    for (int i = 0; i < count; i++) {
                        IDbDataParameter parameter = dbParameters[i];
                        ParameterProperty mapping = scope.ParameterMap.GetProperty(i);
                        ParameterProperty gparam = parameterMap.GetProperty(mapping.PropertyName);
                        // ParameterProperty gparam = GetPropertyByName(mapping.PropertyName, parameterMap);
                        if (gparam == null) {
                            continue;
                        }
                        mapping.CallBackName = gparam.CallBackName;
                        mapping.CLRType = gparam.CLRType;
                        mapping.ColumnName = gparam.ColumnName;
                        mapping.DbType = gparam.DbType;
                        mapping.Direction = gparam.Direction;
                        mapping.DirectionAttribute = gparam.DirectionAttribute;
                        //mapping.NullValue = gparam.NullValue;
                        mapping.Precision = gparam.Precision;
                        mapping.PropertyName = gparam.PropertyName;
                        mapping.Scale = gparam.Scale;
                        mapping.Size = gparam.Size;
                        mapping.TypeHandler = gparam.TypeHandler; //自定义类型读取处理器

                        parameter.Direction = mapping.Direction;
                        if (gparam.Size > -1) {
                            parameter.Size = mapping.Size;
                        }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Precision > Byte.MinValue)
                        if (gparam.Precision > Byte.MIN_VALUE) {
                            parameter.Precision = mapping.Precision;
                        }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Scale > Byte.MinValue)
                        if (gparam.Scale > Byte.MIN_VALUE) {
                            parameter.Scale = mapping.Scale;
                        }
                        if (gparam.DbType.equals("decimal")) {
                            parameter.DbType = DbType.Decimal;
                        }
                        //add by mhh 20160314 mysql
                        if (gparam.DbType.equals("varchar") && CommServiceConfig.Instance.DbProviderName.equals("MYSQL")) {
                            parameter.DbType = DbType.String;
                        }
                        //if (mapping.Direction != ParameterDirection.Input) {
                        //    outDirct.Add(mapping, parameter);
                        //}

                    }
                }
            }

            if (mapper.LocalSession != null && mapper.LocalSession.Connection == null) {
                mapper.LocalSession.OpenConnection();
            }


            statement.PreparedCommand.Create(scope, mapper.LocalSession, statement.Statement, param);
            command = GetInnerDbCommand(scope.IDbCommand);
            //log.Info("执行数据语句" + scope.PreparedStatement.PreparedSql);

            IDbDataAdapter dda = mapper.LocalSession.CreateDataAdapter(command);
            //log.Exp("Fill前");
            //foreach (var parameter in command.Parameters)
            //{
            //    log.Exp(parameter.GetType().ToString() + parameter.ToString());
            //}
            dda.Fill(ds);
            //log.Exp("Fill后");
            int X = 0;
            //log.Exp(scope.IDbCommand.Parameters.Count.ToString());
            for (IDbDataParameter para : scope.IDbCommand.Parameters) {
                if (para.Direction != ParameterDirection.Input) {
                    ParameterProperty mapping = scope.ParameterMap.GetProperty(X);
                    //将输出对象写入到Hashtable中
                    tangible.RefObject<Object> tempRef_param = new tangible.RefObject<Object>(param);
                    scope.ParameterMap.SetOutputParameter(tempRef_param, mapping, para.Value);
                    param = tempRef_param.argValue;
                }
                X++;
                //log.Exp(para.Value.ToString());
            }
            if (ds != null && ds.Tables.Count > 0) {
                table.argValue = ds.Tables[0];
            }

            ireturn = true;
        } catch (IBatisNet.Common.Exceptions.IBatisNetException ex) {
            msg.argValue = ex.getMessage();
            log.Exp("IBatisNetException:" + ex.toString());
            ireturn = false;
        } finally {
            if (command != null) {
                if (command.Parameters.Count > 0) {
                    java.lang.reflect.Method mi = command.Parameters[0].getClass().getMethod("Dispose", BindingFlags.Instance.getValue() | BindingFlags.Public.getValue());
                    if (mi != null) {
                        for (int i = 0; i < command.Parameters.Count; i++) {
                            mi.Invoke(command.Parameters[i], null);
                        }
                    }
                }
                command.Dispose();
            }
            if (!mapper.LocalSession.IsTransactionStart) //在事务中不关闭
            {
                mapper.LocalSession.CloseConnection();
            }
        }
        //log.Info("完成创建对象处理CreateDataTableForSelfFun");
        return ireturn;
    }
*/

    /**
     * 产生记录集合
     *
     * @param mapper    系统可用的mapper操作对象
     * @param bscode    当前业务代码
     * @param statename MAP名称
     * @param  o    需要产生的数据集合
     * @param msg       返回的消息
     * @return 创建是否成功
     */
    public boolean CreateObjectForSelfFun(SqlMapper mapper, String bscode, String statename, Object param, RefObject<Object> o, RefObject<String> msg) {
        //log.Info("进入创建对象处理CreateObjectForSelfFun");
       /* boolean ireturn = false;
        IDbCommand command = null;
        try {
            IMappedStatement statement = mapper.GetMappedStatement(statename);

            if (!mapper.IsSessionStarted) {
                mapper.OpenConnection(); //确保LocalSession
            }
            if (mapper.LocalSession.Connection == null) {
                mapper.LocalSession.OpenConnection(); //确保Connection
            }

            RequestScope scope = statement.Statement.Sql.GetRequestScope(statement, param, mapper.LocalSession);
            //IDictionary<ParameterProperty, IDbDataParameter> outDirct = new Dictionary<ParameterProperty, IDbDataParameter>();
            if (scope.PreparedStatement.DbParameters != null && scope.PreparedStatement.DbParameters.Length > 0) {
                StringCollection dbParametersName = scope.PreparedStatement.DbParametersName;
                IDbDataParameter[] dbParameters = scope.PreparedStatement.DbParameters;
                int count = dbParametersName.Count;
                ParameterMap parameterMap = mapper.ParameterMaps["MyIBatisData.generate_params_" + bscode] instanceof ParameterMap ? (ParameterMap) mapper.ParameterMaps["MyIBatisData.generate_params_" + bscode] : null;
                if (parameterMap != null) {
                    for (int i = 0; i < count; i++) {
                        IDbDataParameter parameter = dbParameters[i];
                        ParameterProperty mapping = scope.ParameterMap.GetProperty(i);
                        ParameterProperty gparam = parameterMap.GetProperty(mapping.PropertyName);
                        // ParameterProperty gparam = GetPropertyByName(mapping.PropertyName, parameterMap);
                        if (gparam == null) {
                            continue;
                        }
                        mapping.CallBackName = gparam.CallBackName;
                        mapping.CLRType = gparam.CLRType;
                        mapping.ColumnName = gparam.ColumnName;
                        mapping.DbType = gparam.DbType;
                        mapping.Direction = gparam.Direction;
                        mapping.DirectionAttribute = gparam.DirectionAttribute;
                        //mapping.NullValue = gparam.NullValue;
                        mapping.Precision = gparam.Precision;
                        mapping.PropertyName = gparam.PropertyName;
                        mapping.Scale = gparam.Scale;
                        mapping.Size = gparam.Size;
                        mapping.TypeHandler = gparam.TypeHandler; //自定义类型读取处理器

                        parameter.Direction = mapping.Direction;
                        if (gparam.Size > -1) {
                            parameter.Size = mapping.Size;
                        }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Precision > Byte.MinValue)
                        if (gparam.Precision > Byte.MIN_VALUE) {
                            parameter.Precision = mapping.Precision;
                        }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Scale > Byte.MinValue)
                        if (gparam.Scale > Byte.MIN_VALUE) {
                            parameter.Scale = mapping.Scale;
                        }
                        if (gparam.DbType.equals("decimal")) {
                            parameter.DbType = DbType.Decimal;
                        }

                        //add by mhh 20160314 mysql
                        if (gparam.DbType.equals("varchar") && CommServiceConfig.Instance.DbProviderName.equals("MYSQL")) {
                            parameter.DbType = DbType.String;
                        }
                        //if (mapping.Direction != ParameterDirection.Input) {
                        //    outDirct.Add(mapping, parameter);
                        //}

                    }
                }
            }

            if (mapper.LocalSession != null && mapper.LocalSession.Connection == null) {
                mapper.LocalSession.OpenConnection();
            }


            //statement.PreparedCommand.Create(scope, mapper.LocalSession, statement.Statement, param);
            o.argValue = statement.ExecuteQueryForObject(mapper.LocalSession, param);
            command = scope.IDbCommand;
            //log.Info("执行数据语句CreateObjectForSelfFun" + scope.PreparedStatement.PreparedSql);
            //o = GetInnerDbCommand(scope.IDbCommand).ExecuteScalar();

            ireturn = true;
        } catch (RuntimeException ex) {
            msg.argValue = ex.getMessage();
            log.Exp("CreateObjectForSelfFun方法,object,BS_CODE: " + bscode + ",异常内容" + msg.argValue + ",异常堆栈:" + ex.StackTrace + "InnerException:" + ex.getCause());
            ireturn = false;
        } finally {
            if (command != null) {
                if (command.Parameters.Count > 0) {
                    java.lang.reflect.Method mi = command.Parameters[0].getClass().getMethod("Dispose", BindingFlags.Instance.getValue() | BindingFlags.Public.getValue());
                    if (mi != null) {
                        for (int i = 0; i < command.Parameters.Count; i++) {
                            mi.Invoke(command.Parameters[i], null);
                        }
                    }
                }
                command.Dispose();
            }
            if (!mapper.LocalSession.IsTransactionStart) //在事务中不关闭
            {
                mapper.LocalSession.CloseConnection();
            }
        }
        //log.Info("完成创建对象处理");*/
        /*return ireturn;*/
        return true;
    }

    /**
     * 产生记录集合
     *
     * @param mapper    系统可用的mapper操作对象
     * @param bscode    当前业务代码
     * @param statename MAP名称
     * @param list     需要产生的数据集合
     * @param msg       返回的消息
     * @return 创建是否成功
     */
    public boolean CreateListForSelfFun(SqlMapper mapper, String bscode, String statename, Object param, RefObject<List> list, RefObject<String> msg) {
        //log.Info("进入创建对象处理CreateListForSelfFun");
        /*boolean ireturn = false;
        IDbCommand command = null;
        try {
            IMappedStatement statement = mapper.GetMappedStatement(statename);

            if (!mapper.IsSessionStarted) {
                mapper.OpenConnection(); //确保LocalSession
            }
            if (mapper.LocalSession.Connection == null) {
                mapper.LocalSession.OpenConnection(); //确保Connection
            }

            RequestScope scope = statement.Statement.Sql.GetRequestScope(statement, param, mapper.LocalSession);
            //IDictionary<ParameterProperty, IDbDataParameter> outDirct = new Dictionary<ParameterProperty, IDbDataParameter>();
            if (scope.PreparedStatement.DbParameters != null && scope.PreparedStatement.DbParameters.Length > 0) {
                StringCollection dbParametersName = scope.PreparedStatement.DbParametersName;
                IDbDataParameter[] dbParameters = scope.PreparedStatement.DbParameters;
                int count = dbParametersName.Count;
                ParameterMap parameterMap = mapper.ParameterMaps["MyIBatisData.generate_params_" + bscode] instanceof ParameterMap ? (ParameterMap) mapper.ParameterMaps["MyIBatisData.generate_params_" + bscode] : null;
                if (parameterMap != null) {
                    for (int i = 0; i < count; i++) {
                        IDbDataParameter parameter = dbParameters[i];
                        ParameterProperty mapping = scope.ParameterMap.GetProperty(i);
                        ParameterProperty gparam = parameterMap.GetProperty(mapping.PropertyName);
                        // ParameterProperty gparam = GetPropertyByName(mapping.PropertyName, parameterMap);
                        if (gparam == null) {
                            continue;
                        }
                        mapping.CallBackName = gparam.CallBackName;
                        mapping.CLRType = gparam.CLRType;
                        mapping.ColumnName = gparam.ColumnName;
                        mapping.DbType = gparam.DbType;
                        mapping.Direction = gparam.Direction;
                        mapping.DirectionAttribute = gparam.DirectionAttribute;
                        //mapping.NullValue = gparam.NullValue;
                        mapping.Precision = gparam.Precision;
                        mapping.PropertyName = gparam.PropertyName;
                        mapping.Scale = gparam.Scale;
                        mapping.Size = gparam.Size;
                        mapping.TypeHandler = gparam.TypeHandler; //自定义类型读取处理器

                        parameter.Direction = mapping.Direction;
                        if (gparam.Size > -1) {
                            parameter.Size = mapping.Size;
                        }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Precision > Byte.MinValue)
                        if (gparam.Precision > Byte.MIN_VALUE) {
                            parameter.Precision = mapping.Precision;
                        }
//C# TO JAVA CONVERTER WARNING: Unsigned integer types have no direct equivalent in Java:
//ORIGINAL LINE: if (gparam.Scale > Byte.MinValue)
                        if (gparam.Scale > Byte.MIN_VALUE) {
                            parameter.Scale = mapping.Scale;
                        }
                        if (gparam.DbType.equals("decimal")) {
                            parameter.DbType = DbType.Decimal;
                        }

                        //add by mhh 20160314 mysql
                        if (gparam.DbType.equals("varchar") && CommServiceConfig.Instance.DbProviderName.equals("MYSQL")) {
                            parameter.DbType = DbType.String;
                        }
                        //if (mapping.Direction != ParameterDirection.Input) {
                        //    outDirct.Add(mapping, parameter);
                        //}

                    }
                }
            }

            if (mapper.LocalSession != null && mapper.LocalSession.Connection == null) {
                mapper.LocalSession.OpenConnection();
            }


            //statement.PreparedCommand.Create(scope, mapper.LocalSession, statement.Statement, param);

            list.argValue = statement.ExecuteQueryForList(mapper.LocalSession, param);
            command = scope.IDbCommand;


            //log.Info("执行数据语句" + scope.PreparedStatement.PreparedSql);
            //o = GetInnerDbCommand(scope.IDbCommand).ExecuteScalar();
            ireturn = true;
        } catch (RuntimeException ex) {
            msg.argValue = ex.getMessage();
            log.Exp("CreateListForSelfFun方法, IList,BS_CODE: " + bscode + ",异常内容:" + msg.argValue + ",异常堆栈:" + ex.StackTrace + "InnerException:" + ex.getCause());
            ireturn = false;
        } finally {
            if (command != null) {
                if (command.Parameters.Count > 0) {
                    java.lang.reflect.Method mi = command.Parameters[0].getClass().getMethod("Dispose", BindingFlags.Instance.getValue() | BindingFlags.Public.getValue());
                    if (mi != null) {
                        for (int i = 0; i < command.Parameters.Count; i++) {
                            mi.Invoke(command.Parameters[i], null);
                        }
                    }
                }
                command.Dispose();
            }
            if (!mapper.LocalSession.IsTransactionStart) //在事务中不关闭
            {
                mapper.LocalSession.CloseConnection();
            }
        }
        //log.Info("完成创建对象处理CreateListForSelfFun");
        return ireturn;*/
        return true;
    }

   /* public SqlMapper CreateSqlMapper() {
        Object tempVar = new KingT.MessageExchange.BaseServiceLib.MapperDao.SqlMapper(_mapper, true);
        mapper = tempVar instanceof ISqlMapper ? (ISqlMapper) tempVar : null;
        return mapper;
    }*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion


//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region Validate验证处理

    /**
     * 用户验证
     *
     * @param usercode 用户代码
     * @param password 密码
     * @param users    用户队列
     * @param msg      消息对象
     */
    protected void ValidateUser(String usercode, String password, List<KttranUserinfo> users, MessageCode msg)//.net+ out表示返回一个对象
    {//
        for (KttranUserinfo user : users) {
            if (usercode == user.getFUrCode()) {//确认账户
                System.out.println(user);
                if (user.getFUseStatus() == "N") {
                    msg = MessageCode.此账户已被停用;
                    System.out.println("账户被停用");
                }
                if (user.getFUrPassword() == password) {
                    msg = MessageCode.成功;
                    System.out.println("成功");
                } else {
                    System.out.println("密码验证失败");
                    msg = MessageCode.密码验证失败;
                }

            } else {
                System.out.println("无此用户");
                msg = MessageCode.无此用户;

            }
        }
        /*KttranUserinfo u = users.<KttranUserinfo>(user ->user==usercode));
        currentUser.argValue = u;
        if (u != null)
        {
            if (u.UseStatus.equals("N"))
            {
                msg.Code = MessageCode.此账户已被停用;
            }
            if (password.equals(SymmCrypto.GetDecryptingPassword(CommServiceConfig.Instance.DbUserName, CommServiceConfig.Instance.UserSerial, u.UrPassword)))
            {
                msg.Code = MessageCode.成功;
                currentUser.argValue = u;
            }
            else
            {
                msg.Code = MessageCode.密码验证失败;
            }
        }
        else
        {
            msg.Code = MessageCode.无此用户;
        }*/
    }

   /* protected void VaildSerial(Map<String, KttranBusiness> businesss, String bsCode, KingT.MessageExchange.IService.Domain.Message msg)
    {
        if (businesss.get(bsCode).SerialFlag)
        {
           MessageCode.SUCCESS;
        }
        else
        {

            msg.Code = MessageCode.业务未注册;
        }
    }
*/

    /**
     * 业务验证
     *
     * @param userid    用户代码
     * @param svrcode   业务代码
     * @param userauths 用户权限队列
     * @param msg       消息对象
     */
    protected void ValidateBusiness(BigDecimal userid, String svrcode, List<KttranAuthority> userauths, MessageCode msg) {
        for (KttranAuthority users : userauths) {
            if (users.getFUrId() == userid && users.getFSvrCode() == svrcode) {
                msg = MessageCode.成功;
            } else {
                msg = MessageCode.无业务权限;
            }
        }
    }
}




    ///#region 业务下发判断 mhh 20150722
    /**
     判断是否下发业务 20150701 mhh

     @param bscode
     @param msg
     @return
     */
  /*  public final boolean ValidateBusinessIsDown(String bscode, List<KttranBusiness> bussiness, KingT.MessageExchange.IService.Domain.Message msg)
    {
        //IList<KttranBusiness> bussinesslist = mapper.QueryForList<KttranBusiness>("GeAll_KttranBusiness", null);
        int count = bussiness.<KttranBusiness>Where(bussines -> (bussines.DownFlag.equals("Y") && bscode.equals(bussines.BsCode))).Count();
        if (count > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }*/

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region 机构权限判断 mhh 20150722

    /**
     判断机构下发权限验证 20150701 mhh

     @param userid
     @param svrcode
     @param orgcode
     @param bsauths
     @param msg
     @return
     */
   /* public final void ValidateBusinessAuth(BigDecimal userid, String svrcode, String orgcode, List<KttranBusinessVaild> bsauths, KingT.MessageExchange.IService.Domain.Message msg)
    {
        int count = bsauths.<KttranBusinessVaild>Where(bsauth -> (bsauth.Auth.equals("Y") && svrcode.equals(bsauth.BsCode) && userid.equals(bsauth.UrId) && orgcode.equals(bsauth.OrgCode))).Count();
        if (count > 0)
        {
            msg.Code = MessageCode.成功;
            //return true;
        }
        else
        {
            msg.Code = MessageCode.无业务权限;
            msg.Info = "用户没有权限调阅该机构业务";
            //return false;
        }
    }*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
    ///#region 判断是否是测试用户--可以放到用户验证进行判断
    /**
     判断是否是测试用户 fjx 20150722

     @param bscode
     @param msg

     @return
     */
   /* public final boolean ValidateUserIsTest(String userid, List<KttranUserinfo> userinfos, KingT.MessageExchange.IService.Domain.Message msg)
    {
        int count = userinfos.<KttranUserinfo>Where(userinfo -> (userinfo.IsTest.equals("Y") && userid.equals(userinfo.UrCode))).Count();
        if (count > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }*/

    ///#region 获取DataTable前几条数据
    /**
     获取DataTable前几条数据

     @param TopItem 前N条数据
     @param oDT 源DataTable
     @return
     */
  /*  public static DataTable DtSelectTop(int TopItem, DataTable oDT)
    {
        if (oDT.Rows.Count < TopItem)
        {
            return oDT;
        }

        DataTable NewTable = oDT.Clone();
        DataRow[] rows = oDT.Select("1=1");
        for (int i = 0; i < TopItem; i++)
        {
            NewTable.ImportRow((DataRow)rows[i]);
        }
        return NewTable;
    }*/

