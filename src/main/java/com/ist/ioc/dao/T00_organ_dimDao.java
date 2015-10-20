package com.ist.ioc.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ist.common.es.util.LogUtils;

public class T00_organ_dimDao extends JdbcDaoSupport implements Serializable{
	private static final Log logger = LogFactory.getLog(T00_organ_dimDao.class);
	/**
	 * 序列化Id
	 */
	private static final long serialVersionUID = 6278720918495578580L;

	/**
	 * 根据机构key查询机构层次
	 * @param T00_organ_dimDTO
	 * @return
	 */
	public List<Map<String, Object>> getOrganDimByOrgankeys(List<String> organkeys){
		String sql = "select uporgankey AS level_code_2,organkey as level_code_3, organname, organkey from t00_organ where source='SJF' and organkey in ('" + org.apache.commons.lang.StringUtils.join(organkeys, "','") + "')";
//	    String sql = "select level_code_2, level_code_3, organname, organkey from  t00_organ_dim where organkey in('" + org.apache.commons.lang.StringUtils.join(organkeys, "','") + "')";
		if(logger.isDebugEnabled()){
			logger.debug("-------------------sql---------------" + sql);
		}
		List<Map<String, Object>> queryForList = this.getJdbcTemplate().queryForList(sql);
		logger.debug(LogUtils.format("results", queryForList));
		return queryForList;
	}
}
