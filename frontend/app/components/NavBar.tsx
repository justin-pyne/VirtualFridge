'use client'

import { useState } from "react"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Button } from "@/components/ui/button"
import { LogOut, LogIn } from "lucide-react"

export default function NavBar() {
  const [isLoggedIn, setIsLoggedIn] = useState(true)

  const handleLogout = () => {
    setIsLoggedIn(false)
  }

  const handleLogin = () => {
    setIsLoggedIn(true)
  }

  return (
    <header className="border-b bg-blue-950">
      <div className="flex h-14 items-center px-4 md:px-6">
        <div className="flex items-center gap-4">
          <h1 className="text-lg font-semibold text-white">
            VirtualFridge
          </h1>
        </div>
        <div className="ml-auto flex items-center gap-4">
          {isLoggedIn ? (
            <>
              <div className="flex items-center gap-4">
                <Avatar className="h-9 w-9">
                  <AvatarImage alt="User avatar" src="/placeholder.svg" />
                  <AvatarFallback>SD</AvatarFallback>
                </Avatar>
                <div className="hidden md:block">
                  <p className="text-sm font-medium leading-none text-white">
                    Shabbir Dawoodi
                  </p>
                </div>
              </div>
              <Button
                variant="ghost"
                size="icon"
                className="text-white"
                onClick={handleLogout}
              >
                <LogOut className="h-5 w-5" />
                <span className="sr-only">Logout</span>
              </Button>
            </>
          ) : (
            <Button
              variant="ghost"
              size="sm"
              className="text-white"
              onClick={handleLogin}
            >
              <LogIn className="mr-2 h-4 w-4" />
              Login
            </Button>
          )}
        </div>
      </div>
    </header>
  )
}