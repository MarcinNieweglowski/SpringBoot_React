export type User = {
  userId?: number,
  username: string,
  password: string,
  userLevel?: UserLevel,
  token?: string
}

export enum UserLevel {
  USER = 'USER',
  ADMIN = 'ADMIN'
}