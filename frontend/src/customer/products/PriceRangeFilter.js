import React, {Component} from 'react';
import TextField from "@material-ui/core/TextField";
import Input from "@material-ui/core/Input";

export default class PriceRangeFilter extends Component {

    constructor(props) {
        super(props);

        this.state = {
            minPrice: props.minPrice,
            maxPrice: props.maxPrice
        }

        this._handleMinPriceChange = this._handleMinPriceChange.bind(this);
        this._handleMaxPriceChange = this._handleMaxPriceChange.bind(this);
    }

    render() {
        return (
            <form>
                <Input type="number" value={this.state.minPrice} label="From" onChange={this._handleMinPriceChange} />
                <Input type="number" value={this.state.maxPrice} label="To" onChange={this._handleMaxPriceChange} />
            </form>
        );
    }

    _handleMinPriceChange(event) {
        if (event.target == null) {
            return;
        }

        const value = event.target.value;

        this.setState((previousState) => ({
            minPrice: value
        }));
    }

    _handleMaxPriceChange(event) {
        const value = event.target != null ? event.target.value : this.state.maxPrice;

        this.setState({
            maxPrice: value,
            minPrice: this.state.minPrice
        });
    }
}