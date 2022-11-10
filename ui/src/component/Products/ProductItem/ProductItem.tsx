import {Product} from "../../../types/Product";
import React from "react";
import {useDispatch, useSelector} from "react-redux";
import {deleteProduct, modifyProduct} from "../../../store/product-actions";

const ProductItem: React.FC<Product> = (product: Product) => {
  const dispatch = useDispatch();
  const user = useSelector((state: any) => state.user.user);

  const onIncreaseQuantityHandler = () => {
    const updatedProduct: Product = {...product, quantity: product.quantity + 1};
    dispatch(modifyProduct(updatedProduct, user));

  }

  const onDecreaseQuantityHandler = () => {
    const updatedProduct: Product = {...product, quantity: product.quantity - 1};
    if (updatedProduct.quantity > 0) {
      dispatch(modifyProduct(updatedProduct, user));
    } else {
      dispatch(deleteProduct(updatedProduct, user));
    }
  }

  const onDeleteHandler = () => {
    dispatch(deleteProduct(product, user));
  }

  return (
    <>
      <li className='row' style={{margin: "1rem"}}>
        <div className="col-xl cell-area">{product.name}</div>
        <div className="col-xl cell-area">Price per unit: {product.price}</div>
        <div className="col-sm">Quantity: {product.quantity}</div>
        <div className="col-sm btn-group" role="group">
          <button type="button" className="btn btn-success" onClick={onIncreaseQuantityHandler}>+</button>
          <button type="button" className="btn btn-secondary" onClick={onDecreaseQuantityHandler}>-</button>
          <button type="button" className="btn btn-danger" onClick={onDeleteHandler}>Delete</button>
        </div>
      </li>
      <hr/>
    </>
  );
}

export default ProductItem;