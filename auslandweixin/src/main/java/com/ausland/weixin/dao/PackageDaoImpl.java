package com.ausland.weixin.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ausland.weixin.model.db.LogisticPackage;
import com.ausland.weixin.model.reqres.StockInfo;

@Repository
public class PackageDaoImpl implements PackageDao{
	
	private static final Logger logger = LoggerFactory.getLogger(PackageDaoImpl.class);
    
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public String saveRecordsInDb(final List<LogisticPackage> list) {
		
		logger.debug("Entered saveExcelRecordsInDb method with records:"+ToStringBuilder.reflectionToString(list));
		String  SQL = "INSERT INTO LogisticPackage "
				+ "(trackno, weight, items, rname, rphone, rbphone, raddress, sname, sphone, saddress, createdby, createdtime, createdsrc, logisticcompany, comments, validationerrors, status) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		StringBuffer errorMessage = new StringBuffer();
		int[]  createRS = new int[list.size()];
		try {
			createRS = jdbcTemplate.batchUpdate(SQL,
					new BatchPreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement preparedStatement, int index) throws SQLException {
							LogisticPackage lp = list.get(index);
							preparedStatement.setString(1, lp.getLogisticTrackingNo());
							preparedStatement.setString(2, lp.getPackageWeight());
							preparedStatement.setString(3, lp.getProductItems());
							preparedStatement.setString(4, lp.getReceiverName() );
							preparedStatement.setString(5,  lp.getReceiverPhone());
							preparedStatement.setString(6,  lp.getReceiverBackupPhone());
							preparedStatement.setString(7,  lp.getReceiverAddress());
							preparedStatement.setString(8,  lp.getSenderName());
							preparedStatement.setString(9,  lp.getSenderPhone());
							preparedStatement.setString(10,  lp.getSenderAddress());
							preparedStatement.setString(11,  lp.getCreatedBy());
							preparedStatement.setString(12,  lp.getCreatedDateTime());
							preparedStatement.setString(13,  lp.getCreatedSrc());
							preparedStatement.setString(14,  lp.getLogisticCompany());
							preparedStatement.setString(15,  lp.getComments());
							if(lp.getValidationErrors() != null)
							{
								preparedStatement.setString(16,  lp.getValidationErrors().substring(0, 2048));
							}
							
							preparedStatement.setString(17,  lp.getStatus());
							
						}

						@Override
						public int getBatchSize() {
							return list.size();
						}
					});
			for (int index = 0; index < createRS.length; index++) {
				if (createRS[index] == Statement.EXECUTE_FAILED) {
					errorMessage.append("; insert db failed on record id:"+index);
				}
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			errorMessage.append("; caught exception:"+e.getMessage()) ;
		}
		logger.debug(
				"Exit createParticipant method after inserting " + createRS.length + " records.");
		return errorMessage.toString();
	}

	@Override
	public List<LogisticPackage> queryLogisticPackage(String excelFileName, String fromDate, String toDate,
			String receiverPhone) {
		logger.debug("entered queryLogisticPackage.");
		String sql = "";
		List<LogisticPackage> response = new ArrayList<LogisticPackage>();
		try
		{
			StringBuffer strb = new StringBuffer();
			strb.append("select * from LogisticPackage where ");
			boolean first = true;
			if(!StringUtils.isEmpty(excelFileName) && first == true)
			{
				strb.append(" createdSrc = '").append(excelFileName).append("'");
				first = false;
			}
			
			if(!StringUtils.isEmpty(fromDate) && !StringUtils.isEmpty(toDate))
			{
				if(first == false)
				{
					strb.append(" and ");
				}
				strb.append(" ( createdtime >= '").append(fromDate).append("' and createdtime <= '").append(toDate).append("' )");
			}
			
			if(!StringUtils.isEmpty(receiverPhone))
			{
				if(first == false)
				{
					strb.append(" and ");
				}
				strb.append(" rphone = '").append(receiverPhone).append("'");
			}
				
			logger.debug("queryLogisticPackage sql:"+sql);
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
			if(rows == null || rows.size() <= 0)
				return null;
			logger.debug("queryLogisticPackage got the rows:"+rows.size());
			for(Map row : rows)
			{
				LogisticPackage lp = new LogisticPackage();
				lp.setComments((String)row.get("comments"));
				lp.setCreatedBy((String)row.get("createdby"));
				lp.setCreatedDateTime((String)row.get("createdtime"));
				lp.setCreatedSrc((String)row.get("createdsrc"));
				lp.setLastupdatedDateTime((String)row.get("updatedtime"));
				lp.setLinkedOrderNo((String)row.get("linkedno"));
				lp.setLogisticCompany((String)row.get("logisticcompany"));
				lp.setPackageWeight((String)row.get("weight"));
				lp.setProductItems((String)row.get("items"));
				lp.setReceiverAddress((String)row.get("raddress"));
				lp.setReceiverBackupPhone((String)row.get("rbphone"));
				lp.setReceiverName((String)row.get("rname"));
				lp.setReceiverPhone((String)row.get("rphone"));
				lp.setSenderAddress((String)row.get("saddress"));
				lp.setSenderName((String)row.get("sname"));
				lp.setSenderPhone((String)row.get("sphone"));
				lp.setStatus((String)row.get("status"));
				lp.setUpdatedBy((String)row.get("updatedby"));
				lp.setValidationErrors((String)row.get("validationerrors"));
				 
				response.add(lp);
			}
		}
		catch(Exception e)
		{
			logger.error("caught exception during queryLogisticPackage sql:"+sql+"; errorMessage:"+e.getMessage());
		}
	   
		return response;
		 
	}

}
