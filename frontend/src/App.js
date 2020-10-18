import React from 'react';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link as RouterLink
} from "react-router-dom";
import ProductsPage from "./customer/products/ProductsPage";
import ProductPage from "./customer/products/ProductPage";
import Page from "./Page";
import {Grid, Typography, Divider, Toolbar, Link, CssBaseline, Container} from "@material-ui/core";

import './App.css';

const sections = [
  { title: "Kategoria 1", url: "http://google.com" },
  { title: "Kategoria 2", url: "http://google.com" },
  { title: "Kategoria 3", url: "http://google.com" },
  { title: "Kategoria 4", url: "http://google.com" },
  { title: "Kategoria 5", url: "http://google.com" },
  { title: "Kategoria 6", url: "http://google.com" }
];

export default function App() {
  return (
      <Router>
        <CssBaseline />
        <Container maxWidth="lg">
          <main>
              <Grid item xs={12} md={8}>
                  <Typography variant="h6" gutterBottom>Ecommerce SDA Project</Typography>
                  <Divider />
                  <Toolbar component="nav" variant="dense">
                      {sections.map((section) => (
                          <RouterLink to={section.url}><Link
                              color="inherit"
                              noWrap
                              key={section.title}
                              variant="body2"
                              href={section.url}
                          >
                              {section.title}
                          </Link></RouterLink>
                      ))}
                  </Toolbar>
              </Grid>
            <Switch>
              <Route exact path="/products">
                <Page><ProductsPage /></Page>
              </Route>
              <Route path="/products/:productId">
                <Page><ProductPage /></Page>
              </Route>
            </Switch>
          </main>
            <footer>
                    <Typography variant="h6" align="center" gutterBottom>
                        Stopka
                    </Typography>
                    {/*<Typography variant="subtitle1" align="center" color="textSecondary" component="p">*/}
                    {/*    {description}*/}
                    {/*</Typography>*/}
                    {/*<Copyright />*/}
            </footer>
        </Container>
      </Router>
  );
}
