package com.zk.nt.netty;

import java.util.Calendar;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyServerListener implements ServletContextListener{
	
	private static final Logger logger = LoggerFactory.getLogger(NettyServerListener.class);
    private NettyServer nettyServer;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	nettyServer =  new NettyServer(8091);
    	try {
        	nettyServer.start();
        } catch (Exception e) {
        	logger.error("netty server abnormal startup {netty服务启动异常}", e);
        }
    }
    
    /**
     * @Author      销毁  
     * @Date        2020年8月27日 上午10:57:53   
     * @param sce   
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("destroy() of netty"+ Calendar.getInstance().getTime()+"  flag:"+(null==nettyServer));
        nettyServer.destroy();
    }
}
