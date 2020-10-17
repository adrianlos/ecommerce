import { createAction, createAsyncThunk } from '@reduxjs/toolkit'
import api from "../../api";

export const getProduct = createAsyncThunk("GET_PRODUCT", async productId => {
    let response = await api.get("http://localhost:8080/products/" + productId);
    return response.data
});
