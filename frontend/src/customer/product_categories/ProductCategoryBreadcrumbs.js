import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Breadcrumbs, Typography } from '@material-ui/core';
import { getAllProductCategories } from "./redux";

class ProductCategoryTree extends Component {

    componentDidMount() {
        this.props.getData()
    }

    render() {
        return (
            <Breadcrumbs aria-label="breadcrumb">
                <Typography color="textPrimary">Cały asortyment</Typography>
                {this.props.nodes.map(node => <Typography key={node.id} color="textPrimary">{node.name}</Typography>)}
            </Breadcrumbs>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    let categories = [];
    let currentCategory = state.product_categories.byId[ownProps.categoryId];

    while (currentCategory != null) {
        categories.unshift(currentCategory);
        currentCategory = state.product_categories.byId[currentCategory.parentId];
    }

    return {
        nodes: categories
    }
}

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        getData: () => dispatch(getAllProductCategories())
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(ProductCategoryTree)