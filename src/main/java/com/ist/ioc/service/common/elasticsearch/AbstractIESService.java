package com.ist.ioc.service.common.elasticsearch;

import static com.ist.ioc.service.common.Constants.*;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.Search.Builder;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.core.Update;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.PutMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyLikeThisFieldQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.RegexpQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.ist.assemble.CustomImmutableSetting;
import com.ist.common.es.util.DateUtils;
import com.ist.common.es.util.LogUtils;
import com.ist.common.es.util.page.Pagination;
import com.ist.dto.bmp.ESDto;

/**
 * 提供创建映射，索引，搜索等服务
 * 
 * @author qianguobing
 */
public abstract class AbstractIESService  implements IESService {

    private static final Log logger = LogFactory.getLog(AbstractIESService.class);
    public static final String PAGE_TOTAL_COUNT = "-1";
    public static final int PAGE_TOTAL_COUNT_DEF = 1000;
    @Resource
    private JestClient client = null;
    @Resource
    private CustomImmutableSetting customImmutableSetting = null;
    
    public int documentHandler(String indexName, String indexType, List<Map<String, Object>> documents, Integer action) throws IOException {
        try {
            // es库名只能是小写字母
            indexName = StringUtils.lowerCase(indexName);
            // 删除索引
             this.deleteIndex(indexName);
            // 创建索引
            this.addIndex(indexName);
            // 设置默认的索引名字
            Bulk.Builder builder = new Bulk.Builder().defaultIndex(indexName);
            // 创建文档
            if (action == ES_ADD_ACTION && !documents.isEmpty()) {
                // 创建索引映射
                createIndexMapping(indexName, indexType);
                // 若文档为空则只创建索引类型
                builder.addAction(Arrays.asList(buildIndexAction(documents)));
                // 删除文档
            } else if (action == ES_DELETE_ACTION && !documents.isEmpty()) {
                builder.addAction(Arrays.asList(buildDeleteAction(documents)));
            } else if (action == ES_UPDATE_ACTION && !documents.isEmpty()) {
                builder.addAction(Arrays.asList(buildUpdateAction(documents)));
            }
            builder.defaultType(indexType);
            JestResult response = client.execute(builder.build());
            if (response.isSucceeded()) {
                return ES_SUCCESS;
            } else {
                return ES_FAILED;
            }
        } catch (IOException e) {
            logger.error("构建索引败:" + LogUtils.format("indexName", indexName, "indexType", indexType, "documents", documents), e);
            throw new IOException("构建索引败", e);
        }
    }
    
    /**
     * 创建索引
     * 
     * @param index
     *            索引名称
     * @param type
     *            索引类型
     */
    private void createIndexMapping(String index, String type) {
        try {
            logger.debug("params: " + LogUtils.format("index", index, "type", type));
            String mappingJsonStr = buildMappingJsonStr(type);
            PutMapping putMapping = new PutMapping.Builder(index, type, mappingJsonStr).build();
            client.execute(putMapping);
        } catch (IOException e) {
            logger.debug("创建索引映射失败 : " + LogUtils.format("index", index, "type", type), e);
        }
    }
    
    
    /**
     * 
     * "1364": { "_timestamp": { "enabled": true, "store": true, "path":
     * "createTime", "format": "yyyy-MM-dd HH:mm:ss" }, "properties": { "id": {
     * "type": "string" }, "createTime": { "store": true, "format":
     * "yyyy-MM-dd HH:mm:ss", "type": "date" }, "title": { "type": "string" },
     * "description": { "store": true, "analyzer": "ik", "boost": 4,
     * "term_vector": "with_positions_offsets", "type": "string", "fields": {
     * "py": { "store": true, "analyzer": "pinyin_analyzer", "boost": 4,
     * "term_vector": "with_positions_offsets", "type": "string" } } }, "path":
     * { "type": "string" } }, "_all": { "auto_boost": true } }
     * 通过一个根映射器构建器去设置各个字段来构建映射源
     * 
     * @param index
     *            索引名称
     * @param type
     *            索引类型
     * @return String 映射字符串
     * @throws IOException 
     */
    private String buildMappingJsonStr(String type) throws IOException {
        try {
            XContentBuilder content = XContentFactory
                    .jsonBuilder()
                    .startObject()
                    // 索引库名（类似数据库中的表）
                    .startObject(type)
                    .startObject("_timestamp").field("enabled", true).field("store", "yes").field("format", "yyyy-MM-dd HH:mm:ss").endObject()
                    .field("date_detection", true)
                    .endObject().endObject();
            logger.debug(LogUtils.format("----------------映射字符串---------------", content.string()));
            return content.string();
        } catch (IOException e) {
            logger.debug("构建映射字符串失败 " + LogUtils.format("type", type), e);
            throw new IOException("构建映射字符串失败", e);
        }
    }
    
    
    
