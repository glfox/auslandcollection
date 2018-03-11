import React from 'react';
import './searchorders.css';

class OrderDetails extends React.Component {
	getDetails(trackingNo) {
		//console.log("orderNo: " + this.state.trackingNo);
		fetch("http://118.24.75.119/auslandtest/query/zhonghuan/detailsbytrackingno?trackingno=" + trackingNo)
	      	.then(res => res.json())
	      	.then(result => {
	      		console.log(result);
	      		if (result.back && result.back.logisticsback) {
	      			let paklog = result.back.logisticsback;
					let rows = [];
					if (paklog.length > 0) {
						for (let i = 0; i < paklog.length; i++) {
							let log = paklog[i];
							rows.push(
								<tr key={log.ztai.toString()}>
									<td>{log.time}</td>
									<td>{log.ztai}</td>
								</tr>
							);
						}
						this.setState({
							details: rows,
							error: null
						});
					}
	      		} else {
					this.setState({
						details: null,
						error: "订单号不存在"
					});
				}
	        },
	        // Note: it's important to handle errors here
	        // instead of a catch() block so that we don't swallow
	        // exceptions from actual bugs in components.
	        (error) => {
	     		console.log(error);
	        })
	}

	constructor(props) {
	    super(props);
	    this.state = {
	    	trackingNo: props.orderNo,
	    	details: null,
	    	error: ""
	    };
	}

	componentDidMount() {
		this.getDetails(this.state.trackingNo);
	}

	render() {
		return (
			<div>
		  		<table align="center">
		  			<tbody>
		  				{this.state.details}
		  			</tbody>
		  		</table>
		  		{this.state.error}
		  	</div>
		)
	}

	componentWillReceiveProps(nextProps) {
		if (this.state.trackingNo === nextProps.orderNo) {
			return;
		}
		this.setState({
			trackingNo: nextProps.orderNo
		});
		//console.log("state no: " + this.state.trackingNo);
		this.getDetails(nextProps.orderNo);
	}
}

export default OrderDetails;