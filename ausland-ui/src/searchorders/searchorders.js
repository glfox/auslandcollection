import React, { Component } from 'react';
import OrderDetails from './orderdetails.js'

class SearchOrders extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	trackingNo: "",
	    	details: null
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
			console.log(this.state.trackingNo);
			this.setState({
				details: <OrderDetails orderNo={this.state.trackingNo} />
			})
		}
		event.preventDefault();
	}

	render() {
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
		      	{this.state.details}
			</div>
		)
	}
}

export default SearchOrders;