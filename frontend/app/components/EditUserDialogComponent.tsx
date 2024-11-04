import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { User } from "./UsersTable"

interface EditUserDialogProps {
  isOpen: boolean
  onOpenChange: (open: boolean) => void
  user: User | null
  onSubmit: (user: User) => void
}

export function EditUserDialog({ isOpen, onOpenChange, user, onSubmit }: EditUserDialogProps) {
  const [editingUser, setEditingUser] = useState<User | null>(null)

  useEffect(() => {
    setEditingUser(user)
  }, [user])

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault()
    if (editingUser) {
      onSubmit(editingUser)
    }
  }

  return (
    <Dialog open={isOpen} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Edit User</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit}>
          <div className="grid gap-4 py-4">
            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="firstName" className="text-right">
                First Name
              </Label>
              <Input
                id="firstName"
                value={editingUser?.firstName || ''}
                onChange={(e) => setEditingUser(prev => prev ? { ...prev, firstName: e.target.value } : null)}
                className="col-span-3"
              />
            </div>
            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="lastName" className="text-right">
                Last Name
              </Label>
              <Input
                id="lastName"
                value={editingUser?.lastName || ''}
                onChange={(e) => setEditingUser(prev => prev ? { ...prev, lastName: e.target.value } : null)}
                className="col-span-3"
              />
            </div>
            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="email" className="text-right">
                Email
              </Label>
              <Input
                id="email"
                type="email"
                value={editingUser?.email || ''}
                onChange={(e) => setEditingUser(prev => prev ? { ...prev, email: e.target.value } : null)}
                className="col-span-3"
              />
            </div>
          </div>
          <DialogFooter>
            <Button type="submit">Save changes</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  )
}