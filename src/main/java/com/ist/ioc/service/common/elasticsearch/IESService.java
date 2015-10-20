package com.ist.ioc.service.common.elasticsearch;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ist.dto.bmp.ESDto;

/**
 * 搜索服务提供者
 * 
 * @author qianguobing
 * @date 2015-07-17
 */
public interface IESService {
    
    /**
     * <p>
     * 批量添加索引文档
     * <p>
     * <p>
     * 这里使用bulk api 它会极大提高索引文档的速度
     * </p>
     * 
     * @param indexParams
     *            索引参数Map
     *            <p>
     *            Map中String分别key,value对应于分行，与支行列表 对应于es中的索引名称和索引类型
     *            </p>
     * @param documents
     *            要索引的文档列表
     * @param action
     *            处理文档动作 1:创建 2:删除
     * @throws IOException 
     */
    public void documentHandler(Map<String, List<String>> mapParams, List<ESDto> documents, Integer action) throws IOException;

    /**
     * <p>
     * 根据机构Key列表查询对应分行与支行
     * <p>
     * <p>
     * 然后进行文档索引
     * </p>
     * 
     * @param documents
     *            索引文档列表
     * @param organkeys
     *            机构key列表
     * @param action
     *            处理文档动作 1:创建 2:删除
     * @throws IOException
     *             
     */
    public void documentHandler(List<ESDto> documents, List<String> organkeys, Integer action) throws IOException;
    
    /**
     * 处理自定义sql情况，文档中需要指定机构key字段，表示其所属的机构
     * @param documents
     *              索引文档列表
     * @param action
     *              处理文档动作 1:创建 2:删除
     * @throws IOException
     */
    public void sqlHandler(ESDto esDto, List<String> organkeys, Integer action) throws IOException;
    
    /**
     * 根据关键字带分页的搜索文档
     * 
     * @param query
     *            搜索关键字
     * @param indexNames
     *            索引名称列表，可以搜索多个索引库
     * @param indexTypes
     *            索引类型，可以搜索多个索引类型
     * @param pageNow
     *            当前页no
     * @param pageSize
     *            每页显示数量
     * @return List<ESDto> eSDto列表
     * @throws IOException 
     */
    public List<ESDto> documentSearch(String query, List<String> indexNames, List<String> indexTypes, Integer pageNow, Integer pageSize) throws IOException;

    /**
     * <p>
     * 根据查询关键字带分页的搜索文档
     * </p>
     * <p>
     * 机构key表去查询对应的分行与支行
     * </p>
     * 
     * @param keywords
     *            搜索关键词
     * @param organkeys
     *            机构码列表
     * @param pageNow
     *            当前页no
     * @param pageSize
     *            每页显示数量
     * @return List<ESDto>
     * @throws IOException
     */
    public List<ESDto> documentSearch(String keywords, List<String> organkeys, Integer pageNow, Integer pageSize) throws IOException;

    /**
     * 创建索引
     * 
     * @param indexName
     *            索引名称
     * @return boolean
     * @throws Exception
     */
    public boolean addIndex(String indexName) throws IOException;
    
    /**
     * 创建索引类型
     * 
     * @param indexName
     *            索引名称
     * @param indexName
     *            索引类型
     * @return boolean
     * @throws Exception
     */
    public boolean deleteIndexType(String indexName, String indexType) throws IOException;

    /**
     * 删除索引
     * 
     * @param indexName
     *            索引名称
     * @return boolean
     * @throws Exception
     */
    public boolean deleteIndex(String indexName) throws IOException;

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
     */
    public boolean deleteDoc(String indexName, String indexType, String docId) throws IOException;

    /**
     * 创建索引并对文档进行索引
     * @param indexName
     * @param indexType
     * @param documents
     * @param action
     * @return
     * @throws IOException
     */
    public int documentHandler(String indexName, String indexType, List<Map<String, Object>> documents, Integer action) throws IOException;
    
    /**
     * 创建索引并对文档进行索引
     * @param tableName 索引的类型
     * @param json 
     * @param indexColName 索引的字段名称
     * @return 1成功，0失败[异常是失败]
     * @throws IOException
     */
    public int updateIndexEs(String tableName, String json, String indexColName) throws IOException;
    
    /**
     * 搜索指定字段
     * @param tableName
     * @param json
     * @return
     * @throws IOException
     */
    public String searchEs(String tableName, String json) throws IOException;
    
    /**
     * 删除记录
     * @param tableName
     * @param json
     * @return
     * @throws IOException
     */
    public int deleteIndexEs(String tableName, String json) throws IOException;
}
