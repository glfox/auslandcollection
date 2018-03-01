package com.ausland.weixin.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ClientHttpLoggingInterceptor implements ClientHttpRequestInterceptor{

	private static final Logger logger=LoggerFactory.getLogger(ClientHttpLoggingInterceptor.class);

	private BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory;
	public ClientHttpLoggingInterceptor(){}
	public ClientHttpLoggingInterceptor(BufferingClientHttpRequestFactory requestFactory) {
		this.bufferingClientHttpRequestFactory = requestFactory;
	}

	@Override
	   public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
	         throws IOException {
		  // Map<String, Object> finalMap = new LinkedHashMap<String, Object>();
		   StringBuffer finalBuffer = new StringBuffer();
		   finalBuffer.append(System.lineSeparator());
		   try
			 {
			     finalBuffer.append("request URI: " + request.getMethod() + " " + request.getURI());
				 HashMap<String, String> headersMap = new HashMap<String, String>();
				 HttpHeaders httpHeaders = request.getHeaders();
				 Set<String> headerNameSet = httpHeaders.keySet();
				 for(String headerName : headerNameSet)
				 {
					 if(HttpHeaders.ACCEPT_CHARSET.equalsIgnoreCase(headerName))
					 {
						 continue;
					 }
					 List list = httpHeaders.get(headerName);
					 StringBuffer headerValue = new StringBuffer();
					 if(list != null && list.size() > 0)
					 {
						 for(int i = 0; i < list.size(); i ++)
						 {
							 if(i == 0)
							 {
								 headerValue.append(list.get(i));
							 }
							 else
							 {
								 headerValue.append(";"+list.get(i));
							 }
						 }
					 }
					 headersMap.put(headerName, headerValue.toString());
				 }
				 
				 String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(headersMap);
				 finalBuffer.append(System.lineSeparator()).append("request headers: " + json);
				 finalBuffer.append(System.lineSeparator()).append("request body: "+ getRequestBody(body));	 
			 }
		     catch(Exception e)
			 {
		    	 logger.error("traceRequest got exception:"+e.getMessage());
			 }
		   
	      ClientHttpResponse clientHttpResponse = execution.execute(request, body);
	      BufferingClientHttpResponseWrapper bufferingClientHttpResponseWrapper = new BufferingClientHttpResponseWrapper(clientHttpResponse);

	      try
		  {
	    	  finalBuffer.append(System.lineSeparator()).append("response status code: " + clientHttpResponse.getRawStatusCode());
	    	  finalBuffer.append(System.lineSeparator()).append("response body: " + getBodyString(bufferingClientHttpResponseWrapper));
		  }
		  catch(Exception e)
		  {
	    	  logger.error("traceResponse got exception:"+e.getMessage());
		  }
	      logger.debug(finalBuffer.toString());
	      return clientHttpResponse;
	   } 

	   private String getRequestBody(byte[] body) throws UnsupportedEncodingException {
	      if (body != null && body.length > 0) {
	         return (new String(body, "UTF-8"));
	      } else {
	         return null;
	      }
	   }

	   private String getBodyString(BufferingClientHttpResponseWrapper response) {
	      try {
	         if (response != null && response.getBody() != null) {// &&
	                                                              // isReadableResponse(response))
	                                                              // {
	            StringBuilder inputStringBuilder = new StringBuilder();
	            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
	            String line = bufferedReader.readLine();
	            while (line != null) {
	               inputStringBuilder.append(line);
	               inputStringBuilder.append(System.lineSeparator());
	               line = bufferedReader.readLine();
	            }
	            return inputStringBuilder.toString();
	         } else {
	            return null;
	         }
	      } catch (IOException e) {
	         logger.error(e.getMessage(), e);
	         return null;
	      }
	   }
}