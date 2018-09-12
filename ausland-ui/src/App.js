import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom'
import Main from './layout/main.js'
import SearchOrders from './searchorders/searchorders.js';
import SearchStock from './searchstock/searchstock.js';
import HistoryOrder from './searchorders/historyorders.js';


class App extends Component {
  render() {
    return (
    	<Main>
	      <Switch>
		      <Route exact path="/ui" component={SearchStock}/>
					<Route path="/ui/order" component={SearchOrders}/>
					<Route path="/ui/history" component={HistoryOrder}/>
					<Route path="/ui/stock" component={SearchStock}/>
		    </Switch>
	    </Main>
    );
  }
}

export default App;
