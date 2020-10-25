import React, {Component} from 'react';
import { connect } from 'react-redux';
import { ancestorsOf, getAllProductCategories } from '../product_categories/redux';
import {Grid, Paper} from "@material-ui/core";
import ProductCategoryTree from "../product_categories/ProductCategoryTree";
import ProductCategoryBreadcrumbs from "../product_categories/ProductCategoryBreadcrumbs";
import ProductsGrid from "./ProductsGrid";
import {changeOrder, changePage, findProducts, updateFilterSettings} from "./redux";
import ProductsSortOrderSwitch from "./ProductSortOrderSwitch";
import PriceRangeFilter from "./PriceRangeFilter";
import Pagination from "./Pagination";

class ProductsPage extends Component {

    constructor(props) {
        super(props);

        this.changeSortOrder = this.changeSortOrder.bind(this);

        this.props.getCategories();
        this.props.search();
    }

    render() {
        return (
            <Paper style={{padding: "1em"}}>
                <Grid container spacing={1}>
                    <Grid item xs={12}><ProductCategoryBreadcrumbs categoryId={this.props.currentCategoryId}
                                                                   nodes={this.props.ancestor_categories}/></Grid>
                    <Grid item xs={12}><ProductsSortOrderSwitch onChange={this.changeSortOrder}/>
                    <PriceRangeFilter minPrice={0} maxPrice={9999}/>
                    <Pagination no={this.props.page.no} count={this.props.page.count}
                                goToPreviousPage={() => this.props.changePage(this.props.page.no - 1)}
                                goToNextPage={() => this.props.changePage(this.props.page.no + 1)}/>
                    </Grid>
                    <Grid item xs={2}><ProductCategoryTree selected={this.props.currentCategoryId}
                                                           onCategorySelect={this.props.changeCategory}
                                                           nodes={this.props.categories}/></Grid>
                    <Grid item xs={10}><ProductsGrid products={this.props.products}/></Grid>
                </Grid>
            </Paper>
        );
    }

    changeSortOrder(event) {
        const parts = event.target.value.split("!");
        this.props.changeSortOrder(parts[0], parts[1]);
    }
}

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        getCategories: () => dispatch(getAllProductCategories()),
        search: () => dispatch(findProducts()),
        changeCategory: (categoryId) => {
            dispatch(updateFilterSettings({ categoryId: categoryId }));
            dispatch(findProducts());
        },
        changePage: (pageNo) => {
            dispatch(changePage({ no: pageNo }));
            dispatch(findProducts());
        },
        changeSortOrder: (property, order) => {
            dispatch(changeOrder({ property: property, order: order }));
            dispatch(findProducts());
        },
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        currentCategoryId: state.products.search.filters.categoryId,
        products: state.products.search.result || [],
        categories: state.product_categories.tree || [],
        ancestor_categories: ancestorsOf(state.products.search.filters.categoryId)(state.product_categories) || [],
        page: state.products.search.page || {}
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(ProductsPage)
