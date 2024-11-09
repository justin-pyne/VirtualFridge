"use client"

import { signIn, signOut } from "next-auth/react"
import { Button } from "@/components/ui/button"

export function AuthButton(props: { type: string }) {
    return (
        <>
            {props.type === "signout" ? (
                <Button onClick={() => signOut()}>
                    Sign out
                </Button>
            ) : (
                <Button onClick={() => signIn("google", { callbackUrl: "/admin" })}>
                    Sign in with Google
                </Button>
            )}
        </>
    );
}
