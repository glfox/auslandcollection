import React, { Component } from 'react';
import { getOrderDetails } from '../utils/services.js';
import './searchorders.css';
import { Form,FormGroup,Button,FormControl,ControlLabel,Table } from 'react-bootstrap';

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
			}, err => {
				this.setState({
					details: null,
					error: "请求错误",
					loaded: true
				});
				console.log(err)
			});
	}

	render() {
		let loader = this.state.loaded? null : <div className="loader"/>
		return (
			<div>
				<Form inline onSubmit={this.handleSubmit}>
					<FormGroup controlId="orderForm">
						<ControlLabel>订单号: </ControlLabel>{' '}
			          	<FormControl
				            type="text"
				            value={this.state.trackingNo}
				            placeholder="输入订单号"
				            onChange={this.handleChange}
				        />
			        </FormGroup>{' '}
			        <Button bsStyle="primary" type="submit">查询</Button>
			    </Form>
		      	<br/>
		      	<div>
			  		<Table striped bordered condensed hover>
			  			<tbody>
			  				{this.state.details}
			  			</tbody>
			  		</Table>
			  		{this.state.error}
			  		{loader}
			  	</div>
			</div>
		)
	}
}

export default SearchOrders;