import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import api from "../../api";


export const getProduct = createAsyncThunk("GET_PRODUCT", async productId => {
    let response = await api.get("http://localhost:8080/products/" + productId);
    return response.data
});

export const productSlice = createSlice({
    name: 'products',
    initialState: { },
    reducers: {
        // standard reducer logic, with auto-generated action types per reducer
    },
    extraReducers: {
        [getProduct.fulfilled]: (state, action) => {
            state[action.payload.id] = action.payload
        }
    }
})


