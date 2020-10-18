import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import api from "../../api";


export const getProduct = createAsyncThunk("GET_PRODUCT", async productId => {
    let response = await api.get("http://localhost:8080/products/" + productId);
    return response.data
});

export const findProducts = createAsyncThunk("FIND_PRODUCT", async filters => {
    let url = "http://localhost:8080/products?" + Object.entries(filters).map((name, value) => name + "=" + value).join("&");
    let response = await api.get(url);
    return response.data
});

export const productSlice = createSlice({
    name: 'products',
    initialState: { search: [], byId: {} },
    reducers: {
        // standard reducer logic, with auto-generated action types per reducer
    },
    extraReducers: {
        [getProduct.fulfilled]: (state, action) => {
            state.byId[action.payload.id] = action.payload
        },
        [findProducts.fulfilled]: (state, action) => {
            for (const product of action.payload) {
                state.byId[product.id] = product
            }

            state.search = action.payload
        }
    }
})


