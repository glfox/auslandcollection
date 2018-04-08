package com.ausland.weixin.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ausland.weixin.config.AuslandApplicationConstants;
import com.ausland.weixin.dao.OrderRepository;
import com.ausland.weixin.dao.ProductRepository;
import com.ausland.weixin.dao.ProductStockRepository;
import com.ausland.weixin.dao.UserRepository;
import com.ausland.weixin.model.db.Order;
import com.ausland.weixin.model.db.Product;
import com.ausland.weixin.model.db.ProductStock;
import com.ausland.weixin.model.db.PurchaseItem;
import com.ausland.weixin.model.db.User;
import com.ausland.weixin.model.reqres.CreateOrderDetailsRes;
import com.ausland.weixin.model.reqres.CreateOrderReq;
import com.ausland.weixin.model.reqres.GlobalListRes;
import com.ausland.weixin.model.reqres.GlobalRes;
import com.ausland.weixin.model.reqres.PurchaseItemReq;
import com.ausland.weixin.model.reqres.QueryOrderDetailsRes;
import com.ausland.weixin.service.OrderService;
import com.ausland.weixin.util.CustomCookie;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ProductStockRepository productStockRepository;
	
	
	@Override
	public GlobalRes deleteOrder(CustomCookie customCookie, String orderId) {
		GlobalRes res = new GlobalRes();
		if(customCookie == null || customCookie.getRole() == null || customCookie.getUserName() == null)
		{
			res.setErrorDetails("没有解析出用户信息");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		Order order = orderRepository.findById(Integer.parseInt(orderId));
		if(order == null)
		{
			res.setErrorDetails("没有在数据库中找到该订单的信息");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		if( AuslandApplicationConstants.ORDER_STATUS_DELETED.equalsIgnoreCase(order.getStatus()))
		{
			res.setErrorDetails("该订单信息显示已经为删除状态了。");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		User user = userRepository.findByUsername(order.getCreatedBy());
		if(user == null)
		{
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
	    	res.setErrorDetails("没有在数据库中找到用户信息,无法删除。");
	    	return res;
		}
		user.setAccountBalance(user.getAccountBalance().add(order.getOrderTotalPrice()));
		order.setStatus(AuslandApplicationConstants.ORDER_STATUS_DELETED);
		orderRepository.save(order);
		userRepository.save(user);
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}
 

	String validateCreateOrderReq(CreateOrderReq req)
	{
		if(req == null || req.getReceiver() == null || StringUtils.isEmpty(req.getReceiver().getAddress()) || 
		   StringUtils.isEmpty(req.getReceiver().getPhoneNumber()) || StringUtils.isEmpty(req.getReceiver().getUserName()))
		{
	        return "该订单收件人信息缺失";
		}
		if(req.getPucharseItemList() == null || req.getPucharseItemList().size() <= 0)
		{
			return "该订单没有商品信息";
		}
		return null;
	}
	
	@Override
	public CreateOrderDetailsRes createOrder(CustomCookie customCookie, CreateOrderReq req) {
		CreateOrderDetailsRes res = new CreateOrderDetailsRes();
		if(customCookie == null || customCookie.getRole() == null || customCookie.getUserName() == null)
		{
			res.setErrorDetails("没有解析出用户信息");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		
		String ret = validateCreateOrderReq(req);
		if(!StringUtils.isEmpty(ret))
		{
			res.setErrorDetails(ret);
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		User user = userRepository.findByUsername(customCookie.getUserName());
		if(user == null || user.getAccountBalance().compareTo(new BigDecimal(0.0)) <= 0)
		{
			res.setErrorDetails("用户不存在或用户余额为零。");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		Order order = new Order();
		order.setComments(req.getComments());
		order.setCreatedBy(customCookie.getUserName());
		order.setCreatedDateTime(new Date());
		order.setReceiverAddress(req.getReceiver().getAddress());
		order.setReceiverName(req.getReceiver().getUserName());
        order.setReceiverPhone(req.getReceiver().getPhoneNumber());
        if(req.getSender() != null)
        {
        	order.setSenderAddress(req.getSender().getAddress());
        	order.setSenderName(req.getSender().getUserName());
        	order.setSenderPhone(req.getSender().getPhoneNumber());
        }
        List<PurchaseItem> purchaseItems = new ArrayList<PurchaseItem>();
        BigDecimal total = new BigDecimal(0.0);
        
		for(PurchaseItemReq itemReq : req.getPucharseItemList())
		{
		    if(itemReq == null || StringUtils.isEmpty(itemReq.getProductId()) || itemReq.getQuantity() < 1 || StringUtils.isEmpty(itemReq.getColor()) || StringUtils.isEmpty(itemReq.getSize()))
		    {
		    	res.setErrorDetails("商品型号，颜色，尺码或者数量没有选择，请重新选择");
		    	res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		    	return res;
		    }
		    
		    Product p = productRepository.findByProductId(itemReq.getProductId());
		    if(p == null || p.getProductUniPrice() == null)
		    {
		    	res.setErrorDetails("商品型号："+itemReq.getProductId()+"或者商品价格不存在数据库中，该商品暂时不能购买，请重新选择商品型号");
		    	res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		    	return res;
		    }
		    
		    ProductStock ps = productStockRepository.findByProductIdAndColor(itemReq.getProductId(), itemReq.getColor());
		    if(ps == null)
		    {
		    	res.setErrorDetails("该商品型号："+itemReq.getProductId()+"商品颜色："+itemReq.getColor()+"不存在数据库中，请重新选择颜色");
		    	res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		    	return res;
		    }
		    if(StringUtils.isEmpty(ps.getSize()) || StringUtils.isEmpty(ps.getStockStatus()))
		    {
		    	res.setErrorDetails("该商品型号："+itemReq.getProductId()+"商品颜色："+itemReq.getColor()+"没有尺码或者库存信息，暂时不能购买，请选择别的商品购买");
		    	res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		    	return res;
		    }
		    String[] sizes = ps.getSize().split(",");
		    String[] stocks = ps.getStockStatus().split(",");
		    boolean found = false;
		    for(int i = 0; i < sizes.length; i ++)
		    {
		    	if(itemReq.getSize().equalsIgnoreCase(sizes[i]))
		    	{
		    		if(stocks.length > i && stocks[i].equalsIgnoreCase(AuslandApplicationConstants.STOCKTATUS_INSTOCK))
		    		{
		    			found = true;
		    			break;
		    		}
		    	}
		    }
		    if(found != true)
		    {
		    	res.setErrorDetails("该商品型号："+itemReq.getProductId()+"商品颜色："+itemReq.getColor()+"商品尺码：" + itemReq.getSize() + "已经断货，请重新选择");
		    	res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
		    	return res;
		    }
			PurchaseItem pItem = new PurchaseItem();
			pItem.setColor(itemReq.getColor());
			pItem.setComments(itemReq.getComments());
			pItem.setCreatedSrc(customCookie.getUserName());
			pItem.setProductId(itemReq.getProductId());
			pItem.setProductProvider(p.getProvider());
			pItem.setProductUnitPrice(p.getProductUniPrice());
			pItem.setQuantity(itemReq.getQuantity());
			pItem.setSize(itemReq.getSize());
			pItem.setStatus(AuslandApplicationConstants.ORDER_STATUS_CREATED);
			pItem.setCustomer_order(order);
			total = total.add(p.getProductUniPrice().multiply(new BigDecimal(itemReq.getQuantity())));
			purchaseItems.add(pItem);
		}
		user = userRepository.findByUsername(customCookie.getUserName());
		if(user.getAccountBalance().compareTo(total) < 0)
		{
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
	    	res.setErrorDetails("创建订单失败，用户余额不足。");
	    	return res;
		}
		user.setAccountBalance(user.getAccountBalance().subtract(total));
		order.setStatus(AuslandApplicationConstants.ORDER_STATUS_CREATED);
	    order.setPurchaseItems(purchaseItems);
	    order.setOrderTotalPrice(total);
	    Order o = orderRepository.saveAndFlush(order);
	    if(o == null)
	    {
	    	res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
	    	res.setErrorDetails("创建订单失败，不能存入数据库错误");
	    	return res;
	    }
	    res.setOrderId("" + o.getId());
	    userRepository.save(user);
	    res.setStatus(AuslandApplicationConstants.STATUS_OK);
	    return res;
	}

	@Override
	public GlobalRes updatePurchaseItemToOrder(CustomCookie customCookie, String orderId, Integer purchaseItemId,
			PurchaseItemReq req) {
		
		return null;
	}

	@Override
	public GlobalRes deletePurchaseItemFromOrder(CustomCookie customCookie, String orderId, Integer purchaseItemId) {
		GlobalRes res = new GlobalRes();
		if(customCookie == null || AuslandApplicationConstants.STANDARD_USER_ROLE.equalsIgnoreCase(customCookie.getRole()))
		{
			res.setErrorDetails("");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		Order order = orderRepository.findById(Integer.parseInt(orderId));
		if(order == null || order.getPurchaseItems() == null || order.getPurchaseItems().size() <= 0)
		{
			res.setErrorDetails("");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
	    
		List<PurchaseItem> list = order.getPurchaseItems();
		PurchaseItem pi = null;
		for(int i = 0; i < list.size(); i ++)
		{
			if(list.get(i) != null && list.get(i).getId() == purchaseItemId)
			{
				pi = list.get(i);
				order.getPurchaseItems().remove(i);
				break;
			}
		}
		if(pi == null)
		{
			res.setErrorDetails("");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		User user = userRepository.findByUsername(order.getCreatedBy());
		if(user == null)
		{
			res.setErrorDetails("");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		orderRepository.save(order);
		user.setAccountBalance(user.getAccountBalance().add(order.getOrderTotalPrice()));
		userRepository.save(user);
		res.setStatus(AuslandApplicationConstants.STATUS_OK);
		return res;
	}

	@Override
	public GlobalRes addPurchaseItemToOrder(CustomCookie customCookie, String orderId, PurchaseItemReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GlobalListRes uploadOrderFromExcel(CustomCookie customCookie, MultipartFile excelFile) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public QueryOrderDetailsRes queryOrderBy(CustomCookie customCookie, String orderStatus, String fromDate,
			String toDate) {
		QueryOrderDetailsRes res = new QueryOrderDetailsRes();
		if(customCookie == null || customCookie.getRole() == null || customCookie.getUserName() == null)
		{
			res.setErrorDetails("没有解析出用户信息");
			res.setStatus(AuslandApplicationConstants.STATUS_FAILED);
			return res;
		}
		 
		if(AuslandApplicationConstants.STANDARD_USER_ROLE.equalsIgnoreCase(customCookie.getRole()))
		{
			
		}
		else
		{
			
		}
		
		return res;
	}

}
