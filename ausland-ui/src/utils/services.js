const BASE_URL = 'http://118.24.75.119:8080';
//const BASE_URL = 'http://localhost:8081';
const CONTEXT_PATH = '/auslandweixin';
const page_size = 30;
export { 
	getOrderDetails, 
	getHistoryOrders, 
	getProductListBy,
	getAllProdID,
	queryDiFou
};

function getOrderDetails(trackingNo) {
	let url = BASE_URL + CONTEXT_PATH + '/query/zhonghuan/detailsbytrackingno?trackingno=' + trackingNo;
	return fetch(url).then(res => res.json());
}

function getHistoryOrders(phone) {
	let url = BASE_URL + CONTEXT_PATH + '/query/zhonghuan/lastthreemonth?phone=' + phone;
	return fetch(url).then(res => res.json());
}

function getProductListBy(brands, productIds, matchingStr, pageNo) {	
	let url = BASE_URL + CONTEXT_PATH
		 + '/query/product/getproductby?pageSize=' + page_size
		 + "&pageNo=" + pageNo 
		 + "&matchingstr=" + matchingStr 
		 + "&brands=" + brands;
	return fetch(url, 
		{
			method: 'POST',
			credentials: 'include',
		}).then(res => res.json());
}

function getAllProdID(phone) {
	let url = BASE_URL + CONTEXT_PATH + '/difou/getAllProductIdList';
	return fetch(url,{
		method: 'POST',
	}).then(res => res.json());
}

function queryDiFou(searchVal) {
	let url = BASE_URL + CONTEXT_PATH + '/difou/getProductStockList?productId=' + searchVal;
	return fetch(url,{
		method: 'POST',
	}).then(res => res.json());
}



