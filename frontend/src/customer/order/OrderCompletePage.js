import React, {Component} from 'react'
import { TextField } from '@material-ui/core';
import Address from "./Address";
import Paper from "@material-ui/core/Paper";
import {prefetchProduct} from "../products/redux";
import {addToBasket, dropBasket, removeFromBasket} from "../basket/redux";
import {connect} from "react-redux";


class OrderCompletePage extends Component {

    constructor(props) {
        super(props);

        for (const product of props.basket) {
            props.prefetchProduct(product.id);
        }
    }

    render() {
        return (
            <Paper>
                <h1>Summary</h1>
            </Paper>
        );
    }
}


const mapStateToProps = (state, ownProps) => {
    return {
        basket: Object.entries(state.basket).map(entry => ({
            id: entry[0],
            count: entry[1],
            ...state.products.byId[entry[0]]
        })),
        deliveryAddress: state.order.deliveryAddress,
        customerAddress: state.order.customerAddress,
    }
}

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        prefetchProduct: (productId) => dispatch(prefetchProduct(productId)),
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(OrderCompletePage)