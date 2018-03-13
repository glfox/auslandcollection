import React, { Component } from 'react';
import { getOrderDetails } from '../utils/services.js'
import './searchorders.css';

class SearchOrders extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	trackingNo: "",
	    	details: null,
	    	error: null,
	    	loaded: true
	    };
	    this.handleChange = this.handleChange.bind(this);
	    this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleChange(event) {
		this.setState({
			trackingNo: event.target.value
		});
	}
	handleSubmit(event) {
		if (this.state.trackingNo) {
			this.setState({
				details: null,
	    		error: null,
				loaded: false
			})
			this.getOrderDetails(this.state.trackingNo);
		}
		event.preventDefault();
	}

	getOrderDetails(trackingNo) {
		getOrderDetails(this.state.trackingNo)
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
							error: null,
							loaded: true
						});
					}
	      		} else {
					this.setState({
						details: null,
						error: "订单号不存在",
						loaded: true
					});
				}
			}, err => console.log(err));
	}

	render() {
		let loader = this.state.loaded? null : <div className="loader" position="center"/>
		return (
			<div>
				<form onSubmit={this.handleSubmit}>
			        <label>
			          	订单号:
			        	<input type="text" value={this.state.trackingNo} onChange={this.handleChange} />
			        </label>
			        <input type="submit" value="查询" />
		      	</form>
		      	<br/>
		      	<div>
		      		{loader}
			  		<table align="center">
			  			<tbody>
			  				{this.state.details}
			  			</tbody>
			  		</table>
			  		{this.state.error}
			  	</div>
			</div>
		)
	}
}

export default SearchOrders;