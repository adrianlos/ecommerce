import React from 'react';
import Select from '@material-ui/core/Select'
import MenuItem from "@material-ui/core/MenuItem";

export default function ProductsSortOrderSwitch(props) {
    return (
            <Select fullWidth
                    displayEmpty
                    value={props.value || null}
                    onChange={props.onChange}
                    inputProps={{
                        name: 'sort-order',
                        id: 'products-sort-order-switch',
                    }}>
                <MenuItem value={null}>bez sortowania</MenuItem>
                <MenuItem value={"price!asc"}>po cenie: rosnąco</MenuItem>
                <MenuItem value={"price!desc"}>po cenie: malejąco</MenuItem>
                <MenuItem value={"title!asc"}>po nazwie: rosnąco</MenuItem>
                <MenuItem value={"title!desc"}>po nazwie: malejąco</MenuItem>
            </Select>
    );
}