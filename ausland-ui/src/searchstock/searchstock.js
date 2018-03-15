import React, { Component } from 'react';
import { getProductListBy } from '../utils/services.js'
import './searchstock.css';
import { Form,FormGroup,Button,FormControl,ControlLabel,Table,BootstrapTable} from 'react-bootstrap'

class SearchStocks extends Component {

	constructor(props) {
	    super(props);
	    this.state = {
	    	brands: "",
	    	productIds: "",
	    	matchingStr:"",
	    	pageNo: 0,
	    	details: null,
	    	error: null,
	    	loaded: true
	    };
	    this.handleChange = this.handleChange.bind(this);
	    this.handleSubmit = this.handleSubmit.bind(this);
	}

	handleChange(event) {
		this.setState({matchingStr: event.target.value});
	}
	
	handleOnSelect(event){
		this.setState({brands: event.target.value});
	}
	
	handleSubmit(event) {
		if (this.state.brands || this.state.productIds || this.state.matchingStr) {
			this.setState({ 
	    		details: null,
	    		error: null,
				loaded: false
			})
			this.getProductListBy(this.state.brands,this.state.productIds, this.state.matchingStr, this.state.pageNo);
		}
		event.preventDefault();
	}

	getProductListBy(brands, productIds, matchingStr, pageNo) {
		getProductListBy(this.state.brands, this.state.productIds, this.state.matchingStr, this.state.pageNo)
			.then(result => {
				console.log(result);
				if (result.status === 'ok' && result.products) {
	      			let paklog = result.products;
					let rows = [];
					if (paklog.length > 0) {
					for (let i = 0; i < paklog.length; i++) {
							let log = paklog[i];
							let rowNo = 1;
							if(log.stock)
							{
							    rowNo = log.stock.length;
							}
							 
							rows.push(
								<tr>
								    <td rowspan={rowNo}>{log.brand}</td>
								    <td rowspan={rowNo}>{log.productId}</td>
								    <td rowspan={rowNo}>{log.productSmallImage}</td>  
									<td >{log.stock[0].color}</td>
								</tr>
							);
							if(rowNo > 1)
							{
							    for(let j = 1; j < log.stock.length; j ++)
						    	{
							    	rows.push(
									    	<tr>
												<td>{log.stock[j].color}</td>
											</tr>	
									    );
						    	}
							}
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
						error: "没有找到任何商品",
						loaded: true
					});
				}
			}, err => console.log(err));
	}

	render() {
		let loader = this.state.loaded? null : <div className="loader"/>
		return (
			<div>
				<Form inline onSubmit={this.handleSubmit}>
					<FormGroup controlId="formInlineName">
						<ControlLabel>商品型号: </ControlLabel>{' '}
			          	<FormControl
				            type="text"
				            value={this.state.matchingStr}
				            placeholder="输入商品型号"
				            onChange={this.handleChange}
				        />
			        </FormGroup>{' '}
			        <Button bsStyle="primary" type="submit">查询</Button>
			    </Form>
		      	<br/>

			  	<div>
		  		<Table responsive striped bordered condensed hover>
			  		<thead>
			  	    <tr>
			  	      <th>商品品牌</th>
			  	      <th>商品型号</th>
			  	      <th>商品图片</th>
			  	      <th>商品颜色</th>
			  	    </tr>
			  	  </thead>
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

export default SearchStocks;