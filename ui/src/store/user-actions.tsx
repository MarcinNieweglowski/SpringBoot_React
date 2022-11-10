import {User} from "../types/User";
import {uiActions} from "./ui-slice";
import {NotificationStatus} from "./NotificationStatus";
import {userActions} from "./user-slice";
import {sendRequest} from "../utils/Auth";

export const logIn: any = (user: User) => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Logging in...',
        message: 'Attempting to log in'
      })
    );

    try {
      const response: any = await sendRequest(dispatch, '/auth/login', 'POST', user, user);
      if (!response.ok) {
        throw Error("Invalid credentials")
      }

      const loggedInUser: User = await response.json();
      dispatch(userActions.logIn(loggedInUser));

      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'Logged in',
          message: 'Successfully logged in'
        })
      );
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Invalid credentials!',
          message: 'Failed to log in'
        })
      );
    }
  }
}

export const logOut: any = () => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Logging out...',
        message: 'Attempting to log out'
      })
    );

    dispatch(userActions.logOut(null));

    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.SUCCESS,
        title: 'Logged out',
        message: 'Successfully logged out'
      })
    );
  }
}

export const createAccount: any = (user: User) => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Signing in...',
        message: `Creating a new user ${user.username}`
      })
    );

    try {
      await sendRequest(dispatch, '/auth/create-account', 'POST', user, user);

      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'Created a new account',
          message: 'Successfully created a new account'
        })
      );
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Error!',
          message: 'Failed to create a new account'
        })
      );
    }
  }
}

export const fetchAllUsers: any = (user: User) => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Fetching...',
        message: 'Fetching all users...'
      })
    );

    try {
      const response = await sendRequest(dispatch, '/user', 'GET', user, user);
      const responseData = await response.json();
      dispatch(userActions.fetchAllUsers(responseData));

      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'Fetched all users',
          message: 'Successfully fetched all users'
        })
      );
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Error!',
          message: 'Failed to fetch all users'
        })
      );
    }
  }
}

export const deleteUser: any = (user: User, userToDelete: User) => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Deleting user...',
        message: 'Deleting user...'
      })
    );

    try {
      const response = await sendRequest(dispatch, `/user/${userToDelete.userId}`, 'DELETE', user, userToDelete);
      const responseData = await response.json();
      dispatch(userActions.deleteUser(responseData));

      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'User deleted',
          message: `Successfully deleted user ${userToDelete.username}`
        })
      );
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Error!',
          message: `Failed to delete user ${userToDelete.username}`
        })
      );
    }
  }
}

export const editUser: any = (loggedInUser: User, userToUpdate: User) => {
  return async (dispatch: any) => {
    dispatch(
      uiActions.showNotification({
        status: NotificationStatus.PENDING,
        title: 'Updating user...',
        message: 'Updating user...'
      })
    );

    try {
      const response = await sendRequest(dispatch, `/user`, 'PUT', loggedInUser, userToUpdate);
      const responseData = await response.json();
      dispatch(userActions.editUser(responseData));

      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.SUCCESS,
          title: 'User updated',
          message: `Successfully updated user ${userToUpdate.username}`
        })
      );
    } catch (error) {
      dispatch(
        uiActions.showNotification({
          status: NotificationStatus.ERROR,
          title: 'Error!',
          message: `Failed to update user ${userToUpdate.username}`
        })
      );
    }
  }
}