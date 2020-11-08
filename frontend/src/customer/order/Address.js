import React, { Component } from 'react'
import { TextField } from '@material-ui/core';
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Typography from "@material-ui/core/Typography";


export default class Address extends Component {

    constructor(props) {
        super(props);

        this.state = {
            street: (props.address || props).street,
            city: (props.address || props).city,
            zipCode: (props.address || props).zipCode,
            country: (props.address || props).country
        }

        this.updateStreet = this.updateStreet.bind(this);
        this.updateCity = this.updateCity.bind(this);
        this.updateZipCode = this.updateZipCode.bind(this);
        this.updateCountry = this.updateCountry.bind(this);
    }

    render() {
        return (
            <div>
                {this.props.label != null ?
                    <Typography variant="h6" gutterBottom>{this.props.label}</Typography> :
                    null}
                <form noValidate autoComplete="on" style={{margin: "1em"}}>
                    <Grid container spacing={1}>
                        <Grid item xs={12}>
                            <TextField fullWidth
                                       error={!this.state.street}
                                       label="Street"
                                       value={this.state.street}
                                       onChange={this.updateStreet} />
                        </Grid>
                        <Grid item xs={3}>
                            <TextField fullWidth
                                       error={!this.state.zipCode}
                                       label="Zip code"
                                       value={this.state.zipCode}
                                       onChange={this.updateZipCode} />
                        </Grid>
                        <Grid item xs={9}>
                            <TextField fullWidth
                                       error={!this.state.city}
                                       label="City"
                                       value={this.state.city}
                                       onChange={this.updateCity} />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField fullWidth
                                       error={!this.state.country}
                                       label="Country"
                                       value={this.state.country}
                                       onChange={this.updateCountry} />
                        </Grid>
                    </Grid>
                </form>
            </div>
        );
    }

    updateStreet(event) {
        const street = event.target.value;
        const newState = { ...this.state };
        newState.street = street;
        this.setState(newState);
        this.props.onChange(newState);
    }

    updateCity(event) {
        const city = event.target.value;
        const newState = { ...this.state };
        newState.city = city;
        this.setState(newState);
        this.props.onChange(newState);
    }

    updateZipCode(event) {
        const zipCode = event.target.value;
        const newState = { ...this.state };
        newState.zipCode = zipCode;
        this.setState(newState);
        this.props.onChange(newState);
    }

    updateCountry(event) {
        const country = event.target.value;
        const newState = { ...this.state };
        newState.country = country;
        this.setState(newState);
        this.props.onChange(newState);
    }
}