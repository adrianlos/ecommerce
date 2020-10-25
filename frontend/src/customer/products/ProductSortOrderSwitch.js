import React from 'react';
import Select from '@material-ui/core/Select'
import MenuItem from "@material-ui/core/MenuItem";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";

export default function ProductsSortOrderSwitch(props) {
    return (
        <FormControl>
            <InputLabel htmlFor="products-sort-order-switch">Sortowanie</InputLabel>
            <Select value={props.value}
                    onChange={props.onChange}
                    inputProps={{
                        name: 'sort-order',
                        id: 'products-sort-order-switch',
                    }}>
                <MenuItem value={"price!asc"}>po cenie: rosnąco</MenuItem>
                <MenuItem value={"price!desc"}>po cenie: malejąco</MenuItem>
                <MenuItem value={"title!asc"}>po nazwie: rosnąco</MenuItem>
                <MenuItem value={"title!desc"}>po nazwie: malejąco</MenuItem>
            </Select>
        </FormControl>
    );
}