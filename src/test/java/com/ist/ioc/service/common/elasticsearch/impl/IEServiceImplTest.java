package com.ist.ioc.service.common.elasticsearch.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static com.ist.ioc.service.common.Constants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ist.ioc.service.common.elasticsearch.IESService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/spring/applicationContext-es.xml"})
public class IEServiceImplTest extends AbstractJUnit4SpringContextTests {

	@Resource
	private IESService iesService;

	private Log logger = LogFactory.getLog(this.getClass());

	@Before
	public void setUp() throws Exception {
		// iess = new IESServiceImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdateIndexEs() {
		try {
		    String json = "{\"PAGE_ID\":1,\"PAGE_SIZE\":15,\"PAGE_TOTAL\":67,\"TOTAL_SIZE\":1000,\"RESULT\":[{\"audit_dt\":\"2015-01-01\",\"check_dt\":\"2015-01-01\",\"create_dt\":\"1970-01-01 08:00:00\",\"curr_post\":\"P2005\",\"datagranularity\":\"4\",\"dealmax_dt\":\"1970-01-01 08:00:00\",\"emendationlevel\":\"1003\",\"export_status\":\"0\",\"fristappralevel\":\"1002\",\"granularity\":\"2\",\"host_cust_id\":\"000000025360599\",\"lastcheckflag\":\"0\",\"lastlevelkey\":\"1023\",\"lastscore\":\"2.88\",\"levelkey\":\"1001\",\"modify_cd\":\"0\",\"nextdealmax_dt\":\"1970-01-01 08:00:00\",\"nextstatistic_dt\":\"1970-01-01 08:00:00\",\"objorgankey\":\"999009\",\"organkey\":\"999008\",\"party_chn_name\":\"中华人发人婚美好\",\"party_class_cd\":\"I\",\"party_id\":\"000000025360599\",\"rcheck_dt\":\"1970-01-01 08:00:00\",\"reportdt\":\"1970-01-01 08:00:00\",\"resulekey\":\"00000002536059920121026203513\",\"return_dt\":\"1970-01-01 08:00:00\",\"score\":\"4.00\",\"statistic_dt\":\"2015-01-01\",\"statisticdate\":\"1970-01-01 08:00:00\",\"status_cd\":\"1\",\"tempcategory\":\"2\",\"templatekey\":\"PKT61\",\"temptype\":\"1\",\"hbase_rowkey\":\"00000002536059920121026203513!!!!!!!!!!!!!!!!!!!!!\"},{\"audit_dt\":\"1970-01-01 08:00:00\",\"check_dt\":\"2015-04-01\",\"create_dt\":\"1970-01-01 08:00:00\",\"curr_post\":\"P2004\",\"datagranularity\":\"4\",\"dealmax_dt\":\"1970-01-01 08:00:00\",\"emendationlevel\":\"1003\",\"export_status\":\"0\",\"fristappralevel\":\"1002\",\"granularity\":\"2\",\"host_cust_id\":\"000000025360599\",\"lastcheckflag\":\"0\",\"lastlevelkey\":\"1023\",\"lastscore\":\"2.88\",\"levelkey\":\"1001\",\"modify_cd\":\"0\",\"nextdealmax_dt\":\"1970-01-01 08:00:00\",\"nextstatistic_dt\":\"1970-01-01 08:00:00\",\"objorgankey\":\"999009\",\"organkey\":\"999009\",\"party_chn_name\":\"aaaa\",\"party_class_cd\":\"I\",\"party_id\":\"000000025360599\",\"rcheck_dt\":\"1970-01-01 08:00:00\",\"reportdt\":\"1970-01-01 08:00:00\",\"resulekey\":\"00000002536059920191026203513\",\"return_dt\":\"1970-01-01 08:00:00\",\"score\":\"4.00\",\"statistic_dt\":\"2015-05-01\",\"statisticdate\":\"1970-01-01 08:00:00\",\"status_cd\":\"1\",\"tempcategory\":\"2\",\"templatekey\":\"PKT61\",\"temptype\":\"1\",\"hbase_rowkey\":\"00000002536059920191026203513!!!!!!!!!!!!!!!!!!!!!\"}]}";
		    String indexColName = "organkey,PARTY_ID,RESULEKEY,modify_cd,curr_post,check_dt,STATISTIC_DT,party_chn_name";
		    int updateIndexEs = iesService.updateIndexEs("table1", json, indexColName);
		    assertEquals(1, updateIndexEs);
		} catch (Exception e) {
			e.printStackTrace();
			fail("----------构建索引失败----------");
		}
	}
	
