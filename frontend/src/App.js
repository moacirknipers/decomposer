import React from 'react';
import './App.css';
import Sidebar from './components/sidebar';
import ManageVariables from './pages/manage-variables';

import {
  Switch,
  Route,
} from 'react-router-dom';
import ListVariables from './pages/list-variables';
import CalculationTypes from './pages/calculation-type';
import SimulateFormulas from './pages/simulate-formulas';
import ManageFormulas from './pages/manage-formulas';

function App() {
  return (
    <div className="container-body" id="main">
      <div className="side">
          <Sidebar />
      </div>
      <div className="header"></div>
      <div className="main">
        <div className="container">
          <Switch>
            <Route path='/' component={ManageVariables} exact />
            <Route path='/variables/:variableId?' component={ManageVariables}/>
            <Route path='/list' component={ListVariables}/>
            <Route path='/config-formulas/:formulaId?' component={ManageFormulas} />
            <Route path='/calc' component={CalculationTypes} />
            <Route path='/simulate-formulas' component={SimulateFormulas} />
          </Switch>
        </div>
      </div>
    </div>
  );
}

export default App;
