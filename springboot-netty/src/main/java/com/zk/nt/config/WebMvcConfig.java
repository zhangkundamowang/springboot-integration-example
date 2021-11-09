package com.zk.nt.config;

import com.zk.nt.netty.NettyServerListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig  implements WebMvcConfigurer{
    /**
     * @Description 配置用于启动netty服务的监听器
     */
    @Bean(name = "nettyServerListener")
    public ServletListenerRegistrationBean<NettyServerListener> nettyListenerRegist() {
        ServletListenerRegistrationBean<NettyServerListener> srb = new ServletListenerRegistrationBean<NettyServerListener>();
        srb.setListener(new NettyServerListener());
        return srb;
    }
}
