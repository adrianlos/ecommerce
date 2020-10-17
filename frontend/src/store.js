import { configureStore } from '@reduxjs/toolkit';
import { productSlice } from './customer/products/redux';


const store = configureStore({
    reducer: {
        // auth:
        product: productSlice.reducer
    }
});

export default store;

