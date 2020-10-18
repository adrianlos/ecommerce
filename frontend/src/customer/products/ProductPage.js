import React, { Component } from 'react';
import { connect } from 'react-redux';
import { getProduct } from './redux';
import {Typography, Divider, CssBaseline, Paper} from "@material-ui/core";
import Price from "./Price";

class ProductPage extends Component {

    componentDidMount() {
        this.props.getData(this.props.productId)
    }

    render() {
        return (
            <Paper style={{ padding: "1em" }}>
                <Typography variant="h4" gutterBottom>{this.props.title}</Typography>
                <Price value={this.props.price} />
                <img src={this.props.thumbnailUrl} style={{ maxWidth: "100%", maxHeight: "100%" }}/>
                <Typography variant="body1">{this.props.description}</Typography>
            </Paper>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    return state.products.byId[ownProps.productId] || { productId: ownProps.productId }
}

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        getData: productId => dispatch(getProduct(productId))
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(ProductPage)