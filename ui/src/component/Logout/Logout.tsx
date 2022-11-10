import React from "react";
import {useDispatch} from "react-redux";
import {logOut} from "../../store/user-actions";

const Logout: React.FC = () => {
  const dispatch = useDispatch();
  const logout = () => {
    dispatch(logOut(null))
  }

  return (
    <div >
      <button className="btn btn-secondary border rounded" onClick={logout}>Logout</button>
    </div>
  )
}

export default Logout;