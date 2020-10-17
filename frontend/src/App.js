import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import ProductsPage from "./customer/products/ProductsPage";
import ProductPage from "./customer/products/ProductPage";
import Page from "./Page";

import './App.css';

export default function App() {
  return (
      <Router>
        <div>
          <nav>
            <ul>
              <li>
                <Link to="/">Home</Link>
              </li>
              <li>
                <Link to="/products">About</Link>
              </li>
            </ul>
          </nav>

          <Switch>
            <Route exact path="/products">
              <Page><ProductsPage /></Page>
            </Route>
            <Route path="/products/:productId">
              <Page><ProductPage /></Page>
            </Route>
          </Switch>
        </div>
      </Router>
  );
}
