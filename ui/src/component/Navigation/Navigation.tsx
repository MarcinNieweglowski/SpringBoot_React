import React from "react";
import {NavLink, Redirect} from "react-router-dom";
import Logout from "../Logout/Logout";
import {useSelector} from "react-redux";
import {User, UserLevel} from "../../types/User";
import classes from "./Navigation.module.css";

const Navigation: React.FC = () => {
  const user: User = useSelector((state: any) => state.user.user);
  const isAdmin: boolean = user?.userLevel === UserLevel.ADMIN;

  const loggingInOptions = (
    <>
      <NavLink className="nav-link" to={"/login"}>Log in</NavLink>
      <NavLink className="nav-link" to={"/create-account"}>Create Account</NavLink>
    </>
  );

  return (
    <nav className="navbar navbar-dark navbar-fixed-top justify-content-center bg-dark">
        <div className="navbar-header">
          <NavLink className="navbar-brand" to="/">Products App</NavLink>
          {user ? <div className={classes.username}>{user.username}</div> : ''}
        </div>
        <ul className="nav navbar-nav">
          <li><NavLink className="nav-link" to="/list">List</NavLink></li>
          <li><NavLink className="nav-link" to="/add">Add</NavLink></li>
          <li><NavLink className="nav-link" to="/print">Print</NavLink></li>
          <li>{isAdmin ? <NavLink className="nav-link" to="/admin">Admin</NavLink> : ''}</li>
          <li>{user ? <Logout /> : loggingInOptions}</li>
          <li><NavLink className="nav-link" to="/*"><Redirect to={"/"}/></NavLink></li>
        </ul>
    </nav>
  )
}

export default Navigation;