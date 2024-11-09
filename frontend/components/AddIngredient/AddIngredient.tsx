'use client'

import { useState } from 'react'
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Plus } from "lucide-react"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { useSession } from 'next-auth/react'

const units = ['g', 'kg', 'oz', 'lb', 'ml', 'l', 'cup', 'tbsp', 'tsp', 'pcs', 'dozen']

export function AddIngredient() {
  const [newIngredient, setNewIngredient] = useState('')
  const [newAmount, setNewAmount] = useState<number>(0)
  const [newUnit, setNewUnit] = useState<string>('g')
  const { data: session } = useSession()
  const INGREDIENT_API_BASE_URL = "http://localhost:8080/api/ingredients/"

  const addIngredient = async () => {
    if (newIngredient.trim() !== '' && session?.user?.email) {
      try {
        const response = await fetch(`${INGREDIENT_API_BASE_URL}add?email=${session.user.email}`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name: newIngredient.trim(),
            amount: newAmount,
            unit: newUnit
          }),
        })

        if (response.ok) {
          const addedIngredient = await response.json()
          // Reset form
          setNewIngredient('')
          setNewAmount(0)
          setNewUnit('g')

        } else {
          console.error("Failed to add ingredient")
        }
      } catch (error) {
        console.error("Error adding ingredient:", error)
      }
    }
  }

  return (
    <div className="space-y-2">
      <h2 className="text-2xl font-bold">Add New Ingredient</h2>
      <div className="flex gap-4">
        <div className="flex-1 space-y-1">
          <Label htmlFor="ingredient-name">Name</Label>
          <Input
            id="ingredient-name"
            placeholder="Ingredient name"
            value={newIngredient}
            onChange={(e) => setNewIngredient(e.target.value)}
          />
        </div>
        <div className="flex-1 space-y-1">
          <Label htmlFor="ingredient-amount">Amount</Label>
          <Input
            id="ingredient-amount"
            type="number"
            placeholder="Amount"
            value={newAmount.toString()}
            onChange={(e) => setNewAmount(parseFloat(e.target.value))}
          />
        </div>
        <div className="w-[150px] space-y-1">
          <Label htmlFor="ingredient-unit">Unit</Label>
          <Select value={newUnit} onValueChange={setNewUnit}>
            <SelectTrigger id="ingredient-unit">
              <SelectValue placeholder="Unit" />
            </SelectTrigger>
            <SelectContent>
              {units.map((unit) => (
                <SelectItem key={unit} value={unit}>
                  {unit}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="flex items-end">
          <Button onClick={addIngredient} className="gap-2">
            <Plus className="h-4 w-4" /> Add
          </Button>
        </div>
      </div>
    </div>
  )
}