    /**
     * 构建批量创建索引动作
     * 
     * @param documents
     *            文档列表
     * @return Index[] 返回添加动作
     */
    private Index[] buildIndexAction(List<Map<String, Object>> documents) {
        Index[] indexs = new Index[documents.size()];
        for (int i = 0; i < documents.size(); i++) {
            Map<String, Object> map = documents.get(i);
            map.put(ES_INDEX_CREATE_TIME, DateUtils.getCurrTime());
            String id = (String) map.get(ES_RESULT_KEY);
            indexs[i] = new Index.Builder(map).id(id).build();
        }
        return indexs;
    }

    /**
     * 构建批量删除动作
     * 
     * @param documents
     *            文档列表
     * @return Delete[] 返回删除动作
     */
    private Delete[] buildDeleteAction(List<Map<String, Object>> documents) {
        Delete[] deleteAction = new Delete[documents.size()];
        for (int i = 0; i < documents.size(); i++) {
            Map<String, Object> map = documents.get(i);
            String id = (String) map.get(ES_RESULT_KEY);
            deleteAction[i] = new Delete.Builder(id).build();
        }
        return deleteAction;
    }
    
    /**
     * 构建批量更新动作
     * 
     * @param documents
     *            文档列表
     * @return Update[] 返回更新动作
     */
    private Update[] buildUpdateAction(List<Map<String, Object>> documents) {
        Update[] updateAction = new Update[documents.size()];
        for (int i = 0; i < documents.size(); i++) {
            Map<String, Object> update = new HashMap<String, Object>();
            Map<String, Object> map = documents.get(i);
            String id = (String) map.get("id");
            update.put(ES_UPDATE_DOC, map);
            updateAction[i] = new Update.Builder(update).id(id).build();
        }
        return updateAction;
    }


