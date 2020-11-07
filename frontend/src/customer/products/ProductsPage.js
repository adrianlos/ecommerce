import React, {Component} from 'react';
import { connect } from 'react-redux';
import { ancestorsOf, getAllProductCategories } from '../product_categories/redux';
import {Grid, Paper} from "@material-ui/core";
import ProductCategoryTree from "../product_categories/ProductCategoryTree";
import ProductCategoryBreadcrumbs from "../product_categories/ProductCategoryBreadcrumbs";
import ProductsGrid from "./ProductsGrid";
import {changeOrder, changePage, changePriceRange, findProducts, updateFilterSettings} from "./redux";
import ProductsSortOrderSwitch from "./ProductSortOrderSwitch";
import PriceRangeFilter from "./PriceRangeFilter";
import Pagination from "./Pagination";
import {addToBasket} from "../basket/redux";

class ProductsPage extends Component {

    constructor(props) {
        super(props);

        this.changeSortOrder = this.changeSortOrder.bind(this);
        this.updatePriceRange = this.updatePriceRange.bind(this);

        this.props.getCategories();
        this.props.search();
    }

    render() {
        return (
            <Paper style={{padding: "1em"}}>
                <Grid container spacing={1}>
                    <Grid item xs={12}><ProductCategoryBreadcrumbs categoryId={this.props.currentCategoryId}
                                                                   nodes={this.props.ancestor_categories}/></Grid>
                    <Grid item xs={2}><ProductCategoryTree selected={this.props.currentCategoryId}
                                                           onCategorySelect={this.props.changeCategory}
                                                           nodes={this.props.categories}/></Grid>
                    <Grid item xs={10}>
                        <Grid container spacing={1} direction="row" justify="space-between" alignItems="flex-end">
                            <Grid item xs={4}><ProductsSortOrderSwitch value={this.sortOrder()} onChange={this.changeSortOrder}/></Grid>
                            <Grid item xs={4}><PriceRangeFilter minPrice={this.props.filters.min_price}
                                                                maxPrice={this.props.filters.max_price}
                                                                onChange={this.updatePriceRange}/></Grid>
                            <Grid item xs={4}>
                                <Pagination no={this.props.page.no} count={this.props.page.count}
                                            goToPreviousPage={() => this.props.changePage(this.props.page.no - 1)}
                                            goToNextPage={() => this.props.changePage(this.props.page.no + 1)}/>
                            </Grid>
                            <Grid item xs={12}>
                                <ProductsGrid products={this.props.products} addToBasket={this.props.addToBasket}/>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Paper>
        );
    }

    sortOrder() {
        if (this.props.sort_order.property) {
            return this.props.sort_order.property + "!" + (this.props.sort_order.order || "asc");
        } else {
            return null;
        }
    }

    changeSortOrder(event) {
        if (event.target.value) {
            const parts = event.target.value.split("!");
            this.props.changeSortOrder(parts[0], parts[1]);
        } else {
            this.props.changeSortOrder(null, null);
        }
    }

    updatePriceRange(priceRange) {
        this.props.changePriceRange(priceRange.minPrice, priceRange.maxPrice);
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
        changePriceRange: (minPrice, maxPrice) => {
            dispatch(changePriceRange({ minPrice: minPrice, maxPrice: maxPrice }));
            dispatch(findProducts());
        },
        addToBasket: (productId) => dispatch(addToBasket({productId: productId, count: 1}))
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        currentCategoryId: state.products.search.filters.categoryId,
        products: state.products.search.result || [],
        categories: state.product_categories.tree || [],
        ancestor_categories: ancestorsOf(state.products.search.filters.categoryId)(state.product_categories) || [],
        page: state.products.search.page || {},
        sort_order: state.products.search.sort_order || {},
        filters: state.products.search.filters || {}
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(ProductsPage)
