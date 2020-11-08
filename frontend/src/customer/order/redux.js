import {createSlice, createAction} from "@reduxjs/toolkit";


export const updateDeliveryAddress = createAction("UPDATE_DELIVERY_ADDRESS");

export const updateCustomerAddress = createAction("UPDATE_CUSTOMER_ADDRESS");

export const confirmOrder = createAction("CONFIRM_ORDER");

export const cancelOrder = createAction("CANCEL_ORDER");

export const orderSlice = createSlice({
    name: 'order',
    initialState: {
        id: null,
        deliveryAddress: { street: null, city: null, zipCode: null, country: null },
        customerAddress: { street: null, city: null, zipCode: null, country: null }
    },
    reducers: {
        // standard reducer logic, with auto-generated action types per reducer
    },
    extraReducers: {
        [updateDeliveryAddress]: (state, action) => {
            if (state.id == null) {
                state.deliveryAddress = action.payload;
            }
        },
        [updateCustomerAddress]: (state, action) => {
            if (state.id == null) {
                state.customerAddress = action.payload;
            }
        },
        [confirmOrder]: (state, action) => {
            // TODO: implement
            state.id = null;
        },
        [cancelOrder]: (state, action) => {
            state.id = null;
            state.deliveryAddress = { street: null, city: null, zipCode: null, country: null };
            state.customerAddress = { street: null, city: null, zipCode: null, country: null };
        }
    }
});


export function areTheSame(firstObject, secondObject) {
    const keys1 = Object.keys(firstObject);
    const keys2 = Object.keys(secondObject);

    if (keys1.length !== keys2.length) {
        return false;
    }

    for (let key of keys1) {
        if (firstObject[key] !== secondObject[key]) {
            return false;
        }
    }

    return true;
}

export function isAddressValid(address) {
    return !!address.street && !!address.street && !!address.zipCode && !!address.country;
}