    /**
     * 字段搜索
     * @param indexName
     * @param indexType
     * @param searchSourceBuilder
     * @param boolQuery
     * @param pageNow
     * @param pageSize
     * @param pageTotalCount 
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked"})
    public Map<String, Object> documentSearch(String indexName, String indexType, SearchSourceBuilder searchSourceBuilder,
            BoolQueryBuilder boolQuery, Integer pageNow, Integer pageSize, String pageTotalCount) throws IOException {
        try {
            SearchSourceBuilder ssb = searchSourceBuilder.query(boolQuery);
//            ssb.field(ES_RESULT_KEY);
            // 设置分页
            ssb.from((pageNow-1)*pageSize).size(pageSize);
            // 设置排序方式
             ssb.sort("_score", SortOrder.DESC);
            if (logger.isDebugEnabled()) {
                logger.debug("query string: " + ssb.toString());
            }
            Builder builder = new Search.Builder(ssb.toString());
            // 构建搜索
            Search search = builder.addIndex(indexName).addType(indexType).allowNoIndices(true).ignoreUnavailable(true).build();
            SearchResult result = client.execute(search);

            Map<String, Object> map = new HashMap<String, Object>();
            List<String> list = new ArrayList<String>();
            Pagination p = null;
            Integer totalCount = 0;
           
            if (result.isSucceeded()) {
                if(PAGE_TOTAL_COUNT.equals(pageTotalCount)){
                    totalCount = result.getTotal();
                    p = new Pagination(pageNow, pageSize, totalCount);
                }else if (StringUtils.isBlank(pageTotalCount)){
                    totalCount = PAGE_TOTAL_COUNT_DEF;
                }else {
                    totalCount = Integer.parseInt(pageTotalCount);
                }
                List<Hit<Map, Void>> hits = result.getHits(Map.class);
                for (Hit<Map, Void> hit : hits) {
                   Map<String, String> source = hit.source;
                   String rowKey = source.get(ES_RESULT_KEY);
                   list.add(rowKey);
                }
            }
            map.put(ES_PAGE_ID, pageNow);
            map.put(ES_PAGE_SIZE, pageSize);
            map.put("PAGE_TOTAL", null == p ? 1 : p.getTotalPage());
            map.put(ES_TABLE_NAME, indexType.toLowerCase());
            map.put(ES_TOTAL_SIZE, totalCount);
            map.put(ES_RESULT, list);
            logger.debug("--------------------------------结果--------------------------" + LogUtils.format("r", map));
            return map;
        } catch (IOException e) {
            logger.error("查询失败 ", e);
            throw new IOException("查询失败 ", e);
        }
    }
    
    /**
     * 构建单个词条
     * @param field
     * @param value
     * @return
     */
    protected TermQueryBuilder termBuilder(String field, String value) {
         //词条查询
       return QueryBuilders.termQuery(field, value);
    }
    /**
     * 范围构建
     * @param field
     * @param from
     * @return
     */
    protected RangeQueryBuilder rangeBuilderFrom(String field, String value) {
        //范围查询
        return QueryBuilders.rangeQuery(field).gte(value);
    }
    /**
     * 范围构建
     * @param field
     * @param from
     * @return
     */
    protected RangeQueryBuilder rangeBuilderTo(String field, String value) {
        //范围查询
        return QueryBuilders.rangeQuery(field).lte(value);
    }
    /**
     * like
     * @param field
     * @param value
     * @return
     */
    protected MoreLikeThisQueryBuilder likeBuilder(String field, String value){
        //like查询
        return QueryBuilders.moreLikeThisQuery(field).likeText(value).minDocFreq(1).minTermFreq(1);
    }
    
    /**
     * fuzzy_like_this
     * @param field
     * @param value
     * @return
     */
    protected FuzzyLikeThisFieldQueryBuilder fuzzyLikeBuilder(String field, String value){
        //like查询
        return QueryBuilders.fuzzyLikeThisFieldQuery(field).likeText(value).fuzziness(Fuzziness.fromSimilarity(0.0f));
    }
    
    /**
     * fuzzy
     * @param field
     * @param value
     * @return
     */
    protected FuzzyQueryBuilder fuzzyBuilder(String field, String value){
        //like查询
        return QueryBuilders.fuzzyQuery(field, value);
    }
    /**
     * match
     * @param field
     * @param value
     * @return
     */
    protected MatchQueryBuilder matchBuilder(String field, String value){
        //like查询
        return QueryBuilders.matchQuery(field, value).fuzziness(0.5);
    }
    
    /**
     * 多个词条
     * @param field
     * @param values
     * @return
     */
    protected TermsQueryBuilder termsBuilder(String field, List<String> values){
        //多个词条查询
        return QueryBuilders.termsQuery(field, values);
    }
    
    protected QueryStringQueryBuilder queryStringBuilder(String field, String value){
        //多个词条查询
        return QueryBuilders.queryString("*" + value + "*").defaultField(field).analyzeWildcard(true).allowLeadingWildcard(true);
    }
    
    protected RegexpQueryBuilder regexpBuilder(String field, String value){
        //多个词条查询
        return QueryBuilders.regexpQuery(field, "*" + value +"*");
    }
    
    protected WildcardQueryBuilder wildcardBuilder(String field, String value){
        //多个词条查询
        return QueryBuilders.wildcardQuery(field, "*" + value + "*");
    }

