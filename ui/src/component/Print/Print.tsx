import {useDispatch, useSelector} from "react-redux";
import {download} from "../../store/product-actions";
import classes from "./Print.module.css";

const Print = () => {
  const user = useSelector((state: any) => state.user.user);
  const dispatch = useDispatch();

  const printHandler = () => {
    dispatch(download(user))
  }

  return (
    <div className={classes.verticalCenter}>
      <div>Click to download a PDF with all the stored products</div>
      <button type="button" className="btn btn-lg btn-primary" onClick={printHandler}>Print products</button>
    </div>
  )
}

export default Print;