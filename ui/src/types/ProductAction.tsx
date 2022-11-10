import {Product} from "./Product";
import {OperationType} from "./OperationType";

export type ProductAction = {
  product: Product,
  operationType: OperationType
}