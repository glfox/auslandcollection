import React from 'react';
import { NavItem, Nav, Navbar, Grid, Row } from 'react-bootstrap';
import SearchOrders from '../searchorders/searchorders.js';
import SearchStock from '../searchstock/searchstock.js';
import HistoryOrder from '../searchorders/historyorders.js';

const contents = {
	"order": <SearchOrders />,
	"history": <HistoryOrder />,
	"stock": <SearchStock />
}

class PanelContainer extends React.Component {

	constructor() {
		super();
		this.state = {
			item: <SearchOrders />,
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
					      	<NavItem eventKey="order">单号查询</NavItem>
							<NavItem eventKey="history">手机查询</NavItem>
							<NavItem eventKey="stock">商品查询</NavItem>
					    </Nav>
					    <Nav pullRight onSelect={this.handleSelect}>
					      	<NavItem eventKey="login">
					        	登陆
					      	</NavItem>
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