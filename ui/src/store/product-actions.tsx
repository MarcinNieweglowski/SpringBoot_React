import React from "react";
import {uiActions} from "./ui-slice";
import {NotificationStatus} from "./NotificationStatus";
import {productActions} from "./product-slice";
import {Product} from "../types/Product";
import fileDownload from "js-file-download"
import {User} from "../types/User";
import {sendRequest} from "../utils/Auth";

export const fetchProductsData: any = (user: any) => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Fetching...',
        message: 'Fetching products data'
      })
    );

    const fetchProducts = async () => {
      const response = await sendRequest(dispatch, '/products', 'GET', user);

      if (!response.ok) {
        return <div>Failed to fetch products list.</div>
      }

      const responseData = await response.json() as Product;

      return responseData;
    }

    try {
      const products: any = await fetchProducts();
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'Success',
          message: 'Successfully fetched product data'
        })
      );

      const totalPrice = products
        .map((product: Product) => product.price * product.quantity)
        .reduce((total: number, current: number) => total + current, 0);

      dispatch(
        productActions.populateProductList({
          products: products || [],
          totalPrice: totalPrice
        })
      );
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Error!',
          message: 'Failed to fetch product data'
        })
      );
    }
  }
}

export const addNewProduct: any = (product: Product, user: User) => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Creating...',
        message: `Creating new product '${product.name}'`
      })
    );

    try {
      const response = await sendRequest(dispatch, '/products/new', 'POST', user, product);
      const responseData: Product = await response.json();

      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'Success',
          message: `Successfully created product '${product.name}'`
        })
      );

      dispatch(productActions.addProduct(responseData));
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Error!',
          message: 'Failed to create new product'
        })
      );
    }
  }
}

export const deleteProduct: any = (product: Product, user: User) => {
  return async (dispatch: any) => {
    const deletedProductName: string = product.name;
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Deleting...',
        message: `Deleting product '${product.name}'`
      })
    );

    try {
      await sendRequest(dispatch, `/products/${product.productId}`, 'DELETE', user, product);
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'Success',
          message: `Successfully deleted product '${deletedProductName}'`
        })
      );
      dispatch(productActions.deleteProduct(product));
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Error!',
          message: 'Failed to delete product data'
        })
      );
    }
  }
}

export const modifyProduct: any = (product: Product, user: User) => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Updating...',
        message: 'Updating product data'
      })
    );

    try {
      const response = await sendRequest(dispatch, `/products/edit/${product.productId}`, 'PUT', user, product);
      const responseData = await response.json();

      dispatch(productActions.editProduct(responseData));

      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'Success',
          message: 'Successfully edited product data'
        })
      );
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Error!',
          message: 'Failed to send new product data'
        })
      );
    }
  }
}

export const download = (user: User): any => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Printing...',
        message: 'Printing products'
      })
    );

    try {
      const response = await sendRequest(dispatch, '/products/download', 'GET', user);
      const blob = await response.blob()

      fileDownload(blob, 'products.pdf')

      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'Success',
          message: 'Successfully generated the file'
        })
      );
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Error!',
          message: 'Failed to generate the file'
        })
      );
    }
  }
}