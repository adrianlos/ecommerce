import React, {Component} from 'react';
import { connect } from 'react-redux';
import {Grid, Paper} from "@material-ui/core";
import {addToBasket, removeFromBasket} from "./redux";
import Basket from "./Basket";
import {prefetchProduct} from "../products/redux";

class BasketPage extends Component {

    constructor(props) {
        super(props);

        for (const product of props.items) {
            props.prefetchProduct(product.id);
        }
    }

    render() {
        return (<Basket items={this.props.items}/>);
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        items: Object.entries(state.basket).map(entry => ({
            id: entry[0],
            count: entry[1],
            ...state.products.byId[entry[0]]
        }))
    }
}

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        prefetchProduct: (productId) => dispatch(prefetchProduct(productId)),
        addToBasket: (productId, count) => dispatch(addToBasket({productId: productId, count: count})),
        removeFromBasket: (productId, count) => dispatch({productId: productId, count: count})
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(BasketPage)
