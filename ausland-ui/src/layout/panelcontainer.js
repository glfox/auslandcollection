import React from 'react';
import { NavItem, Nav, Navbar, Grid, Row } from 'react-bootstrap';
import SearchOrders from '../searchorders/searchorders.js';
import SearchStock from '../searchstock/searchstock.js';
import HistoryOrder from '../searchorders/historyorders.js';
import Login from '../login/login.js';
import { isLogin, logout } from '../utils/services.js'

const contents = {
	"order": <SearchOrders />,
	"history": <HistoryOrder />,
	"stock": <SearchStock />,
	"login": <Login />
}

class PanelContainer extends React.Component {

	constructor() {
		super();
		this.state = {
			item: <SearchStock />,
			show: false,
			username: '',
		}
		this.handleSelect = this.handleSelect.bind(this);
		this.handleShow = this.handleShow.bind(this);
    	this.handleClose = this.handleClose.bind(this);
    	this.logout = this.logout.bind(this);
    	this.updateStatus = this.updateStatus.bind(this);

    	this.checkLoginStatus();
	}

	handleClose() {
    	this.setState({ show: false });
  	}

  	handleShow() {
    	this.setState({ show: true });
  	}

	handleSelect(selectedKey) {
		this.setState({
			item: contents[selectedKey]
		})
	}

	logout() {
		logout().then(res => {
			if (!res.isLogin) {
				this.setState({
					login: false,
					username: ''
				})
				window.location.reload(true);
			}
		}, err => {
			console.log(err)
		});
	}

	checkLoginStatus() {

		isLogin().then(res => {
			this.updateStatus(res.login, res.userName)
		}, err => {
			console.log(err)
		});
	}

	updateStatus(isLogin, userName) {
		if (isLogin) {
			this.setState({
				login: true,
				username: userName,
			})
		} else {
			this.setState({
				login: false,
				username: '',
			})
		}
	}

	render() {
		let status;
		if (this.state.login) {
			status = (
				<NavItem>
					<span>{this.state.username}</span>{', '}
					<span onClick={this.logout}>登出</span>
				</NavItem>
			)
		} else {
			status = (
				<NavItem onClick={this.handleShow}>
					登陆/注册
				</NavItem>
			)
		}
		return (
			<div>
				<Navbar inverse collapseOnSelect fixedTop>
				  	<Navbar.Header>
				    	<Navbar.Brand>
				      		<a href="">Ausland Collection</a>
				    	</Navbar.Brand>
				    	<Navbar.Toggle />
				  	</Navbar.Header>
				 	<Navbar.Collapse>
					    <Nav onSelect={this.handleSelect}>
					    	<NavItem eventKey="stock">商品查询</NavItem>
					      	{this.state.login? <NavItem eventKey="order">单号查询</NavItem> : null}
							{this.state.login? <NavItem eventKey="history">手机查询</NavItem> : null}
					    </Nav>
					    <Nav pullRight>
							{status}
					    </Nav>
				  	</Navbar.Collapse>
				</Navbar>
				<div className="nav-content">
					<Grid>
						<Row>
							{this.state.item}
						</Row>
					</Grid>
				</div>
				<Login show={this.state.show} handleShow={this.handleShow} handleClose={this.handleClose} loginStatus={this.updateStatus}/>
			</div>
		)
	}
}

export default PanelContainer;