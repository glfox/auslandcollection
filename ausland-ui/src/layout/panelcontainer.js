import React from 'react';
import { Grid, Row, Col, Tab, NavItem, Nav } from 'react-bootstrap';
import SearchOrders from '../searchorders/searchorders.js';
import SearchStock from '../searchstock/searchstock.js';

class PanelContainer extends React.Component {

	constructor() {
		super();
		this.state = {
			rightPanel: <SearchOrders />
		};
		this.compMap = new Map();
		this.compMap.set("order", <SearchOrders />);
		this.compMap.set("stock", <SearchStock />);

		this.handleOnClick = this.handleOnClick.bind(this);
	}

	handleOnClick(t) {
		this.setState({
			rightPanel: this.compMap.get(t.toString())
		});
	}

	render() {
		return (
			<Grid>
				<Row>
					<h1>Ausland Collection</h1>
				</Row>
				<Tab.Container id="search-table" defaultActiveKey="order">
					 <Row className="clearfix">
					    <Col md={2} xs={4}>
					      	<Nav bsStyle="pills" stacked>
					        	<NavItem eventKey="order">订单查询</NavItem>
					        	<NavItem eventKey="stock">库存查询</NavItem>
					      	</Nav>
					    </Col>
					    <Col md={8} xs={8}>
					      	<Tab.Content animation>
					        	<Tab.Pane eventKey="order"><SearchOrders /></Tab.Pane>
					        	<Tab.Pane eventKey="stock"><SearchStock /></Tab.Pane>
					      	</Tab.Content>
					    </Col>
					  </Row>
				</Tab.Container>	
			</Grid>
		)
	}
}

export default PanelContainer;