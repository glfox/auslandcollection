import React from 'react';
import './searchstock.css';
import { Form,FormGroup,Button,FormControl,ControlLabel,Table,Dropdown,Checkbox } from 'react-bootstrap';
import { getProductListBy, getAllBrands, getAllCategory } from '../utils/services.js';

class SearchStock extends React.Component {

	constructor() {
	    super();
	    this.state = {
	    	brandOptions: null,
	    	categoriesOptions: null,
	    	selectedBrands: [],
	    	selectedCategories: [],
	    	searchstr: "",
	    	details: null,
	    	error: null,
	    	loaded: true,
	    };
	    this.handleChange = this.handleChange.bind(this);
	    this.handleSubmit = this.handleSubmit.bind(this);
	    this.handleBrandSelect = this.handleBrandSelect.bind(this);
	    this.handleCategorySelect = this.handleCategorySelect.bind(this);
	}

	componentDidMount() {
		this.getAllBrands();
		this.getAllCategory();
	}

	handleChange(event) {
		this.setState({
			searchstr: event.target.value
		})
	}

	//function getProductListBy(brands, productIds, matchingStr, pageNo)
	handleSubmit(event) {
		this.queryProduct(this.state.selectedBrands.join(','), this.state.searchstr);
	}

	handleBrandSelect(event) {
		let value = event.target.getAttribute("eventkey");
		let index = this.state.selectedBrands.indexOf(value);
		if (index > -1) {
			this.state.selectedBrands.splice(index, 1);
		} else {
			index = this.state.selectedBrands.length;
			this.state.selectedBrands.splice(index, 0, value);
		}
		document.getElementById("brandsSelector").innerHTML = this.state.selectedBrands.length > 0? this.state.selectedBrands : "选择品牌";
	}

	handleCategorySelect(event) {
		let value = event.target.getAttribute("eventkey");
		let index = this.state.selectedCategories.indexOf(value);
		if (index > -1) {
			this.state.selectedCategories.splice(index, 1);
		} else {
			index = this.state.selectedCategories.length;
			this.state.selectedCategories.splice(index, 0, value);
		}
		document.getElementById("categoriesSelector").innerHTML = this.state.selectedCategories.length > 0? this.state.selectedCategories : "选择类别";
	}

	queryProduct(brands, queryStr) {
		getProductListBy(brands, null, queryStr, 0)
			.then(res => {
				if (res.status === 'ok') {
					if (res.products && res.products.length > 0) {
						let rows = [];
						let maxLength = 0;
						for (let i = 0; i < res.products.length; i++) {
							let p = res.products[i];
							let stocks = p.stock;
							for (let j = 0; j < stocks.length; j++) {
								if (stocks[j].size.split(',').length > maxLength) {
									maxLength = stocks[j].size.split(',').length;
								}
							}
						}
						rows.push(
							<tr key="-1">
								<th>产品名称</th>
								<th>品牌</th>
								<th>类别</th>
								<th>预览图片</th>
								<th>颜色</th>
								<th colSpan={maxLength}>库存状态</th>
							</tr>
						);
						for (let i = 0; i < res.products.length; i++) {
							let p = res.products[i];
							let stocks = p.stock;
							
							for (let j = 0; j < stocks.length; j++) {
								let sizeStatus = [];
								if (j === 0) {
									sizeStatus.push(
										<td rowSpan={stocks.length} key={p.productId + "*" + p.productName}>{p.productName}</td>
									)
									sizeStatus.push(
										<td rowSpan={stocks.length} key={p.productId + "*" + p.brand}>{p.brand}</td>
									)
									sizeStatus.push(
										<td rowSpan={stocks.length} key={p.productId + "*" + p.category}>{p.category}</td>
									)
								}
								sizeStatus.push(
									<td key={p.productId + "*image"}><img src={p.productSmallImage} alt=""/></td>
								)
								sizeStatus.push(
									<td key={p.productId + "*" + stocks[j].color}>{stocks[j].color}</td>
								)
								let sizes = stocks[j].size.split(',');
								let status = stocks[j].stockStatus.split(',');
								let len = sizes.length;
								for (let k = 0; k < maxLength; k++) {
									if (k >= len) {
										sizeStatus.push(
											<td key={p.productId + "*notfound" + k}>{'-'}</td>
										)
									} else {
										let instock = (status[k] === 'Y'? 'btn-success':'btn-danger');
										sizeStatus.push(
											<td className={instock} key={p.productId + "*" + k + "*" + sizes[k]}>{sizes[k]}</td>
										)
									}
								}
								rows.push(
									<tr key={i + '*' + j}>
										{sizeStatus}
									</tr>
								);
							}
						}
						this.setState({
							details: rows,
							error: null,
							loaded: true
						})
					} else {
						this.setState({
							details: null,
							error: "没有找到商品",
							loaded: true
						})
					}
				} else {
					console.log(res.errorDetails);
					this.setState({
						details: null,
						error: "没有找到商品",
						loaded: true
					})
				}
			}, err => {
				console.log(err);
				this.setState({
					details: null,
					error: "请求错误",
					loaded: true
				})
			});
	}

	getAllBrands() {
		getAllBrands()
			.then(result => {
				if (result.status === 'ok') {
					let brands = [];
					for (let i = 0; i < result.list.length; i++) {
						brands.push(
							<div key={result.list[i]}>
								<Checkbox inline eventkey={result.list[i]} onClick={this.handleBrandSelect}>{result.list[i]}</Checkbox>
							</div>
						);
					}
					
					this.setState({
						brandOptions: brands
					})
				} else {
					console.log(result.errorDetails);
				}
			}, err => {
				console.log(err);
			});
	}

	getAllCategory() {
		getAllCategory()
			.then(result => {
				if (result.status === 'ok') {
					let categories = [];
					for (let i = 0; i < result.list.length; i++) {
						categories.push(
							<div key={result.list[i]}>
								<Checkbox inline eventkey={result.list[i]} onClick={this.handleCategorySelect}>{result.list[i]}</Checkbox>
							</div>
						);
					}
					
					this.setState({
						categoriesOptions: categories
					})
				} else {
					console.log(result.errorDetails);
				}
			}, err => {
				console.log(err);
			});
	}

	render() {
		let loader = this.state.loaded? null : <div className="loader"/>
		return (
			<div>
				<Form inline onSubmit={this.handleSubmit}>
					<FormGroup controlId="orderInfo">
			        	<ControlLabel>产品名称: </ControlLabel>{' '}
			        	<FormControl
				            type="text"
				            value={this.state.searchstr}
				            placeholder="输入商品信息"
				            onChange={this.handleChange}
				        />{' '}
			        </FormGroup>{' '}
			        <FormGroup controlId="brands">
				      	<ControlLabel>品牌: </ControlLabel>{' '}
				      	<Dropdown id="brandsSelector">
				      		<Dropdown.Toggle>
						     	选择品牌
						    </Dropdown.Toggle>
						    <Dropdown.Menu>
						    	{this.state.brandOptions}
					      	</Dropdown.Menu>
					    </Dropdown>
				    </FormGroup>{' '}

				    <FormGroup controlId="categories">
				      	<ControlLabel>类别: </ControlLabel>{' '}
				      	<Dropdown id="categoriesSelector">
				      		<Dropdown.Toggle>
						     	选择类别
						    </Dropdown.Toggle>
						    <Dropdown.Menu>
						    	{this.state.categoriesOptions}
					      	</Dropdown.Menu>
					    </Dropdown>
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