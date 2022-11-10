import {createSlice} from "@reduxjs/toolkit";
import {Product} from "../types/Product";
import {OperationType} from "../types/OperationType";

export type InitialProductState = {
  products: Product[],
  totalPrice: number
}

const initialState = {
  products: [],
  totalPrice: 0
} as InitialProductState;

const productSlice = createSlice({
  name: 'product',
  initialState: initialState,
  reducers: {
    populateProductList(state, action) {
      state.products = action.payload.products;
      state.totalPrice = action.payload.totalPrice;
    },
    addProduct(state, action) {
      const product: Product = action.payload;
      const existingProduct = state.products.find(existingProduct => existingProduct.productId === product.productId);

      if (existingProduct) {
        return;
      }

      state.totalPrice = state.totalPrice + product.price * product.quantity;
      const products: Array<Product> = state.products;
      products.push(product);
      state.products = products;
    },
    removeProduct(state, action) {
      const productToBeRemoved: Product = action.payload;
      const existingProduct = state.products.find(existingProduct => existingProduct.productId === productToBeRemoved.productId) as Product;

      state.totalPrice = state.totalPrice - productToBeRemoved.price;

      if (existingProduct.quantity > 1) {
        existingProduct.quantity--;
      } else {
        state.products = state.products.filter(existingProduct => existingProduct.productId !== productToBeRemoved.productId);
      }
    },
    editProduct(state, action) {
      const updatedProduct: Product = action.payload;
      let existingProduct = state.products.find(existingProduct => existingProduct.productId === updatedProduct.productId);

      if (!existingProduct) {
        return;
      }

      existingProduct.price = updatedProduct.price;
      existingProduct.quantity = updatedProduct.quantity;

      state.totalPrice = state.products
        .map(p => p.quantity * p.price)
        .reduce((previous, current) => previous + current, 0);
    },
    deleteProduct(state, action) {
      const productToBeRemoved: Product = action.payload;
      state.products = state.products.filter(existingProduct => existingProduct.productId !== productToBeRemoved.productId);

      state.totalPrice = state.products
        .map(p => p.quantity * p.price)
        .reduce((previous, current) => previous + current, 0);
    }
  }
});

export const productActions = productSlice.actions;

export default productSlice;