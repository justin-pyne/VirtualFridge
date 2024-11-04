'use client'

import { useEffect, useState } from "react"
import { UserTable } from "./UserTableComponent"
import { EditUserDialog } from "./EditUserDialogComponent"
import { DeleteUserDialog } from "./DeleteUserAlertDialog"

export type User = {
  id: string
  firstName: string
  lastName: string
  email: string
}

export default function UsersTable() {
  const [isEditDialogOpen, setIsEditDialogOpen] = useState(false)
  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false)
  const [editingUser, setEditingUser] = useState<User | null>(null)
  const [deletingUser, setDeletingUser] = useState<User | null>(null)
  const USER_API_BASE_URL = "http://localhost:8080/api/users/"
  const [users, setUsers] = useState<User[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)
      try {
        const response = await fetch(USER_API_BASE_URL, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        })
        const users = await response.json()
        setUsers(users)
      } catch (error) {
        console.error(error)
      }
      setLoading(false)
    }
    fetchData()
  }, []);

  const handleEdit = (user: User) => {
    setEditingUser(user)
    setIsEditDialogOpen(true)
  }

  const handleEditSubmit = async (updatedUser: User) => {
    try {
      const response = await fetch(`${USER_API_BASE_URL}${updatedUser.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(updatedUser),
      })

      if (response.ok) {
        const updatedUserFromServer = await response.json()
        setUsers(users.map((u) => u.id === updatedUserFromServer.id ? updatedUserFromServer : u))
      } else {
        console.error("Failed to update user")
      }
    } catch (error) {
      console.error("Error updating user:", error)
    }

    setIsEditDialogOpen(false)
    setEditingUser(null)
  }

  const handleDelete = (user: User) => {
    setDeletingUser(user)
    setIsDeleteDialogOpen(true)
  }

  const confirmDelete = async () => {
    if (deletingUser) {
      try {
        const response = await fetch(`${USER_API_BASE_URL}${deletingUser.id}`, {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
          },
        })

        if (response.ok) {
          setUsers(users.filter(u => u.id !== deletingUser.id))
        } else {
          console.error("Failed to delete user")
        }
      } catch (error) {
        console.error("Error deleting user:", error)
      }

      setIsDeleteDialogOpen(false)
      setDeletingUser(null)
    }
  }

  return (
    <div className="container mx-auto p-4">
      <UserTable 
        users={users} 
        onEdit={handleEdit} 
        onDelete={handleDelete} 
      />
      <EditUserDialog 
        isOpen={isEditDialogOpen} 
        onOpenChange={setIsEditDialogOpen}
        user={editingUser}
        onSubmit={handleEditSubmit}
      />
      <DeleteUserDialog 
        isOpen={isDeleteDialogOpen} 
        onOpenChange={setIsDeleteDialogOpen}
        user={deletingUser}
        onConfirm={confirmDelete}
      />
    </div>
  )
}