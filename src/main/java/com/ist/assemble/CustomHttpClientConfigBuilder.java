package com.ist.assemble;

import io.searchbox.client.config.HttpClientConfig.Builder;

import javax.annotation.Resource;

/**
 * 扩展httpConfig,设置其连接读取和是否启用多线程属性
 * 
 * @author qianguobing
 */
public class CustomHttpClientConfigBuilder extends Builder {
    @Resource
    private Builder builder;
    private int readTimeout;
    private int connTimeout;
    private boolean multiThreaded;

    public CustomHttpClientConfigBuilder(String serverUri) {
        super(serverUri);
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        builder.readTimeout(readTimeout);
        this.readTimeout = readTimeout;
    }

    public int getConnTimeout() {
        return connTimeout;
    }

    public void setConnTimeout(int connTimeout) {
        builder.connTimeout(connTimeout);
        this.connTimeout = connTimeout;
    }

    public boolean isMultiThreaded() {
        return multiThreaded;
    }

    public void setMultiThreaded(boolean multiThreaded) {
        builder.multiThreaded(multiThreaded);
        this.multiThreaded = multiThreaded;
    }

}
