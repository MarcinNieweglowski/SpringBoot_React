import {User} from "../../types/User";
import SingleUser from "./SingleUser";

const AdminList = (props: any) => {
  const users: User[] = props.users;

  const usersList = users?.map((us: User) =>
    <SingleUser key={us.userId} userId={us.userId} username={us.username} userLevel={us.userLevel} />
  );

  return (
    <ul>
      <div>{(users?.length > 0) && usersList}</div>
    </ul>
  );
}

export default AdminList;