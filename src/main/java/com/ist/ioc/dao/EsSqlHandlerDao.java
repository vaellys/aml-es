package com.ist.ioc.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.ist.common.es.util.LogUtils;
import com.ist.dto.bmp.ESDto;

public class EsSqlHandlerDao extends JdbcDaoSupport {
    
    public List<ESDto> sqlHandler(String sql, List<String> organkeys){
        StringBuilder stringBuilder = null;
        stringBuilder = new StringBuilder();
        stringBuilder.append("select * from (");
        stringBuilder.append(sql);
        stringBuilder.append(") x ");
        if(organkeys != null && !organkeys.isEmpty()){
            stringBuilder.append(" where x.organkey in('");
            stringBuilder.append(org.apache.commons.lang.StringUtils.join(organkeys, "','"));
            stringBuilder.append("')");
        }
        String sqlStr = stringBuilder.toString();
        if(logger.isDebugEnabled()){
            logger.debug("-------------------sql---------------" + sqlStr);
        }
        RowMapper<ESDto> rowMapper = new BeanPropertyRowMapper<ESDto>(ESDto.class);
        List<ESDto> queryForList = this.getJdbcTemplate().query(sqlStr, rowMapper);
        logger.debug(LogUtils.format("results", queryForList));
        return queryForList;
    }

}
