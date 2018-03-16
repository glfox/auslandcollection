import React from 'react';
import { Tab, Row, Col, Nav, NavItem } from 'react-bootstrap';

class MenuPanel extends React.Component {

	constructor(props) {
		super(props);
		this.state = {
			onClick: props.onClick
		}
	}

	render() {
		return (
			<span>
				<Tab.Container id="search-table" defaultActiveKey="order">
					 <Row className="clearfix">
					    <Col>
					      	<Nav bsStyle="pills" stacked>
					        	<NavItem eventKey="order" onSelect={() => this.state.onClick("order")}>订单查询</NavItem>
					        	<NavItem eventKey="stock" onSelect={() => this.state.onClick("stock")}>库存查询</NavItem>
					      	</Nav>
					    </Col>
					  </Row>
				</Tab.Container>
			</span>
		)
	}
}

export default MenuPanel;