import {User} from "../types/User";
import {uiActions} from "../store/ui-slice";
import {NotificationStatus} from "../store/NotificationStatus";
import {Product} from "../types/Product";


const addAuthToken = (dispatch: any, user: User) => {
  const token = user.token;

  if (!token) {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.ERROR,
        title: 'Authorization token not found',
        message: 'You must log in to perform the action'
      })
    );
    return "";
  }

  return token;
}

export const sendRequest = async (dispatch: any, url: string, method: string, user: User, body?: Product | User) => {
  const token = addAuthToken(dispatch, user);

  const sendingData = await fetch(url, {
    method,
    body: method === 'GET' ? null : JSON.stringify(body),
    headers: {
      'Content-Type': 'application/json',
      'Authorization': token
    }
  });

  if (!sendingData.ok) {
    throw new Error('Sending data failed.');
  }

  return sendingData;
};