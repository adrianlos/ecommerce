import React, {Component} from 'react';
import { connect } from 'react-redux';
import {Grid, Paper} from "@material-ui/core";
import {addToBasket, dropBasket, removeFromBasket} from "./redux";
import Basket from "./Basket";
import {prefetchProduct} from "../products/redux";
import IconButton from "@material-ui/core/IconButton";
import { Link as RouterLink } from "react-router-dom";
import ShoppingBasketIcon from '@material-ui/icons/ShoppingBasket';
import Button from "@material-ui/core/Button";
import ClearIcon from '@material-ui/icons/Clear';
import { Redirect } from "react-router-dom";

class BasketPage extends Component {

    constructor(props) {
        super(props);

        this.state = {
            redirectNeeded: false
        }

        this.dropBasket = this.dropBasket.bind(this);

        for (const product of props.items) {
            props.prefetchProduct(product.id);
        }
    }

    render() {
        if (this.state.redirectNeeded) {
            return <Redirect to="/" />
        }

        return (
            <Basket items={this.props.items}>
                {this.props.items.length == 0 ? null :
                <Grid container
                      spacing={2}
                      direction="row"
                      justify="space-evenly"
                      alignItems="center"
                      style={{marginTop: "1em"}}>
                    <Grid item>
                        <RouterLink to="/order/delivery" style={{textDecoration: "none"}}>
                            <Button variant="contained"
                                    color="primary"
                                    startIcon={<ShoppingBasketIcon />}>
                                Złóż zamówienie
                            </Button>
                        </RouterLink>
                    </Grid>
                    <Grid item>
                        <Button variant="contained"
                                color="secondary"
                                onClick={this.dropBasket}
                                startIcon={<ClearIcon />}>
                            Anuluj zamówienie
                        </Button>
                    </Grid>
                </Grid>}
            </Basket>
        );
    }

    dropBasket() {
        this.props.dropBasket();
        this.setState({ redirectNeeded: true });
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
        removeFromBasket: (productId, count) => dispatch(removeFromBasket({productId: productId, count: count})),
        dropBasket: () => dispatch(dropBasket())
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(BasketPage)
