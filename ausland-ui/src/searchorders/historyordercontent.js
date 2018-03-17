import React from 'react';
import { Panel, Table } from 'react-bootstrap';

class HistoryOrderContent extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			order: props.order
		}

		this.attrMap = new Map();
		this.attrMap.set('courierCreatedDateTime', '下单日期');
		this.attrMap.set('courierNumber', '快递单号');
		this.attrMap.set('courierChinaNumber', '国内转运单号');
		this.attrMap.set('weight', '重量');
		this.attrMap.set('customStatus', '快递状态');
		this.attrMap.set('courierCompany', '承运公司');
		this.attrMap.set('receiverName', '收件人');
		this.attrMap.set('receiverAddress', '收件地址');
		this.attrMap.set('receiverPhone', '收件人手机号码');
		this.attrMap.set('productItems', '商品')
	}

	render() {
		let content = [];
		for (let attr in this.state.order) {
		  	if (this.state.order[attr] !== "null") {
		  		content.push(
		  			<tr key={attr.toString()}>
						<td>{this.attrMap.get(attr)}</td>
						<td>{this.state.order[attr]}</td>
					</tr>
		  		);
		  	}
		}

		return (
			<Panel id="{this.order.courierNumber}" bsStyle="info">
	          	<Panel.Heading>
		            <Panel.Title toggle>
		            	<p>
		            		日期：{this.state.order.courierCreatedDateTime}，
		            		订单号：{this.state.order.courierNumber}，
		            	</p>
		            	<p className="long-text" >收件人：{this.state.order.receiverName}，货物：{this.state.order.productItems}</p>
		            </Panel.Title>
		        </Panel.Heading>
	          	<Panel.Collapse>
	            	<Panel.Body>
	              		<Table striped bordered condensed hover>
				  			<tbody>
				  				{content}
				  			</tbody>
				  		</Table>
	            	</Panel.Body>
	          	</Panel.Collapse>
	        </Panel>
		) 
	}
}

export default HistoryOrderContent;