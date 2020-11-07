import React, {PureComponent} from 'react';
import Input from "@material-ui/core/Input";
import RemoveIcon from '@material-ui/icons/Remove';
import Grid from "@material-ui/core/Grid";

export default class PriceRangeFilter extends PureComponent {

    constructor(props) {
        super(props);

        this._handleMinPriceChange = this._handleMinPriceChange.bind(this);
        this._handleMaxPriceChange = this._handleMaxPriceChange.bind(this);
    }

    render() {
        return (
            <Grid container direction="row" justify="space-around" alignItems="center">
                <Grid item xs={5}>
                    <Input error={this.props.minPrice < 0}
                           type="number"
                           size="small"
                           value={this.props.minPrice || 0}
                           label="From"
                           onChange={this._handleMinPriceChange}
                           margin="dense" />
                </Grid>
                <Grid item xs={2}><RemoveIcon /></Grid>
                <Grid item xs={5}>
                    <Input error={this.props.minPrice > this.props.maxPrice}
                           type="number"
                           size="small"
                           value={this.props.maxPrice}
                           label="To"
                           onChange={this._handleMaxPriceChange} />
                </Grid>
            </Grid>
        );
    }

    _handleMinPriceChange(event) {
        if (event.target == null) {
            return;
        }

        const value = parseInt(event.target.value);

        this.props.onChange({
            minPrice: value > 0 ? value: 0,
            maxPrice: this.props.maxPrice,
        })
    }

    _handleMaxPriceChange(event) {
        if (event.target == null) {
            return;
        }

        const value = parseInt(event.target.value);

        this.setState({
            maxPrice: value,
            minPrice: this.props.minPrice
        });
    }
}