import React, { Component } from 'react';
import './App.css';
import SearchOrders from './searchorders/searchorders.js';

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <h1 className="App-title">订单查询</h1>
        </header>
        <SearchOrders />
      </div>
    );
  }
}

export default App;