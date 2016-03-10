package net.sanmuyao.core.redis;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sanmuyao.core.util.ValidateUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 加载 redis 配置文件
 * @author 杜庆明
 *
 */
public class RedisContext implements ServletContextListener {

	public static JedisPool jedisPool;
    private static Properties redisPros = new Properties();
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("执行：net.sanmuyao.core.redis.RedisContext.contextDestroyed");
	}

	/**
	 * 初始化RedisContext
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		String rootPath = event.getServletContext().getRealPath("/");
	    String sepa=File.separator;
	    String filePath = rootPath +sepa+ "WEB-INF"+sepa+"config"+sepa+"redis-config.properties";
	    
	    System.out.println("########## LOAD: redis-config.properties ##########");
	    System.out.println(filePath);
	    
	    File file = new File(filePath);
	    FileInputStream fis = null;
	    try {
		    //fis = event.getServletContext().getResourceAsStream(filePath);
	    	fis = new FileInputStream(file);
	    	redisPros.load(fis);
		} catch (Exception e) {
			System.err.println("redis-config.properties PATH: " + filePath);
			e.printStackTrace();
			return;
		}
	    if(redisPros != null) {
			JedisPoolConfig config = new JedisPoolConfig();
		    config.setMaxActive(Integer.valueOf(redisPros.getProperty("redis.pool.maxActive")));  
		    config.setMaxIdle(Integer.valueOf(redisPros.getProperty("redis.pool.maxIdle")));  
		    config.setMaxWait(Long.valueOf(redisPros.getProperty("redis.pool.maxWait")));  
		    config.setTestOnBorrow(Boolean.valueOf(redisPros.getProperty("redis.pool.testOnBorrow")));  
		    config.setTestOnReturn(Boolean.valueOf(redisPros.getProperty("redis.pool.testOnReturn")));
		    String host = redisPros.getProperty("redis.ip");
		    Integer port = Integer.valueOf(redisPros.getProperty("redis.port"));
		    String password = redisPros.getProperty("redis.password");
		    if(ValidateUtil.isNotNull(password)) {
		    	jedisPool = new JedisPool(config, host, port, 10000, password);
		    } else {
		    	jedisPool = new JedisPool(config, host, port); 
		    }
	    } else {
	    	System.err.println("redis-config.properties LOAD FAIL! PATH:" + filePath);
	    }
	}

	public static Jedis getResource() throws Exception {
		try {
			return jedisPool.getResource();
		} catch(Exception e) {
			System.out.println("========== RedisManager ERROR ==========");
			System.out.println("host=" + redisPros.getProperty("redis.ip"));
			System.out.println("port=" + redisPros.getProperty("redis.port"));
		    System.out.println("password=" + redisPros.getProperty("redis.password"));
			e.printStackTrace();
			throw new Exception("取 Jedis 失败");
		}
		
	}

	
}
