//const BASE_URL = 'http://118.24.75.119';
const BASE_URL = 'http://localhost:8081';
const CONTEXT_PATH = '/auslandtest';
export { getOrderDetails };

function getOrderDetails(trackingNo) {
	let url = BASE_URL + CONTEXT_PATH + '/query/zhonghuan/detailsbytrackingno?trackingno=' + trackingNo;
	return fetch(url).then(res => res.json());
}