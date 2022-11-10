import React from 'react';
import {Redirect, Route} from "react-router-dom";
import {useSelector} from "react-redux";
import {User, UserLevel} from "../types/User";

// @ts-ignore
const GuardedRoute = ({ component: Component, ...rest }) => {
  const user: User = useSelector((state: any) => state.user.user);

  const isAdmin = user?.userLevel === UserLevel.ADMIN && rest.path === '/admin';

  const adminOnlyRoute = (props: any) => {
    return (
      isAdmin
      ? <Component {...props}/>
      : <Redirect to={{pathname: '/admin-error'}}/>
    )
  };

  const normalRoutes = (props: any) => {
    return (
      user
        ? <Component {...props} />
        : <Redirect to={{pathname: '/login', state: props.path}}/>
    )
  };

  return (
    <Route {...rest} render={props => {
      return (
        isAdmin
        ? adminOnlyRoute(props)
        : normalRoutes(props)
      )
    }}/>
  );
}

export default GuardedRoute;