    /**
     * 根据id删除指定的文档
     * 
     * @param indexName
     *            索引名称
     * @param indexType
     *            索引类型
     * @param docId
     *            文档id
     * @return boolean
     * @throws IOException 
     */
    @Override
    public boolean deleteDoc(String indexName, String indexType, String docId) throws IOException {
        JestResult result = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("indexName:" + indexName + " indexType:" + indexType + " docId:" + docId);
            }
            Delete d = new Delete.Builder(docId).index(indexName).type(indexType).build();
            result = client.execute(d);
            if (null != result && result.isSucceeded()) {
                return true;
            }
            return false;
        } catch (IOException e) {
            logger.error("删除索引失败", e);
            throw new IOException("删除索引失败", e);
        }
    }

    /**
     * 创建索引
     * 
     * @param indexName
     *            索引名称
     * @return boolean
     * @throws IOException
     */
    public boolean addIndex(String indexName) throws IOException{
        JestResult result = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("indexName:" + indexName);
            }
            org.elasticsearch.common.settings.ImmutableSettings.Builder buildIndexSetting = this.buildIndexSetting();
            io.searchbox.indices.CreateIndex.Builder bIndex = new CreateIndex.Builder(indexName);
            CreateIndex cIndex = bIndex.settings(buildIndexSetting.build().getAsMap()).build();
            result = client.execute(cIndex);
            if (null != result && result.isSucceeded()) {
                return true;
            }
            return false;
        } catch (IOException e) {
            logger.error("创建索引失败", e);
            throw new IOException("创建索引失败", e);
        }
    }

    /**
     * "analysis": { "analyzer": { "pinyin_analyzer": { "filter": [ "standard",
     * "nGram" ], "tokenizer": [ "my_pinyin" ] } }, "tokenizer": { "my_pinyin":
     * { "padding_char": "", "type": "pinyin", "first_letter": "only" } } }
     * 构建索引设置
     * 
     * @return
     * @throws IOException
     */
    public ImmutableSettings.Builder buildIndexSetting() throws IOException {
//        try {
            ImmutableSettings.Builder settingsBuilder = customImmutableSetting.getBuilder();
//            XContentBuilder settings = XContentFactory.jsonBuilder().startObject().startObject("analysis").startObject("analyzer")
//                    .startObject("pinyin_analyzer").startArray("filter").value("standard").value("nGram").endArray().startArray("tokenizer")
//                    .value("my_pinyin").endArray().endObject().endObject().startObject("tokenizer").startObject("my_pinyin").field("type", "pinyin")
//                    .field("first_letter", "only").field("padding_char", " ").endObject().endObject().endObject();
//            settingsBuilder.put(JsonUtils.jsonToObject(settings.string(), Map.class));
//            logger.debug(LogUtils.format("settings", settings.string()));
            return settingsBuilder;
//        } catch (IOException e) {
//            logger.debug("构建索引配置失败", e);
//            throw new IOException("构建索引配置失败");
//        }
    }

    /**
     * 删除索引
     * 
     * @param indexName
     *            索引名称
     * @return boolean
     * @throws IOException
     */
    @Override
    public boolean deleteIndex(String indexName) throws IOException {
        JestResult result = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("indexName:" + indexName);
            }
            DeleteIndex dIndex = new DeleteIndex.Builder(indexName).build();
            result = client.execute(dIndex);
            if (null != result && result.isSucceeded()) {
                return true;
            }
            return false;
        } catch (IOException e) {
            logger.error("删除索引失败", e);
            throw new IOException("删除索引失败", e);
        }
    }
    
    @Override
    public boolean deleteIndexType(String indexName, String indexType) throws IOException {
        JestResult result = null;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("indexName:" + indexName);
            }
            DeleteIndex dIndex = new DeleteIndex.Builder(indexName).type(indexType).build();
            result = client.execute(dIndex);
            if (null != result && result.isSucceeded()) {
                return true;
            }
            return false;
        } catch (IOException e) {
            logger.debug("删除索引类型失败", e);
            throw new IOException("删除索引类型失败", e);
        }
    }

    @Override
    public void documentHandler(List<ESDto> documents, List<String> organkeys, Integer action) throws IOException {
    }

    @Override
    public List<ESDto> documentSearch(String keywords, List<String> organkeys, Integer pageNow, Integer pageSize) throws IOException {
        return Collections.emptyList();
    }
}
