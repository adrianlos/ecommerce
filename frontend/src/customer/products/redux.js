import {createSlice, createAsyncThunk, createAction} from "@reduxjs/toolkit";
import api from "../../api";


export const getProduct = createAsyncThunk("GET_PRODUCT", async productId => {
    let response = await api.get("http://localhost:8080/products/" + productId);
    return response.data
});

export const prefetchProduct = createAsyncThunk("PREFETCH_PRODUCT", async (productId, thunkAPI) => {
    if (thunkAPI.getState().products[productId]) {
        return thunkAPI.getState().products[productId];
    } else {
        return thunkAPI.dispatch(getProduct(productId));
    }
});

export const findProducts = createAsyncThunk("FIND_PRODUCTS", async (_, thunkAPI) => {
    let url = "http://localhost:8080/products?" + createProductsQueryParams(
        thunkAPI.getState().products.search.filters,
        thunkAPI.getState().products.search.page,
        thunkAPI.getState().products.search.sort_order
    );

    let response = await api.get(url);
    return response.data
});

function createProductsQueryParams(filters, pagination, sortOrder) {
    let parameters = [];
    parameters.push(...Object.entries(filters)
        .filter(([name, value]) => value != null)
        .map(([name, value]) => (findProductsFiltersTranslations[name] || name) + "=" + value))

    parameters.push(...Object.entries(pagination)
        .filter(([name, value]) => value != null)
        .map(([name, value]) => (findProductsPaginationParametersTranslations[name] || name) + "=" + value));

    if (sortOrder.property != null) {
        parameters.push("sort=" + sortOrder.property + (sortOrder.order != null ? "," + sortOrder.order : ""));
    }

    return parameters.join("&");
}

const findProductsFiltersTranslations = {
    "name": "name",
    "category_id": "categoryId",
    "type": "type",
    "min_price": "minPrice",
    "max_price": "maxPrice",
};

const findProductsPaginationParametersTranslations = {
    "no": "page",
    "size": "size"
}


export const updateFilterSettings = createAction("UPDATE_FILTER_SETTINGS")

export const changePage = createAction("UPDATE_PAGE_SETTINGS")

export const changeOrder = createAction("CHANGE_ORDER")

export const changePriceRange = createAction("CHANGE_PRICE_RANGE")

export const productSlice = createSlice({
    name: 'products',
    initialState: {
        search: {
            filters: {
                name: null,
                type: null,
                categoryId: null,
                min_price: null,
                max_price: null,
            },
            sort_order: {
                property: null,
                order: null
            },
            result: [],
            page: { no: 0, size: 20, count: null}
        },
        byId: {}
    },
    reducers: {
        // standard reducer logic, with auto-generated action types per reducer
    },
    extraReducers: {
        [getProduct.fulfilled]: (state, action) => {
            state.byId[action.payload.id] = action.payload
        },
        [findProducts.fulfilled]: (state, action) => {
            for (const product of action.payload.content) {
                state.byId[product.id] = product
            }

            state.search.result = action.payload.content
            state.search.page = {
                no: action.payload.number,
                size: action.payload.size,
                count: action.payload.totalPages
            }
        },
        [updateFilterSettings]: (state, action) => {
            for (const filterName in action.payload) {
                state.search.filters[filterName] = action.payload[filterName]
            }
        },
        [changePage]: (state, action) => {
            if (action.payload.no != null) {
                state.search.page.no = action.payload.no
            }

            if (action.payload.size != null) {
                state.search.page.size = action.payload.size
            }

            if (action.payload.count != null) {
                state.search.page.count = action.payload.count
            }
        },
        [changeOrder]: (state, action) => {
            if (action.payload.property != null) {
                state.search.sort_order.property = action.payload.property
            }

            if (action.payload.order != null) {
                state.search.sort_order.order = action.payload.order
            }
        },
        [changePriceRange]: (state, action) => {
            state.search.filters.min_price = action.payload.minPrice;
            state.search.filters.max_price = action.payload.maxPrice;
        }
    }
})


