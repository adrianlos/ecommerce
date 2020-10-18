import React, {Component} from 'react';
import {Grid, Paper} from "@material-ui/core";
import ProductCategoryTree from "../product_categories/ProductCategoryTree";
import ProductCategoryBreadcrumbs from "../product_categories/ProductCategoryBreadcrumbs";
import ProductsGrid from "./ProductsGrid";

class ProductsPage extends Component {

    constructor(props) {
        super(props);

        this.state = {
            page: {
                current: 0,
                size: 20,
                count: 0
            },
            filters: {
                name: null,
                type: null,
                categoryId: null,
                minPrice: null,
                maxPrice: null
            },
            sortOrder: {
                name: null,
                type: null,
                price: null
            }
        }

        this.changeCategory = this.changeCategory.bind(this);
    }

    render() {
        return (
            <Paper style={{padding: "1em"}}>
                <Grid container spacing={1}>
                    <Grid item xs={12}><ProductCategoryBreadcrumbs categoryId={this.state.filters.categoryId}/></Grid>
                    <Grid item xs={2}><ProductCategoryTree selected={this.state.filters.categoryId} onCategorySelect={this.changeCategory}/></Grid>
                    <Grid item xs={10}><ProductsGrid {...this.state.filters}/></Grid>
                </Grid>
            </Paper>
        );
    }

    changeCategory(categoryId) {
        this.setState({filters: {categoryId: categoryId, ...this.state.filters}, ...this.state});
    }
}

export default ProductsPage;
