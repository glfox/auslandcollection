import React from 'react';
import { getHistoryOrders } from '../utils/services.js';
import HistoryOrderContent from './historyordercontent.js';
import { Row, Form, FormGroup, ControlLabel, FormControl, Button } from 'react-bootstrap';

class HistoryOrder extends React.Component {

	constructor() {
		super();
		this.state = {
			phone: "",
			orders: null,
			error: null,
			loaded: true
		}
		
		this.handleChange = this.handleChange.bind(this);
	    this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleChange(event) {
		this.setState({
			phone: event.target.value
		});
	}
	handleSubmit(event) {
		if (this.state.phone) {
			this.setState({
				orders: null,
	    		error: null,
				loaded: false
			})
			this.getHistoryOrder(this.state.phone);
		}
		event.preventDefault();
	}

	getHistoryOrder(phone) {
		getHistoryOrders(this.state.phone)
			.then(result => {
				if (result.status === 'ok' && result.fydhList && result.fydhList.length > 0) {
	      			let history = result.fydhList;
					let rows = [];
					for (let i = 0; i < history.length; i++) {
						rows.push(
							<Row key={history[i].courierNumber.toString()}>
								<HistoryOrderContent order={history[i]} />
							</Row>
						);
					}
					this.setState({
						details: rows,
						error: null,
						loaded: true
					});
	      		} else if (!result.fydhList || result.fydhList.length === 0) {
					this.setState({
						details: null,
						error: "没有历史记录",
						loaded: true
					});
				} else {
					this.setState({
						details: null,
						error: result.errorDetails,
						loaded: true
					});
				}
			}, err => {
				this.setState({
					details: null,
					error: "请求错误，请联系客服",
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
						<ControlLabel>手机号码/姓名: </ControlLabel>{' '}
			          	<FormControl
				            type="text"
				            value={this.state.phone}
				            placeholder="手机号码或姓名"
				            onChange={this.handleChange}
				        />
			        </FormGroup>{' '}
			        <Button bsStyle="primary" type="submit">查询</Button>
			    </Form>
		      	<br/>
		      	<div>
			  		{this.state.details}
			  		{this.state.error}
			  		{loader}
			  	</div>
			</div>
		)
	}
}

export default HistoryOrder;