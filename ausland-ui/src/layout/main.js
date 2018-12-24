import React from 'react';
import { NavItem, Nav, Navbar, Grid, Row } from 'react-bootstrap';
import SearchOrders from '../searchorders/searchorders.js';
import SearchStock from '../searchstock/searchstock.js';
import HistoryOrder from '../searchorders/historyorders.js';
import '../App.css';

import { Link } from 'react-router-dom'

const contents = {
	"order": <SearchOrders />,
	"history": <HistoryOrder />,
	"stock": <SearchStock />,
}

class Main extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			item: this.props.children,
			show: false,
			username: '',
		}
		this.handleSelect = this.handleSelect.bind(this);
	}
	
	handleSelect(selectedKey) {
		this.setState({
			item: contents[selectedKey]
		})
	}
	render() {
		return (
			<div className="App">
				<Navbar inverse collapseOnSelect fixedTop>
				  	<Navbar.Header>
				    	<Navbar.Brand>
				      	<a href="">Ausland Collection</a>
				    	</Navbar.Brand>
				    	<Navbar.Toggle />
				  	</Navbar.Header>
				 	<Navbar.Collapse>
					    <Nav>
					    	<NavItem eventKey="stock"><Link to="/ui/stock">售后查询</Link></NavItem>
					      <NavItem eventKey="order"><Link to="/ui/order">物流查询</Link></NavItem>
							  <NavItem eventKey="history"><Link to="/ui/history">订单查询</Link></NavItem>
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
			</div>
		)
	}
}

export default Main;