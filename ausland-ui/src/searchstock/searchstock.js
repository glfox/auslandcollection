import React from 'react';
import './searchstock.css';
import { Form,FormGroup,Button,ControlLabel,Table } from 'react-bootstrap';
import { getAllProdID, queryDiFou } from '../utils/services.js';
import {Typeahead} from 'react-bootstrap-typeahead';

class SearchStock extends React.Component {

	constructor() {
	    super();
	    this.state = {
	    	prodIds: [],
	    	searchstr: "",
	    	details: null,
	    	error: null,
	    	loaded: true,
	    };
	    this.handleChange = this.handleChange.bind(this);
	    this.handleSubmit = this.handleSubmit.bind(this);
	}

	// init here: get all ids
	componentDidMount() {
		this.getAllProdID();
	}

	handleChange(event) {
		this.setState({
			searchstr: event.target.value
		})
	}

	//function getProductListBy(brands, productIds, matchingStr, pageNo)
	handleSubmit(event) {
		this.setState({
			loaded: false
		})
		this.getProdStockDetails(this.state.searchstr);
	}

	getAllProdID() {
		getAllProdID()
			.then(res => {
				this.setState({
					prodIds: res
				})
			}, err => {
				console.log("请求错误： " + err)
			})
	}

	getProdStockDetails(searchVal) {
		queryDiFou(searchVal)
			.then(res => {

				if (res && res.returncode === 1) {
					this.setState({
						error: res.returninfo,
						loaded: true,
					})
					return;
				}

				if (res.returninfo === 0) {
					this.setState({
						error: '产品不存在',
						loaded: true,
					})
					return;
				}

				if (res.returninfo > 0 && !res.datalist) {
					this.setState({
						error: '可能的产品为： ' + res.possibleids + ', 请重新查询',
						loaded: true,
					})
					return;
				}

				if (!res.datalist || res.datalist.length === 0) {
					this.setState({
						error: '产品不存在',
						loaded: true,
					})
					return;
				}

				let rows = [];
				rows.push(
					<tr key="-1">
						<th>产品编号</th>
						<th>种类编号</th>
						<th>库存数量</th>
					</tr>
				)
				for (let i = 0; i < res.datalist.length; i++) {
					let data = res.datalist[i];
					rows.push(
						<tr key={i}>
							<td key={i + '-' + data.goodsno}>{data.goodsno}</td>
							<td key={i + '-' + data.specname}>{data.specname}</td>
							<td key={i + '-' + data.stock}>{data.stock}</td>
						</tr>
					)
				}
				this.setState({
					details: rows,
					loaded: true,
				})
			}, err => {
				console.err("错误： " + err);
				this.setState({
					error: '请求错误： ' + err,
					loaded: true,
				})
			})
	}

	render() {
		let loader = this.state.loaded? null : <div className="loader"/>
		return (
			<div>
				<Form inline>
					<FormGroup controlId="searchProd">
		        <ControlLabel>产品名称: </ControlLabel>{' '}
		        <ControlLabel>
		        	<Typeahead
              onChange={(selected) => {
                    this.setState({searchstr: selected})}}
              options={this.state.prodIds}
              emptyLabel='没有此商品'
            />
		       </ControlLabel>{' '}
	        </FormGroup>{' '}
	        <Button bsStyle="primary" type="button" onClick={this.handleSubmit}>查询</Button>
		    </Form>
      	<br/>
      	<div>
      		<Table bordered responsive>
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