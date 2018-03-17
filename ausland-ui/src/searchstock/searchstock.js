import React, { Component } from 'react';
import './searchstock.css';
import { Form,FormGroup,Button,FormControl,ControlLabel,Table } from 'react-bootstrap';

class SearchStock extends Component {

	constructor() {
	    super();
	    this.state = {
	    	brand: "",
	    	productId:"",
	    	productName:"",
	    	isChanged: false,
	    	stockStatus: "",
	    	error: null,
	    	details: null,
	    	loaded: true
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
			this.setState({
				error: null,
				details: null,
				loaded: false
			})
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
							error: null,
							loaded: true
						});
					}
	      		} else {
					this.setState({
						details: null,
						error: result.errorDetails,
						loaded: true
					});
				}
	        },
	        // Note: it's important to handle errors here
	        // instead of a catch() block so that we don't swallow
	        // exceptions from actual bugs in components.
	        (error) => {
	     		console.log(error);
	     		this.setState({
					details: null,
					error: error,
					loaded: true
				});	
	        })
	}

	render() {
		let loader = this.state.loaded? null : <div className="loader"/>
		return (
			<div>
				<Form inline onSubmit={this.handleSubmit}>
					<FormGroup controlId="orderForm">
			        	<ControlLabel>产品名称: </ControlLabel>{' '}
			        	<FormControl
				            type="text"
				            value={this.state.trackingNo}
				            placeholder="输入商品信息"
				            onChange={this.handleChange}
				        />{' '}
			        	<Button bsStyle="primary" type="submit">查询</Button>
			        </FormGroup>
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

export default SearchStock;