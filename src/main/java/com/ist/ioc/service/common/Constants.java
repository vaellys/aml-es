package com.ist.ioc.service.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Constants {
    /**
     * 创建动作
     */
    public static final Integer ES_ADD_ACTION = 1;
    /**
     * 删除动作
     */
    public static final Integer ES_DELETE_ACTION = 2;
    /**
     * 更新动作
     */
    public static final Integer ES_UPDATE_ACTION = 3;
    /**
     * bulk局部更新参数
     */
    public static final String ES_UPDATE_DOC = "doc";
    /**
     * es公共索引库
     */
    public static final String ES_PUBLIC_INDEX = "publicIndex";
    /**
     * es公共索引类型
     */
    public static final String ES_PUBLIC_TYPE = "publicType";
    /**
     * json串中结果集字段
     */
    public static final String ES_RESULT = "RESULT";
    /**
     * json串中结果集rowkey
     */
    public static final String ES_RESULT_KEY = "hbase_rowkey";
    /**
     * 索引库的名字
     */
    public static final String ES_INDEX_NAME = "aml";
    /**
     * 索引生成时间
     */
    public static final String ES_INDEX_CREATE_TIME = "indexCreateTime";
    /**
     * es命中常量 
     */
    public static final String ES_HITS = "hits";
    
    /**
     * 返回结果状态 1 成功 0 失败
     */
    public static final int ES_SUCCESS = 1;
    public static final int ES_FAILED = 0;
    /**
     * 分页常量 : PAGE_ID 当前页 PAGE_TOTALCOUNT 如果传-1则查询总数, 否则不查询总数 PAGE_SIZE 每页多少条记录
     */
    public static final String ES_PAGE_ID = "PAGE_ID";
    public static final String ES_PAGE_TOTALCOUNT = "PAGE_TOTALCOUNT";
    public static final String ES_PAGE_SIZE = "PAGE_SIZE";
    public static final String ES_TOTAL_SIZE = "TOTAL_SIZE";
    public static final String ES_TABLE_NAME = "table_name";
    
    
    /**
     * 查询条件
     * '' in
     * NOTEQUAL[2015-12-12]NOTEQUAL 不等于 2015-12-12
     * LIKECOND[9902]LIKECOND LIKE  '%9902%'
     * >=, <= 
     * PARTY_ID = 000000025360599
     */
    public static final String ES_NOTEQUAL_BEGIN = "NOTEQUAL[";
    public static final String ES_NOTEQUAL_END = "]NOTEQUAL";
    public static final String ES_LIKECOND_BEGIN = "LIKECOND[";
    public static final String ES_LIKECOND_END = "]LIKECOND";
    
    //开始日期或金额字段 >=
    public static String[] ES_BEGIN_COLUMN = {"RPDT1","CASE_DATE1","TX_DT1","CNY_AMT1","SENDDATE_CH1","TSTM1","OPEN_DT1","TSTM1","STATISTIC_DT1","CREATE_DT1","MODIFYDATE1"};
    //结束日期或金额字段 <= 
    public static String[] ES_END_COLUMN = {"RPDT2","CASE_DATE2","TX_DT2","CNY_AMT2","SENDDATE_CH2","TSTM2","OPEN_DT2","TSTM2","STATISTIC_DT2","CREATE_DT2","MODIFYDATE2","SENDDATE_DT2"};
    //不包含列
    public static String[] ES_NOT_COLUMN = {ES_PAGE_ID, ES_PAGE_SIZE, ES_PAGE_TOTALCOUNT};
    //日期匹配
    public static String ES_DATE_PATTERN = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\-\\s]?((((0?" +"[13578])|(1[02]))[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))" +"|(((0?[469])|(11))[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(30)))|" +"(0?2[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12" +"35679])|([13579][01345789]))[\\-\\-\\s]?((((0?[13578])|(1[02]))" +"[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))" +"[\\-\\-\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\-\\s]?((0?[" +"1-9])|(1[0-9])|(2[0-8]))))))";

    private Constants(){
    }
    
    public static void main(String[] args) {
        String str = "2012-01-01";
        Pattern p = Pattern.compile(ES_DATE_PATTERN);
        Matcher matcher = p.matcher(str);
        System.out.println(matcher.matches());
    }
}