	@Test
    public void testDocumentHandler() {
        try {
            String json = "{\"PAGE_ID\":1,\"PAGE_SIZE\":15,\"PAGE_TOTAL\":67,\"TOTAL_SIZE\":1000,\"RESULT\":[{\"audit_dt\":\"2015-01-01\",\"check_dt\":\"2015-01-01\",\"create_dt\":\"1970-01-01 08:00:00\",\"curr_post\":\"P2005\",\"datagranularity\":\"4\",\"dealmax_dt\":\"1970-01-01 08:00:00\",\"emendationlevel\":\"1003\",\"export_status\":\"0\",\"fristappralevel\":\"1002\",\"granularity\":\"2\",\"host_cust_id\":\"000000025360599\",\"lastcheckflag\":\"0\",\"lastlevelkey\":\"1023\",\"lastscore\":\"2.88\",\"levelkey\":\"1001\",\"modify_cd\":\"0\",\"nextdealmax_dt\":\"1970-01-01 08:00:00\",\"nextstatistic_dt\":\"1970-01-01 08:00:00\",\"objorgankey\":\"999009\",\"organkey\":\"999008\",\"party_chn_name\":\"中华人发人婚美好\",\"party_class_cd\":\"I\",\"party_id\":\"000000025360599\",\"rcheck_dt\":\"1970-01-01 08:00:00\",\"reportdt\":\"1970-01-01 08:00:00\",\"resulekey\":\"00000002536059920121026203513\",\"return_dt\":\"1970-01-01 08:00:00\",\"score\":\"4.00\",\"statistic_dt\":\"2015-01-01\",\"statisticdate\":\"1970-01-01 08:00:00\",\"status_cd\":\"1\",\"tempcategory\":\"2\",\"templatekey\":\"PKT61\",\"temptype\":\"1\",\"hbase_rowkey\":\"00000002536059920121026203513!!!!!!!!!!!!!!!!!!!!!\"},{\"audit_dt\":\"1970-01-01 08:00:00\",\"check_dt\":\"2015-04-01\",\"create_dt\":\"1970-01-01 08:00:00\",\"curr_post\":\"P2004\",\"datagranularity\":\"4\",\"dealmax_dt\":\"1970-01-01 08:00:00\",\"emendationlevel\":\"1003\",\"export_status\":\"0\",\"fristappralevel\":\"1002\",\"granularity\":\"2\",\"host_cust_id\":\"000000025360599\",\"lastcheckflag\":\"0\",\"lastlevelkey\":\"1023\",\"lastscore\":\"2.88\",\"levelkey\":\"1001\",\"modify_cd\":\"0\",\"nextdealmax_dt\":\"1970-01-01 08:00:00\",\"nextstatistic_dt\":\"1970-01-01 08:00:00\",\"objorgankey\":\"999009\",\"organkey\":\"999009\",\"party_chn_name\":\"aaaa\",\"party_class_cd\":\"I\",\"party_id\":\"000000025360599\",\"rcheck_dt\":\"1970-01-01 08:00:00\",\"reportdt\":\"1970-01-01 08:00:00\",\"resulekey\":\"00000002536059920191026203513\",\"return_dt\":\"1970-01-01 08:00:00\",\"score\":\"4.00\",\"statistic_dt\":\"2015-05-01\",\"statisticdate\":\"1970-01-01 08:00:00\",\"status_cd\":\"1\",\"tempcategory\":\"2\",\"templatekey\":\"PKT61\",\"temptype\":\"1\",\"hbase_rowkey\":\"00000002536059920191026203513!!!!!!!!!!!!!!!!!!!!!\"}]}";
            String indexColName = "organkey,PARTY_ID,RESULEKEY,modify_cd,curr_post,check_dt,STATISTIC_DT,party_chn_name";
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", "4");
            map.put("description", "a");
            list.add(map);
            int updateIndexEs = iesService.documentHandler("9905", "509", list, ES_UPDATE_ACTION);
            assertEquals(1, updateIndexEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------构建索引失败----------");
        }
    }
	
	@Test
    public void testUpdateIndexEs2() {
        try {
            String json = "{\"PAGE_ID\":1,\"PAGE_SIZE\":15,\"PAGE_TOTAL\":67,\"TOTAL_SIZE\":800,\"RESULT\":[{\"create_dt\":\"2015-01-17\",\"party_id\":\"000000025360599\",\"resultkey\":\"中华人发人婚美好\",\"hbase_rowkey\":\"00000002536059920121026203513!!!!!!!!!!!!!!!!!!!!!\"},{\"create_dt\":\"2015-02-17\",\"party_id\":\"1970-01-0108:00:00\",\"resulekey\":\"abcd\",\"curr_post\":\"P2004\",\"hbase_rowkey\":\"00000002536059920191026203513!!!!!!!!!!!!!!!!!!!!!\"},{\"create_dt\":\"2015-09-17\",\"party_id\":\"000000025360599\",\"resulekey\":\"xxccccddd\",\"hbase_rowkey\":\"00000002536059920121026203503!!!!!!!!!!!!!!!!!!!!!\"}]}";
            String indexColName = "create_dt,party_id,resultkey";
            int updateIndexEs = iesService.updateIndexEs("table1", json, indexColName);
            assertEquals(1, updateIndexEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------构建索引失败----------");
        }
    }
	
	
	@Test
    public void testUpdateIndexEs3() {
        try {
            String json = "{\"PAGE_ID\":1,\"PAGE_SIZE\":15,\"PAGE_TOTAL\":67,\"TOTAL_SIZE\":800,\"RESULT\":[{\"CREATE_DT\":\"100\",\"PARTY_ID\":\"000000025360599\",\"RESULEKEY\":\"1970-01-01 08:00:00\",\"hbase_rowkey\":\"00000002536059920121026203513!!!!!!!!!!!!!!!!!!!!!\"},{\"CREATE_DT\":\"1000\",\"PARTY_ID\":\"1970-01-0108:00:00\",\"RESULEKEY\":\"1970-01-01 8:00:00\",\"curr_post\":\"P2004\",\"hbase_rowkey\":\"00000002536059920191026203513!!!!!!!!!!!!!!!!!!!!!\"},{\"CREATE_DT\":\"10000\",\"PARTY_ID\":\"000000025360599\",\"RESULEKEY\":\"1970-01-0108:00:00\",\"hbase_rowkey\":\"00000002536059920121026203503!!!!!!!!!!!!!!!!!!!!!\"}]}";
            String indexColName = "CREATE_DT,PARTY_ID,RESULEKEY";
            int updateIndexEs = iesService.updateIndexEs("table1", json, indexColName);
            assertEquals(1, updateIndexEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------构建索引失败----------");
        }
    }
	
	@Test
    public void testDeleteIndexEs() {
        try {
            String json = "{\"PAGE_ID\":1,\"PAGE_SIZE\":15,\"PAGE_TOTAL\":67,\"TOTAL_SIZE\":1000,\"RESULT\":[{\"ORGANSTR\":\"'10','101002','1040','999009'\",\"PARTY_ID\":\"1970-01-0108:00:00\",\"RESULEKEY\":\"1970-01-0108:00:00\",\"curr_post\":\"P2004\",\"hbase_rowkey\":\"00000002536059920191026203513!!!!!!!!!!!!!!!!!!!!!\"}]}";
            int updateIndexEs = iesService.deleteIndexEs("table1", json);
            assertEquals(1, updateIndexEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------构建索引失败----------");
        }
    }
	
	@Test
    public void testDeleteIndexType() {
        try {
            boolean deleteIndexType = iesService.deleteIndexType("index_", null);
            assertEquals(true, deleteIndexType);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------构建索引失败----------");
        }
    }
	
	@Test
    public void testSearchEs1() {
        try {
            String json1 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"ORGANSTR\":\"'10','101002','1040','999009'\",\"PARTY_ID\":\"000000025360599\",\"RESULEKEY\":\"000000025360599\"}";
            iesService.searchEs("table1", json1);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
	
	@Test
    public void testSearchEs2() {
        try {
            String json2 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\"}";
            iesService.searchEs("table1", json2);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
	@Test
    public void testSearchEs3() {
        try {
            String json1 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"PARTY_ID\":\"000000025360599\",\"RESULEKEY\":\"000000025360599\"}";
            iesService.searchEs("table1", json1);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
	@Test
    public void testSearchEs4() {
        try {
            String json1 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"ORGANSTR\":\"'10','101002','1040','999009'\"}";
            String searchEs = iesService.searchEs("table1", json1);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
	@Test
    public void testSearchEs5() {
        try {
            String json1 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"0\",\"PAGE_TOTALCOUNT\":\"\",\"ORGANSTR\":\"'10','101002','1040','999009'\",\"audit_dt\":\"1970\",\"RESULEKEY\":\"000000025360599\"}";
            iesService.searchEs("table1", json1);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
	@Test
    public void testSearchEs6() {
        try {
            String json1 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"RESULEKEY\":\"NOTEQUAL[2015-12-12]NOTEQUAL\"}";
            String searchEs = iesService.searchEs("table1", json1);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
	@Test
    public void testSearchEs7() {
        try {
            String json1 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"CREATE_DT1\":\"2015-01-01\",\"CREATE_DT2\":\"2015-02-01\"}";
            String searchEs = iesService.searchEs("table1", json1);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
	@Test
    public void testSearchEs8() {
        try {
            String json1 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"RESULEKEY\":\"LIKECOND[x]LIKECOND\"}";
            String searchEs = iesService.searchEs("table1", json1);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
	
	/**
	 * 测试in
	 */
	@Test
    public void testSearchEsIn() {
        try {
            String json = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"ORGANKEY\":\"'10','101002','1040','999009'\"}";
            String searchEs = iesService.searchEs("table1", json);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
	/**
     * 测试不等于
     */
    @Test
    public void testSearchEsNotEquals() {
        try {
            String json = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"party_chn_name\":\"NOTEQUAL[AAAA]NOTEQUAL\"}";
            String searchEs = iesService.searchEs("table1", json);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
    
    /**
     * 测试like
     */
    @Test
    public void testSearchEsLike() {
        try {
            String json = "{\"PAGE_SIZE\":\"100\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"resultkey\":\"LIKECOND[发人婚]LIKECOND\"}";
            String searchEs = iesService.searchEs("table1", json);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
    /**
     * 测试范围
     */
    @Test
    public void testSearchEsRange() {
        try {
            String json1 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"STATISTIC_DT1\":\"2015-01-01\",\"sTATISTIC_DT2\":\"2015-05-01\"}";
            String searchEs = iesService.searchEs("table1", json1);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
    /**
     * 测试等于
     */
    @Test
    public void testSearchEsEquals() {
        try {
            String json = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"\",\"curr_post\":\"P2004\"}";
            String searchEs = iesService.searchEs("table1", json);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
    
    
    /**
     * 测试条件组合
     */
    @Test
    public void testSearchEsCondJoin() {
        try {
            String json1 = "{\"PAGE_SIZE\":\"15\",\"PAGE_ID\":\"4\",\"PAGE_TOTALCOUNT\":\"\",\"STATISTIC_DT1\":\"2015-01-01\",\"sTATISTIC_DT2\":\"2015-05-01\",\"curr_post\":\"P2004\",\"party_chn_name\":\"aaaa\"}";
            String searchEs = iesService.searchEs("table1", json1);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
    
    /**
     * 测试in
     */
    @Test
    public void testSearchEsIn2() {
        try {
            String json = "{\"PAGE_SIZE\":\"5\",\"PAGE_ID\":\"1\",\"PAGE_TOTALCOUNT\":\"-1\",\"ORGANKEY\":\"'10','101002','1040','999009'\"}";
            String searchEs = iesService.searchEs("t37_party_result_inx2", json);
            logger.debug(searchEs);
            assertNotNull(searchEs);
        } catch (Exception e) {
            e.printStackTrace();
            fail("----------搜索失败----------");
        }
    }
	
}
