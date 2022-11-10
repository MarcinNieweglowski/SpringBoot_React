import {User} from "./User";

export type Product = {
  productId?: number,
  name: string,
  price: number,
  quantity: number,
  user?: User
}