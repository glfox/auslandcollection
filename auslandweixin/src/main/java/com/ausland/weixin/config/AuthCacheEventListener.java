package com.ausland.weixin.config;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthCacheEventListener implements CacheEventListener<Object, Object> {

private static final Logger logger = LoggerFactory.getLogger(AuthCacheEventListener.class);
	
	@Override
	public void onEvent(CacheEvent<? extends Object, ? extends Object> event) {
		logger.info(event.getType().name() + " in " + "- caching Key: " + event.getKey().hashCode());
	}
}