const BASE_URL = 'http://118.24.75.119';
//const BASE_URL = 'http://localhost:8081';
const CONTEXT_PATH = '/auslandtest';
const page_size = 30;
export { getOrderDetails, getHistoryOrders, getProductListBy, getAllBrands, getAllCategory };

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

function getAllBrands() {
	let url = BASE_URL + CONTEXT_PATH + '/query/product/getallbrand';
	return fetch(url).then(res => res.json());
}

function getAllCategory() {
	let url = BASE_URL + CONTEXT_PATH + '/query/product/getallcategory';
	return fetch(url).then(res => res.json());
}