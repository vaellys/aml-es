package com.ist.dto.bmp;

import io.searchbox.annotations.JestId;

import java.io.Serializable;

/**
 * elasticsearch搜索和索引实体
 * 
 * @author qianguobing
 */
public class ESDto implements Serializable {
    /**
     * 序列化Id
     */
    private static final long serialVersionUID = 7712247850042953111L;
    /**
     * 文档id 用注解指定es索引id
     */
    @JestId
    private String id;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档摘要
     */
    private String description;
    /**
     * 文档路径
     */
    private String path;
    /**
     * 图片地址（保留字段）
     */
    private String imgUrl;
    /**
     * 生成时间
     */
    private String createTime;
    /**
     * 机构key
     */
    private String organkey;
    /**
     * 文档名称
     */
    private String name;
    
    private String sql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    public String getOrgankey() {
        return organkey;
    }

    public void setOrgankey(String organkey) {
        this.organkey = organkey;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((imgUrl == null) ? 0 : imgUrl.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((organkey == null) ? 0 : organkey.hashCode());
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ESDto other = (ESDto) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (imgUrl == null) {
            if (other.imgUrl != null)
                return false;
        } else if (!imgUrl.equals(other.imgUrl))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (organkey == null) {
            if (other.organkey != null)
                return false;
        } else if (!organkey.equals(other.organkey))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        return true;
    }
}
