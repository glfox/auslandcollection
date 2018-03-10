import React, { Component } from 'react';
import './searchstock.css';

class SearchStock extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	brand: "",
	    	productId:"",
	    	productName:"",
	    	isChanged: false,
	    	stockStatus: "",
	    	error: ""
	    };
	    this.handleChange = this.handleChange.bind(this);
	    this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleChange(event) {
		if (this.state.brand !== event.target.value) {
			this.setState({
				isChanged: true,
				brand: event.target.value
			});
		} else {
			this.setState({
				isChanged: true,
			});
		}
	}
	handleSubmit(event) {
		if (this.state.trackingNo > 0 && this.state.isChanged) {
			this.getDetails(this.state.trackingNo);
		}
		event.preventDefault();
	}

	getDetails(trackingNo) {
		fetch("http://118.24.75.119/query/zhonghuan/detailsbytrackingno?trackingno=" + trackingNo)
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
						error: result.errorDetails
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

	render() {
		return (
			<div>
				<form onSubmit={this.handleSubmit}>
			        <label>
			          	产品名称:
			        	<input type="text" value={this.state.trackingNo} onChange={this.handleChange} />
			        </label>
			        
			        <input type="submit" value="查询" />
		      	</form>
		      	<br/>
		      	<div>
		      		<table align="center">
		      			<tbody>
		      				{this.state.details}
		      			</tbody>
		      		</table>
		      	</div>
		      	<div>{this.state.error}</div>
			</div>
		)
	}
}

export default SearchOrders;