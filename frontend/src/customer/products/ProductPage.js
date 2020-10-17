import React, { Component } from 'react';
import { connect } from 'react-redux';
import { getProduct } from './redux';

class ProductPage extends Component{

    componentDidMount() {
        this.props.getData(this.props.productId)
    }

    render() {
        return (
            <div>
                <p>{this.props.title}</p>
                <p>{this.props.description}</p>
                <img src={this.props.thumbnailUrl}/>
                <p>{this.props.category == null ? "" : this.props.categoryname}</p>
                <p>{this.props.price}</p>
                <p>{this.props.type}</p>
                <p>{this.props.author == null ? "" : this.props.author.firstName + " " + this.props.author.lastName}</p>
            </div>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    return state.product[ownProps.productId] || { productId: ownProps.productId }
}

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        getData: productId => dispatch(getProduct(productId))
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(ProductPage)