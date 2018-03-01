package com.ausland.weixin.config;

import java.util.UUID;

public final class AppThreadLocal {
	
	private static ThreadLocal<String> transactionId = new InheritableThreadLocal<String>(){
		@Override protected String initialValue() {
            return UUID.randomUUID().toString();
		}
	};
	
	public static String getId(){
		return transactionId.get();
	}
	
	public static void clear() {
		transactionId.remove();
	}
}
