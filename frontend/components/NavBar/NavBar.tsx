import { auth } from "@/auth"
import { UserDropdownMenuComponent } from "@/components/UserDropdownMenuComponent"
import { AuthButton } from "@/components/AuthButton"

export default async function NavBar() {
  const session = await auth()
  return (
    <header className="border-b bg-blue-950">
      <div className="flex h-14 items-center px-4 md:px-6">
        <div className="flex items-center gap-4">
          <h1 className="text-lg font-semibold text-white">
            <a href="/">VirtualFridge</a>
          </h1>
        </div>
        <div className="ml-auto flex items-center gap-4">
          {session ? (
            <UserDropdownMenuComponent
              name={session?.user?.name}
              email={session?.user?.email}
              image={session?.user?.image}
            />
          ) : (
            <AuthButton type={"signin"} />
          )}
        </div>
      </div>
    </header>
  )
}