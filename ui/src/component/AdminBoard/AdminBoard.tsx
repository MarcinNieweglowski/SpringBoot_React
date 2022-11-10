import {useDispatch, useSelector} from "react-redux";
import React, {useEffect, useState} from "react";
import {fetchAllUsers} from "../../store/user-actions";
import {User, UserLevel} from "../../types/User";
import AdminList from "./AdminList";
import classes from "./Admin.module.css";

const AdminBoard = () => {
  const user = useSelector((state: any) => state.user);
  const dispatch = useDispatch();
  const [filteredUsers, setFilteredUsers] = useState<Array<User>>([]);
  const [searchUser, setSearchUser] = useState('');
  const [users, setUsers] = useState<Array<User>>([user.users]);

  useEffect(() => {
    if (user?.user?.userLevel === UserLevel.ADMIN) {
      dispatch(fetchAllUsers(user.user));
    }
  }, [dispatch]);

  useEffect(() => {
    if(searchUser && filteredUsers) {
      setUsers(filteredUsers);
    } else {
      setUsers(user.users)
    }
  }, [searchUser, filteredUsers, user.users]);

  const onFilterHandler = (event: any) => {
    const enteredValue: string = event.target.value.toLowerCase();
    setSearchUser(enteredValue);

    if (!enteredValue) {
      setFilteredUsers([]);
      return;
    }

    const filtered = user.users
      .filter((user: User) => user.username?.toLowerCase().includes(enteredValue));

    setFilteredUsers(filtered)
  }

  return (
    <div>
      <div className={classes.welcome}>Welcome to Admin Board</div>
      <hr/>
      <div className="row">
        <div className="col-md-6">Search by name:</div>
        <input className="col-md-3 rounded" type="text" onChange={onFilterHandler} placeholder="Username"/>
      </div>
      {user?.users && <AdminList users={users} />}
      {(!user || !user.users) && <div>Fetching list of users...</div>}
      <hr/>
    </div>
  )
}

export default AdminBoard;