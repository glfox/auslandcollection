//const BASE_URL = 'http://118.24.75.119';
const BASE_URL = 'http://localhost:8081';
const CONTEXT_PATH = '/auslandtest';
const page_size = 30;
export { getOrderDetails };
export { getProductListBy };

function getOrderDetails(trackingNo) {
	let url = BASE_URL + CONTEXT_PATH + '/query/zhonghuan/detailsbytrackingno?trackingno=' + trackingNo;
	return fetch(url).then(res => res.json());
}

function getProductListBy(brands, productIds, matchingStr, pageNo) {	
	let url = BASE_URL + CONTEXT_PATH + '/query/product/getproductby?pageSize=' + page_size + "&pageNo="+pageNo+"&matchingstr="+matchingStr+"&brands="+brands+"&productIds="+productIds;
	return fetch(url).then(res => res.json());
}
 
