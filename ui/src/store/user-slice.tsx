import {createSlice} from "@reduxjs/toolkit";
import {User} from "../types/User";

export type InitialUserState = {
  user: User | null,
  users: Array<User> | null
}

const initialState = {
  user: null,
  users: null
} as InitialUserState;

const userSlice = createSlice({
  name: 'user',
  initialState: initialState,
  reducers: {
    logIn(state, action) {
      const user = action.payload as User;
      state.user = user
    },
    logOut(state, action) {
      state.user = null;
      state.users = null;
    },
    signIn(state, action) {

    },
    fetchAllUsers(state, action) {
      const users = action.payload as Array<User>;
      state.users = users;
    },
    editUser(state, action) {
      const userToEdit = action.payload as User;
      const x = state.users?.filter(user => user.userId === userToEdit.userId) || null

    },
    deleteUser(state, action) {
      const userToDelete = action.payload as User;
      state.users = state.users?.filter(user => user.userId !== userToDelete.userId) || null
    }
  }
});

export const userActions = userSlice.actions;

export default userSlice;