import {configureStore} from "@reduxjs/toolkit";
import productSlice from "./product-slice";
import uiSlice from "./ui-slice";
import userSlice from "./user-slice";

const store = configureStore({
  reducer: {
    product: productSlice.reducer,
    ui: uiSlice.reducer,
    user: userSlice.reducer
  }
});

export default store;