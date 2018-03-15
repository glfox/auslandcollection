import React from 'react';
import { Panel, ListGroup, ListGroupItem } from 'react-bootstrap';

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
				<Panel>
					<ListGroup>
					   	<ListGroupItem onClick={() => this.state.onClick("order")}>订单查询</ListGroupItem>
					    <ListGroupItem onClick={() => this.state.onClick("stock")}>库存查询</ListGroupItem>
					</ListGroup>
				</Panel>
			</span>
		)
	}
}

export default MenuPanel;