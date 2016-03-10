package net.sanmuyao.core.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import net.sanmuyao.core.redis.RedisManager;
import net.sanmuyao.core.util.ComputerUtils;
import net.sanmuyao.core.util.DataMap;


/**
 * session 监听器
 * @author 杜庆明
 * 2015年1月22日
 */
public class SessionCountListener implements HttpSessionListener {

	public static final String redisKey = "SESSION_COUNT_MAP";
	
	/**
	 * 缓存生存时间
	 * 默认12个小时
	 */
	private static final int expire = 60*60*24;
	
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		//System.out.println("########## Session Created: " + event.getSession().getId());
		setRedisCount(1);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		//System.out.println("########## Session Destroyed: " + event.getSession().getId());
		setRedisCount(-1);
	}
	
	/**
	 * 将个数保存到缓存中
	 * @param serverName
	 * @param count
	 */
	private void setRedisCount(long count) {
		String serverName = ComputerUtils.getIP();
		DataMap sessionCountMap = (DataMap)RedisManager.getObject(redisKey);
		if(sessionCountMap == null) {
			sessionCountMap = DataMap.getInstance();
		}
		int session_count = sessionCountMap.getInt(serverName);
		if(session_count < 0) {
			session_count = 0;
		}
		session_count += count;
		sessionCountMap.put(serverName, session_count);
		RedisManager.putObject(redisKey, sessionCountMap, expire);
	}

}
