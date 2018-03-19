import React from 'react';
import { Grid, Row, Col, Tab, NavItem, Nav } from 'react-bootstrap';
import SearchOrders from '../searchorders/searchorders.js';
import SearchStock from '../searchstock/searchstock.js';
import HistoryOrder from '../searchorders/historyorders.js';
import Login from '../login/login.js';
import CreateUser from '../login/createuser.js';
class PanelContainer extends React.Component {

	render() {
		return (
			<div>
				<Row>
					<h1 className="App-header">Ausland Collection</h1>
				</Row>
				<Tab.Container id="search-table" defaultActiveKey="order">
					<Grid>
						 <Row className="clearfix nav-link navbar-toggler sidebar-toggler">
						    <Col sm={2}>
						      	<Nav bsStyle="pills" stacked>
						      		<NavItem eventKey="order">单号查询</NavItem>
							        <NavItem eventKey="history">手机查询</NavItem>
							        <NavItem eventKey="stock">商品查询</NavItem>
							        <NavItem eventKey="login">登陆/注册</NavItem>
						      	</Nav>
						    </Col>
						    <Col sm={10}>
						      	<Tab.Content animation>
						        	<Tab.Pane eventKey="order"><SearchOrders /></Tab.Pane>
						        	<Tab.Pane eventKey="history"><HistoryOrder /></Tab.Pane>
						        	<Tab.Pane eventKey="stock"><SearchStock /></Tab.Pane>
						        	<Tab.Pane eventKey="login"><Login /></Tab.Pane>
						        	
						      	</Tab.Content>
						    </Col>
						</Row>
					</Grid>
				</Tab.Container>	
			</div>
		)
	}
}

export default PanelContainer;