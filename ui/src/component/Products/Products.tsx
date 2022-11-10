import React, {useEffect, useState} from "react";
import ProductList from "./ProductList/ProductList";
import {fetchProductsData} from "../../store/product-actions";
import {useDispatch, useSelector} from "react-redux";
import AddProduct from "./AddProduct/AddProduct";
import classes from "./Products.module.css";
import {Product} from "../../types/Product";

const Products: React.FC = () => {
  const productsState = useSelector((state: any) => state.product);
  const user = useSelector((state: any) => state.user.user);
  const dispatch = useDispatch();
  const [products, setProducts] = useState<Array<Product>>(productsState.products);
  const [searchText, setSearchText] = useState('');
  const [filteredList, setFilteredList] = useState<Array<Product>>([]);

  useEffect(() => {
    if (user) {
      dispatch(fetchProductsData(user));
    }
  }, [dispatch, user]);

  useEffect(() => {
    if(searchText && filteredList) {
      setProducts(filteredList);
    } else {
      setProducts(productsState.products)
    }
  }, [searchText, filteredList, productsState.products]);

  const onFilterHandler = (event: any) => {
    const enteredValue: string = event.target.value.toLowerCase();
    setSearchText(enteredValue);

    if (!enteredValue) {
      setFilteredList([]);
      return;
    }

    const filtered = products
      .filter((product: Product) => product.name?.toLowerCase().includes(enteredValue));

    setFilteredList(filtered)
  }

  return (
    <>
      { products.length > 0
      ? (<>
          <h1>List of all products</h1>
          <div className="row">
            <div className="col-md-6">Search by name:</div>
            <input className="col-md-3 rounded" type="text" onChange={onFilterHandler} placeholder="Banana"/>
          </div>
          <ProductList products={products} />
          <div className={classes.text}><span className={classes.total}>Total Price: {productsState.totalPrice.toFixed(2)}</span></div>
        </>)
      : (<AddProduct/>)
      }
    </>
  )
}

export default Products;