package com.ausland.weixin.dao;

import java.util.List;

import com.ausland.weixin.model.db.LogisticPackage;

public interface PackageDao {

	
	String saveRecordsInDb(List<LogisticPackage> records);
	List<LogisticPackage> queryLogisticPackage(String excelFileName, String fromDate, String toDate, String receiverPhone);
}
