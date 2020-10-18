import { configureStore } from '@reduxjs/toolkit';
import { productSlice } from './customer/products/redux';
import { productCategorySlice } from './customer/product_categories/redux';


const store = configureStore({
    reducer: {
        // auth:
        products: productSlice.reducer,
        product_categories: productCategorySlice.reducer
    }
});

export default store;

