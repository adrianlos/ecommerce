import {createSlice, createAction} from "@reduxjs/toolkit";


export const addToBasket = createAction("ADD_TO_BASKET");

export const removeFromBasket = createAction("REMOVE_FROM_BASKET");

export const dropBasket = createAction("DROP_BASKET");

export const basketSlice = createSlice({
    name: 'basket',
    initialState: JSON.parse(localStorage.getItem("basket")) || {},
    reducers: {
        // standard reducer logic, with auto-generated action types per reducer
    },
    extraReducers: {
        [addToBasket]: (state, action) => {
            state[action.payload.productId] = (state[action.payload.productId] || 0) + action.payload.count;
        },
        [removeFromBasket]: (state, action) => {
            state[action.payload.productId] = (state[action.payload.productId] || 0) - action.payload.count;

            if (state[action.payload.productId] <= 0) {
                delete state[action.payload.productId];
            }
        },
        [dropBasket]: (state, action) => {
            for (const productId of Object.keys(state)) {
                delete state[productId];
            }
        }
    }
});
