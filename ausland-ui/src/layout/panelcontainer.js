import React from 'react';
import { NavItem, Nav, Navbar, Grid, Row } from 'react-bootstrap';
import SearchOrders from '../searchorders/searchorders.js';
import SearchStock from '../searchstock/searchstock.js';
import HistoryOrder from '../searchorders/historyorders.js';

const contents = {
	"order": <SearchOrders />,
	"history": <HistoryOrder />,
	"stock": <SearchStock />,
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
	}
	
	handleSelect(selectedKey) {
		this.setState({
			item: contents[selectedKey]
		})
	}
	render() {
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
					    	<NavItem eventKey="stock">库存查询</NavItem>
					      <NavItem eventKey="order">物流查询</NavItem>
							  <NavItem eventKey="history">订单查询</NavItem>
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

export default PanelContainer;