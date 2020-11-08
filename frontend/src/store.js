import { configureStore } from '@reduxjs/toolkit';
import { productSlice } from './customer/products/redux';
import { productCategorySlice } from './customer/product_categories/redux';
import { basketSlice } from './customer/basket/redux';
import { orderSlice } from "./customer/order/redux";


const store = configureStore({
    reducer: {
        // auth:
        products: productSlice.reducer,
        product_categories: productCategorySlice.reducer,
        basket: basketSlice.reducer,
        order: orderSlice.reducer
    }
});

store.subscribe(() => {
    const { basket } = store.getState();

    // TODO: improve performance
    localStorage.setItem("basket", JSON.stringify(basket));
});

export default store;

