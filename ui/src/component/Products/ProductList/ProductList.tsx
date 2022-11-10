import {Product} from "../../../types/Product";
import ProductItem from "../ProductItem/ProductItem";
import React, {useState} from "react";
import {useSelector} from "react-redux";

const ProductList = (props: any) => {
  const user = useSelector((state: any) => state.user.user);
  const { products } = props;

  const productList = products.map((product: Product) =>
    <ProductItem
      key={product.productId}
      productId={product.productId}
      name={product.name}
      price={product.price}
      quantity={product.quantity}
      user={user}
    />
  );

  return (
    <div>
      <div className="row">
        <ul>{productList}</ul>
        <hr />
      </div>
    </div>
  )
}

export default ProductList;