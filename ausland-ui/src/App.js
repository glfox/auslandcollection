import React, { Component } from 'react';
import './App.css';
import AuslandNavBar from './layout/auslandbar.js';

class App extends Component {
  render() {
    return (
      <div className="App">
        <header className="App-header">
          <h1 className="App-title">Ausland<br/>Collection</h1>
        </header>
        <AuslandNavBar />
      </div>
    );
  }
}

export default App;
