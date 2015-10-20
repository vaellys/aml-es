package com.ist.ioc.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ist.common.es.util.LogUtils;

public class Bmp_user_organ_authorityDao extends JdbcDaoSupport {
    public static final Log logger = LogFactory.getLog(Bmp_user_organ_authorityDao.class);

    /**
     * <p>
     * 根据用户名查询对应的机构key列表
     * </p>
     * 
     * @param username
     *            用户名
     * @return List<String> 机构key列表
     */
    public List<String> getBmpOrganKeysByUsername(String username) {
        String sql = "select organkey from bmp_user_organ_authority where username=?";
        if (logger.isDebugEnabled()) {
            logger.debug("--------------------sql--------------:" + sql);
        }
        List<String> queryForList = this.getJdbcTemplate().queryForList(sql, new Object[] { username }, String.class);
        logger.debug(LogUtils.format("result", queryForList));
        return queryForList;
    }
    
    
}
