import {useState} from "react";
import {User, UserLevel} from "../../types/User";
import {useDispatch, useSelector} from "react-redux";
import {deleteUser, editUser} from "../../store/user-actions";

const SingleUser = (props: any) => {
  const [singleUser, setSingleUser] = useState<User>({
    password: "",
    token: "",
    userId: props.userId,
    userLevel: props.userLevel,
    username: props.username
  });

  const dispatch = useDispatch();
  const loggedInUser: User = useSelector((state: any) => state.user.user);

  const onUsernameChangeHandler = (event: any) => {
    const value = event.target.value;

    setSingleUser({...singleUser, username: value});
  }

  const onUserLevelChangeHandler = (event: any) => {
    const value = event.target.value;

    setSingleUser({...singleUser, userLevel: value});
  }

  const onUserUpdateHandler = () => {
    dispatch(editUser(loggedInUser, singleUser))
  }

  const onUserDeleteHandler = () => {
    dispatch(deleteUser(loggedInUser, singleUser));
  }

  return (
    <li className='row' style={{margin: "1rem"}}>
      <form>
        <div className="row" style={{padding: "1rem"}}>
          <div className="col-sm-1">{singleUser.userId}</div>
          <input type="text" className="col-sm-3 rounded" onChange={onUsernameChangeHandler} defaultValue={singleUser.username}/>
          <select className="col-sm-2 rounded" onChange={onUserLevelChangeHandler} value={singleUser.userLevel}>
            <option value={UserLevel.USER}>User</option>
            <option value={UserLevel.ADMIN}>Admin</option>
          </select>
          <div className="col-sm btn-group" role="group">
            <button className="col-sm btn btn-primary" type="button" onClick={onUserUpdateHandler}>Update</button>
            <button className="col-sm btn btn-danger" type="button" onClick={onUserDeleteHandler}>Delete</button>
          </div>
        </div>
      </form>
    < hr/>
    </li>
  );
}

export default SingleUser;