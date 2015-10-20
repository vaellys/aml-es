package com.ist.ioc.service.common.elasticsearch.impl;

import static com.ist.ioc.service.common.Constants.ES_ADD_ACTION;
import static com.ist.ioc.service.common.Constants.ES_BEGIN_COLUMN;
import static com.ist.ioc.service.common.Constants.ES_DATE_PATTERN;
import static com.ist.ioc.service.common.Constants.ES_END_COLUMN;
import static com.ist.ioc.service.common.Constants.ES_INDEX_NAME;
import static com.ist.ioc.service.common.Constants.ES_LIKECOND_BEGIN;
import static com.ist.ioc.service.common.Constants.ES_LIKECOND_END;
import static com.ist.ioc.service.common.Constants.ES_NOTEQUAL_BEGIN;
import static com.ist.ioc.service.common.Constants.ES_NOTEQUAL_END;
import static com.ist.ioc.service.common.Constants.ES_NOT_COLUMN;
import static com.ist.ioc.service.common.Constants.ES_PAGE_ID;
import static com.ist.ioc.service.common.Constants.ES_PAGE_SIZE;
import static com.ist.ioc.service.common.Constants.ES_PAGE_TOTALCOUNT;
import static com.ist.ioc.service.common.Constants.ES_RESULT;
import static com.ist.ioc.service.common.Constants.ES_RESULT_KEY;
import static com.ist.ioc.service.common.Constants.ES_SUCCESS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.ist.common.es.util.JsonUtils;
import com.ist.common.es.util.LogUtils;
import com.ist.common.es.util.page.DateUtils;
import com.ist.common.es.util.page.Pagination;
import com.ist.dto.bmp.ESDto;
import com.ist.ioc.service.common.elasticsearch.AbstractIESService;

public class IESServiceImpl extends AbstractIESService {
    private static final Log logger = LogFactory.getLog(IESServiceImpl.class);
    
    @Autowired
    SearchSourceBuilder searchSourceBuilder = null;

