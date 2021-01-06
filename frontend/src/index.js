import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'font-awesome/css/font-awesome.min.css'
import { BrowserRouter as Router } from 'react-router-dom';
import { positions, Provider as AlertProvider } from "react-alert";
import { Provider as ReduxProvider} from 'react-redux'
import { createStore, applyMiddleware } from 'redux'
import AlertTemplate from "react-alert-template-basic";
import promise from 'redux-promise'
import multi from 'redux-multi'
import thunk from 'redux-thunk'

import reducers from './reducers'
import * as Sentry from '@sentry/browser';

Sentry.init({dsn: "https://94deba7f12ee4a7d9f62f76bcd257262@o375605.ingest.sentry.io/5195295"});

const options = {
  timeout: 5000,
  position: positions.BOTTOM_CENTER
};

const store = applyMiddleware(thunk, multi, promise)(createStore)(reducers)

ReactDOM.render(
  <ReduxProvider store={store}>
    <AlertProvider template={AlertTemplate} {...options}>
    <React.StrictMode>
      <Router>
        <App />
      </Router>
    </React.StrictMode>
    </AlertProvider>
  </ReduxProvider>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
