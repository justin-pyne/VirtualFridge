import { type Session } from "next-auth"

export type User = {
  name: string
  email: string
}

export function checkAuth(session: Session | null): User | false {
  if (!session)
    return false
  const { user } = session
  if (!user)
    return false
  if (!user.email || !user.name)
    return false
  return {
    name: user.name,
    email: user.email
  }
}

export function isAdmin(session: Session | null) {
  if (!checkAuth(session)) {
    return false
  }

  if (process.env.ADMINS?.includes(session!.user!.email!)) {
    return true
  }
  return false
}