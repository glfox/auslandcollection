const BASE_URL = 'http://118.24.75.119';

export { getOrderDetails };

function getOrderDetails(trackingNo) {
	let url = BASE_URL + '/auslandtest/query/zhonghuan/detailsbytrackingno?trackingno=' + trackingNo;
	return fetch(url).then(res => res.json());
}