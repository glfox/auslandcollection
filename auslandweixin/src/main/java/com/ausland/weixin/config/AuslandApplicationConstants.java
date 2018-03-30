package com.ausland.weixin.config;

public class AuslandApplicationConstants {

	public static final String COOKIE_NAME= "AuslandCookieSessionId";
	public static final String ACTIVE_USER_STATUS = "活跃";
	public static final String LOCKED_USER_STATUS = "被锁";
	public static final String PENDING_USER_STATUS = "僵尸";
	public static final String STANDARD_USER_ROLE = "standard";
	public static final String ADMIN_USER_ROLE = "admin";
	public static final String SUPER_ADMIN_USER_ROLE = "superadmin";
	public static Integer COOKIE_EXPIRATION_INSECONDS = 1440;
	public static final String STATUS_OK = "ok";
	public static final String COOKIE_ENCRYPTION_KEY = "ausland";
	public static final String STATUS_FAILED = "failed";
	public static final String STATUS_PARTIAL = "partial";
	public static final String WEIXIN_MSG_TYPE_TEXT="text";
	public static final Integer WEIXIN_MSG_TYPE_TEXT_MAXLENGTH=2046;
	public static final String WEIXIN_MSG_TYPE_EVENT="event";
	public static final String WELCOME_SUBSCRIBE="欢迎关注AUSLAND微信公众号";
	public static final String ZHONGHUAN_COURIER_SEARCH_PROMPT="请输入手机号查询最近三个月的单号信息，或者输入重庆中环的单号查询具体物流信息";
	public static final String ZHONGHUAN_COURIER_SEARCH_PROMPT_NOORDERINFO="没有找到最近三个月内该手机号对应的重庆中环单号信息 ";
	public static final String ZHONGHUAN_COURIER_SEARCH_PROMPT_NOCOURIERINFO="没有找到重庆中环对应单号的物流信息";
	public static final String ZHONGHUAN_COURIER_SEARCH_PROMPT_SERVERERROR="服务器异常请稍后再试";
	public static final Integer DB_BATCH_SIZE=20;
	public static final String STOCKTATUS_INSTOCK = "Y";
	public static final String STOCKTATUS_NA = "N/A";
	public static final String STOCKTATUS_OUTOFSTOCK = "N";
	public static final String DATETIME_STRING_FORMAT = "yyyy-MM-dd HHmmss";
	public static final String DATE_STRING_FORMAT = "yyyy-MM-dd";
	public static final String IMAGE_HEADER = "data:image/jpeg:base64,";
}
