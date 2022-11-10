import React, {useEffect, useState} from 'react';
import './App.css';
import Navigation from "./component/Navigation/Navigation";
import Products from "./component/Products/Products";
import Footer from "./component/Footer/Footer";
import AddProduct from "./component/Products/AddProduct/AddProduct";
import {Route} from "react-router-dom";
import Welcome from "./component/Welcome/Welcome";
import Notification from "./component/Notification/Notification";
import {useSelector} from "react-redux";
import Print from "./component/Print/Print";
import Login from "./component/Login/Login";
import GuardedRoute from "./utils/GuardedRoute";
import AdminBoard from "./component/AdminBoard/AdminBoard";

function App() {

  const notification = useSelector((state: any) => state.ui.notification);

  return (
    <div className="background">
      <div className="App">
        <Navigation/>
        <div className="container card jumbotron marginate">
          {notification &&
            <Notification status={notification.status} title={notification.title} message={notification.message}/>}
          <Route path="/" exact><Welcome/></Route>
          <GuardedRoute exact path="/list" component={Products} />
          <GuardedRoute exact path="/add" component={AddProduct} />
          <GuardedRoute exact path="/print" component={Print} />
          <GuardedRoute exact path="/admin" component={AdminBoard} />
          <Route path="/login"><Login btnText={"Log in"} isLogin={true}/></Route>
          <Route path="/create-account" exact><Login btnText={"Create account"} isLogin={false}/></Route>
        </div>
        <Footer/>
      </div>
    </div>
  );
}

export default App;