    @SuppressWarnings("unchecked")
    @Override
    public int updateIndexEs(String tableName, String json, String indexColName) throws IOException {
        try {
            if(logger.isDebugEnabled()){
                logger.debug("------------请求参数 ------- " + LogUtils.format("tableName", tableName, "jsonStr", json,  "indexColName", indexColName));
            }
            //将json串转化为map
            Map<String, Object> jsonMap = JsonUtils.jsonToObject(json, Map.class);
            //索引的字段
            String[] columns = StringUtils.split(indexColName.toLowerCase(), ",");
            //从json串中取结果列表 并转化为list
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) jsonMap.get(ES_RESULT);
            //索引的记录数
            List<Map<String, Object>> indexRecords = new ArrayList<Map<String, Object>>();
            Map<String, Object> record = null;
            for (Map<String, Object> map : resultList) {
                record = new HashMap<String, Object>();
                for(String column : columns){
                    if(map.containsKey(column)){
                        String value = (String) map.get(column);
                        value = convertDateToT(value);
                        record.put(column.toLowerCase(), value);
                    }
                }
                record.put(ES_RESULT_KEY, map.get(ES_RESULT_KEY));
                indexRecords.add(record);
            }
            //创建索引并索引文档
            return this.documentHandler(ES_INDEX_NAME, tableName, indexRecords, ES_ADD_ACTION);
        } catch (IOException e) {
            logger.error("构建索引失败" + LogUtils.format("tableName", tableName, "json", json, "indexColName", indexColName), e);
            throw new IOException("构建索引失败", e);
        }
    }

    private String convertDateToT(String value) {
        Pattern pattern = Pattern.compile(ES_DATE_PATTERN);
        Matcher matcher = pattern.matcher(value);
        if(matcher.matches()){
            if(value.length() == 8){
                value = DateUtils.dateToStringT(DateUtils.stringToDateShortL(value));
            }else if (value.length() == 10){
                value = DateUtils.dateToStringT(DateUtils.stringToDateShort(value));
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int deleteIndexEs(String tableName, String json) throws IOException {
        try {
            //将json串转化为map
            Map<String, Object> jsonMap = JsonUtils.jsonToObject(json, Map.class);
            //从json串中取结果列表 并转化为list
            List<Map<String, Object>> resultList = (List<Map<String, Object>>) jsonMap.get(ES_RESULT);
            //从结果列表中获取rowkey
            for (Map<String, Object> map : resultList) {
                String key = (String) map.get(ES_RESULT_KEY);
                this.deleteDoc(ES_INDEX_NAME, tableName, key);
            }
            return ES_SUCCESS;
        } catch (Exception e) {
            logger.error("删除索引失败" + LogUtils.format("tableName", tableName, "json", json), e);
            throw new IOException("构建索引失败", e);
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public String searchEs(String tableName, String json) throws IOException {
        // 将json串转化为map
        Map<String, Object> resultMap = JsonUtils.jsonToObject(json, Map.class);
        // 获取当前页
        Integer pageNow =  Integer.parseInt((String) resultMap.get(ES_PAGE_ID));
        Integer pageSize = Integer.parseInt((String) resultMap.get(ES_PAGE_SIZE));
        String totalCount = (String) resultMap.get(ES_PAGE_TOTALCOUNT);
        // 使用布尔查询 将多个查询条件联合起来
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            String key = entry.getKey().toUpperCase();
            String value = (String) entry.getValue();
            if (value.startsWith(ES_NOTEQUAL_BEGIN) && value.endsWith(ES_NOTEQUAL_END)) {// 对于!=的设置
                value = value.replace(ES_NOTEQUAL_BEGIN, "").replace(ES_NOTEQUAL_END, "");
                TermQueryBuilder termQuery = this.termBuilder(key.toLowerCase(), value.toLowerCase());
                boolQuery.mustNot(termQuery);
            } else if (value.startsWith("'") && value.endsWith("'")) {// CASE_KIND IN ('N', 'A')
                List<String> values = Arrays.asList(StringUtils.split(value.toLowerCase(), "','"));
                TermsQueryBuilder termsBuilder = this.termsBuilder(key.toLowerCase(), values);
                boolQuery.must(termsBuilder);
            } else if (value.startsWith(ES_LIKECOND_BEGIN) && value.endsWith(ES_LIKECOND_END)) {// 模糊查询
                value = value.replace(ES_LIKECOND_BEGIN, "").replace(ES_LIKECOND_END, "");
                QueryStringQueryBuilder queryBuilder = this.queryStringBuilder(key.toLowerCase(), value);
                boolQuery.must(queryBuilder);
//                MoreLikeThisQueryBuilder likeBuilder = this.likeBuilder(key.toLowerCase(), value);
//                boolQuery.must(likeBuilder);
//                MatchQueryBuilder matchBuilder = this.matchBuilder(key.toLowerCase(), value);
//                boolQuery.must(matchBuilder);
//                FuzzyLikeThisFieldQueryBuilder fuzzyBuilder = this.fuzzyLikeBuilder(key.toLowerCase(), value);
//                boolQuery.must(fuzzyBuilder);
//                FuzzyQueryBuilder fuzzyBuilder = this.fuzzyBuilder(key.toLowerCase(), value);
//                boolQuery.must(fuzzyBuilder);
//                RegexpQueryBuilder regexBuilder = this.regexpBuilder(key.toLowerCase(), value);
//                boolQuery.must(regexBuilder);
//                WildcardQueryBuilder wildBuilder = this.wildcardBuilder(key.toLowerCase(), value);
//                boolQuery.must(wildBuilder);
            } else if (ArrayUtils.contains(ES_BEGIN_COLUMN, key)) { // >=
                String field = key.substring(0, key.length() - 1).toLowerCase(); // >=
                String convertValue = convertDateToT(value);
                RangeQueryBuilder rangeBuilder = this.rangeBuilderFrom(field, convertValue);
                boolQuery.must(rangeBuilder);
            } else if (ArrayUtils.contains(ES_END_COLUMN, key)) { // <=
                String field = key.substring(0, key.length() - 1).toLowerCase();
                String convertValue = convertDateToT(value);
                RangeQueryBuilder rangeBuilder = this.rangeBuilderTo(field, convertValue);
                boolQuery.must(rangeBuilder);
            } else if (!ArrayUtils.contains(ES_NOT_COLUMN, key)) {
                TermQueryBuilder termBuilder = this.termBuilder(key.toLowerCase(), value.toLowerCase());// =
                boolQuery.must(termBuilder);
            }
        }
        Map<String, Object> results = this.documentSearch(ES_INDEX_NAME, tableName, searchSourceBuilder, boolQuery, Pagination.cpn(pageNow),
                Pagination.cps(pageSize), totalCount);
        return JsonUtils.toJsonString(results);
    }

    
    @Override
    public void documentHandler(Map<String, List<String>> mapParams, List<ESDto> documents, Integer action) throws IOException {
    }

    @Override
    public void sqlHandler(ESDto esDto, List<String> organkeys, Integer action) throws IOException {
    }

    @Override
    public List<ESDto> documentSearch(String query, List<String> indexNames, List<String> indexTypes, Integer pageNow, Integer pageSize)
            throws IOException {
        return null;
    }

}
