import React from 'react';
import MenuPanel from './menupanel';
import { Grid, Row, Col } from 'react-bootstrap';
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
				<Row>
					<Col md={2} componentClass="span">
	  					<MenuPanel onClick={(t) => this.handleOnClick(t)}/>
					</Col>
					<Col md={8} componentClass="span">
	 					 {this.state.rightPanel}
					</Col>
				</Row>
			</Grid>
		)
	}
}

export default PanelContainer;