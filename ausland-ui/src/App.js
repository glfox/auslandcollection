import React, { Component } from 'react';
import './App.css';
import PanelContainer from './layout/panelcontainer.js';
import { hasUserLogin } from './utils/loginstatus.js' 

class App extends Component {
  render() {
  	const PanelWithLogin = hasUserLogin(PanelContainer)

    return (
      <div className="App">
        <PanelWithLogin />
      </div>
    );
  }
}

export default App;
