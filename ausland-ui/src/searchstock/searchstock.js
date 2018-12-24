import React from 'react';
import './searchstock.css';
import { Form,FormGroup,Button,ControlLabel,Table, FormControl } from 'react-bootstrap';
import { getAllProdID, queryShouhou } from '../utils/services.js';
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
		queryShouhou(searchVal)
			.then(res => {

				if (res && res.returncode === "1") {
					this.setState({
						error: res.returninfo,
						loaded: true,
					})
					return;
				}

				if (!res.datalist || res.datalist.length === 0) {
					this.setState({
						error: '售后不存在',
						loaded: true,
					})
					return;
				}

				let rows = [];
				rows.push(
					<tr key="-1">
						<th>售后时间</th>
						<th>品牌</th>
						<th>收货人姓名</th>
						<th>售后货号</th>
						<th>售后问题</th>
						<th>处理进度</th>
						<th>换码费</th>
						<th>客户寄出单号</th>
						<th>品牌方寄出单号</th>
					</tr>
				)
				for (let i = 0; i < res.datalist.length; i++) {
					let data = res.datalist[i];
					rows.push(
						<tr key={i}>
							<td key={i + '-' + data.creationdate}>{data.creationdate}</td>
							<td key={i + '-' + data.brand}>{data.brand}</td>
							<td key={i + '-' + data.name}>{data.name}</td>
							<td key={i + '-' + data.product}>{data.product}</td>

							<td key={i + '-' + data.problem}>{data.problem}</td>
							<td key={i + '-' + data.progress}>{data.progress}</td>
							<td key={i + '-' + data.comments}>{data.comments}</td>

							<td key={i + '-' + data.customercourierid}>{data.customercourierid}</td>
							<td key={i + '-' + data.brandcourierid}>{data.brandcourierid}</td>
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
		        <ControlLabel>手机号码/姓名: </ControlLabel>{' '}
		        <FormControl
				            type="text"
				            value={this.state.searchstr}
				            placeholder="输入手机号码/姓名"
				            onChange={this.handleChange}
				        />{' '}
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