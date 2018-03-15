import React, { Component } from 'react';
import './App.css';
import AuslandNavBar from './layout/auslandbar.js';
import SearchStocks from './searchstock/searchstock.js';
class App extends Component {
  render() {
    return (
      <div className="App">
        <AuslandNavBar />
        <SearchStocks />
      </div>
    );
  }
}

export default App;
