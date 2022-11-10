import {Product} from "../../../types/Product";
import {useEffect, useState} from "react";
import classes from './AddProduct.module.css';
import {useDispatch, useSelector} from "react-redux";
import {addNewProduct} from "../../../store/product-actions";

const AddProduct = () => {
  const dispatch = useDispatch();
  const user = useSelector((state: any) => state.user.user);
  const [nameCssClass, setNameCssClass] = useState('valid');
  const [priceCssClass, setPriceCssClass] = useState('valid');
  const [quantityCssClass, setQuantityCssClass] = useState('valid');
  const [isValid, setIsValid] = useState(false);

  const [product, setProduct] = useState<Product>({
    name: "",
    quantity: 0,
    price: 0
  });

  useEffect(() => {
    setIsValid(isNameValid() && isNumberValid(product.price) && isNumberValid(product.quantity));
  }, [product])

  const onChangeNameHandler = (event: any) => {
    event.preventDefault();
    const value = event.target.value
    setProduct({...product, name: value});
    setNameCssClass(!isNameValid() ? classes.invalid : classes.valid);
  }

  const onChangePriceHandler = (event: any) => {
    let value: number = event.target.value;
    value = isNaN(value) ? 0 : value;
    setProduct({...product, price: value});

    setPriceCssClass(!value || !isNumberValid(value) ? classes.invalid : classes.valid);
  }

  const onChangeQuantityHandler = (event: any) => {
    let value: number = parseInt(event.target.value, 10);
    value = isNaN(value) ? 0 : value;
    setProduct({...product, quantity: value});

    setQuantityCssClass(!value || !isNumberValid(value) ? classes.invalid : classes.valid);
  }

  const onAddProductHandler = (event: any) => {
    event.preventDefault();
    dispatch(addNewProduct(product, user));
  }

  function isNameValid() {
    return product.name.length > 2;
  }

  function isNumberValid(num: number) {
    return num > 0;
  }

  return (
    <div style={{textAlign: "center"}}>
      <form onSubmit={onAddProductHandler}>
        <div>
          <label>Name:</label>
          <input className={`form-control ${nameCssClass}`} type="text" id="name" value={product.name} onChange={onChangeNameHandler} placeholder="Name"/>
        </div>
        <div>
          <label>Price:</label>
          <input className={`form-control ${priceCssClass}`} type="number" step="0.01" id="price" value={product.price} onChange={onChangePriceHandler} placeholder="0"/>
        </div>
        <div>
          <label>Quantity:</label>
          <input className={`form-control ${quantityCssClass}`} type="number" id="quantity" value={product.quantity} onChange={onChangeQuantityHandler} placeholder="0"/>
        </div>
        <button disabled={!isValid} type="submit" className="btn btn-primary" >Add new product </button>
      </form>
    </div>
  )
}

export default AddProduct;