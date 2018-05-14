package com.ausland.weixin.util;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
 
@Service
public class DifouUtil {
 
	private static final Logger logger = LoggerFactory.getLogger(DifouUtil.class);
 
	@Value("${difou.accesstoken}")
	private String accessToken;
	
	@Value("${difou.appkey}")
	private String appKey;
	
	@Value("${difou.appsecret}")
	private String appSecret;
	
	private final static String GET_STOCK_METHOD = "wdgj.stock.list.get";
	private final static String GET_PRODUCT_METHOD = "wdgj.goods.list.get";
	public static void main(String[] args) {
		 
	}
	
	public String getStockReqStr(String productId){
		if(StringUtils.isEmpty(productId)) return "";
		Map<String, String> map = getStockMap(productId);
		return ChangeToPostValue(map);
	}
	
	public String getProductReqStr(String productId){
		if(StringUtils.isEmpty(productId)) return "";
		Map<String, String> map = getProductMap(productId);
		return ChangeToPostValue(map);
	}
	
	public String getAllProductIdListReqStr(String pageNo){
		if(StringUtils.isEmpty(pageNo)) return "";
		Map<String, String> map = getAllProductIdListMap(pageNo);
		return ChangeToPostValue(map);
	}
	
	//收集签名需要的参数
    private Map<String, String> getStockMap(String productId) {
        String timestamp = ""+ System.currentTimeMillis() / 1000;
        Map<String, String> map = new HashMap<String, String>();
        map.put("accesstoken", accessToken);
        map.put("appkey", appKey);
        map.put("timestamp", timestamp);
        map.put("versions", "1.1");
        map.put("format","json");
        map.put("pageno","1");
        map.put("method", GET_STOCK_METHOD);
        map.put("pagesize","100");
        map.put("searchcode", productId);
        String sign = getSign(map);
        map.put("sign", sign);
        return map;
    }
    
    private Map<String, String> getAllProductIdListMap(String pageNo) {
        String timestamp = ""+ System.currentTimeMillis() / 1000;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("accesstoken", accessToken);
        map.put("appkey", appKey);
        map.put("timestamp", timestamp);
        map.put("versions", "1.1");
        map.put("format","json");
        map.put("pageno",pageNo);
        map.put("method", GET_PRODUCT_METHOD);
        map.put("pagesize","100");
        String sign = getSign(map);
        map.put("sign", sign);
        return map;
    }
    
    private Map<String, String> getProductMap(String productId) {
        String timestamp = ""+ System.currentTimeMillis() / 1000;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("accesstoken", accessToken);
        map.put("appkey", appKey);
        map.put("timestamp", timestamp);
        map.put("versions", "1.1");
        map.put("format","json");
        map.put("pageno","1");
        map.put("method", GET_PRODUCT_METHOD);
        map.put("pagesize","100");
        map.put("searchno", productId);
        String sign = getSign(map);
        map.put("sign", sign);
        return map;
    }
    
    //生成签名
    private String getSign(Map<String,String> paraMap) {
        Iterator iter = paraMap.entrySet().iterator();
        List<String> list = new ArrayList<String>();
       // String AppSecret = context.getString(R.string.dfAppSecret);
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            String val = entry.getValue().toString();
            list.add(val);
        }
        //ASCII码排序
        Collections.sort(list, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        StringBuilder builder = new StringBuilder();
        //参数拼接
        builder.append(appSecret);
        for (String value : list) {
            builder.append(value);
        }
        builder.append(appSecret);
        //md5加密后得到最终的签名
        return getMd5Value(builder.toString());
  
    }
    
    private String ChangeToPostValue(Map<String, String> map)
    {
        if(map == null || map.size()<= 0) return "";
    	StringBuilder sb = new StringBuilder();
        Set<String> keySet = map.keySet();
        Iterator iterator = keySet.iterator();
        while(iterator.hasNext())
        {
            String key = (String)iterator.next();
        	sb.append(key + "=" + URLEncoder.encode(map.get(key)) + "&"); 
        }
        String s = sb.toString();
        if(s.endsWith("&")){
        	s = s.substring(0, s.length() - 1);
        }
        logger.debug("ChangeToPostValue returned with: ", s);
        return s;
    }
        
    //自己处理一下md5加密，原生的md5加密方法有问题
    private String getMd5Value(String sSecret) {
    	MessageDigest bmd5 = null;
    	 try {
    		    bmd5 = MessageDigest.getInstance("MD5");
				bmd5.update(sSecret.getBytes("UTF-8"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
    }
}
