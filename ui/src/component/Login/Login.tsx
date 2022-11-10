import {useState} from "react";
import {useDispatch} from "react-redux";
import {User} from "../../types/User";
import {logIn, createAccount} from "../../store/user-actions";
import {useHistory, useLocation} from "react-router-dom";
import classes from "./Login.module.css";

const Login = (props: any) => {
  const isLogin: boolean = props.isLogin;
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isValid, setIsValid] = useState(false);

  const dispatch = useDispatch();
  const history = useHistory();
  const location = useLocation();

  const onUsernameChangeHandler = (event: any) => {
    event.preventDefault();
    const value = event.target.value;

    value.length < 2 ? setUsername('') : setUsername(value);
    validateUserData();
  }

  const onPasswordChangeHandler = (event: any) => {
    event.preventDefault();
    const value = event.target.value;

    value.length < 2 ? setPassword('') : setPassword(value);
    validateUserData();
  }

  const onButtonClick = async (event: any) => {
    event.preventDefault();
    if (isValid) {
      const user: User = {username, password};
      await dispatch(isLogin ? logIn(user) : createAccount(user))
      const fallbackUrl = isLogin ? '/list' : '/';
      history.replace(location?.state || fallbackUrl);
    }

  }

  const validateUserData = () => {
    setIsValid(username.length > 0 && password.length > 0);
  }

  return (
    <div>
      <form className={classes.padded}>
        <div>
          <label>Username:</label>
          <input type='text' className="form-control" onChange={onUsernameChangeHandler} placeholder="Username"/>
        </div>
        <div>
          <label>Password:</label>
          <input type='password' className="form-control" onChange={onPasswordChangeHandler} placeholder="Password"/>
        </div>
        <button type="submit" className="btn btn-primary" disabled={!isValid} onClick={onButtonClick}>{props.btnText}</button>
      </form>
    </div>
  );
}

export default Login;