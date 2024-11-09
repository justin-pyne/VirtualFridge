'use client'

import { useState } from "react"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Button } from "@/components/ui/button"
import { DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"

interface Ingredient {
  id: number
  name: string
  amount: number
  unit: string
}

interface EditIngredientDialogProps {
  ingredient: Ingredient
  onUpdate: (updates: Partial<Ingredient>) => void
  onClose: () => void
}

const units = ['g', 'kg', 'oz', 'lb', 'ml', 'l', 'cup', 'tbsp', 'tsp', 'pcs', 'dozen']

export function EditIngredientDialog({ ingredient, onUpdate, onClose }: EditIngredientDialogProps) {
  const [updatedIngredient, setUpdatedIngredient] = useState<Partial<Ingredient>>({})

  const handleUpdate = (updates: Partial<Ingredient>) => {
    setUpdatedIngredient(prev => ({ ...prev, ...updates }))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    onUpdate(updatedIngredient)
    onClose()
  }

  return (
    <DialogContent className="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>Edit Ingredient</DialogTitle>
      </DialogHeader>
      <form onSubmit={handleSubmit}>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="name" className="text-right">
              Name
            </Label>
            <Input
              id="name"
              defaultValue={ingredient.name}
              className="col-span-3"
              onChange={(e) => handleUpdate({ name: e.target.value })}
            />
          </div>
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="amount" className="text-right">
              Amount
            </Label>
            <Input
              id="amount"
              type="number"
              defaultValue={ingredient.amount.toString()}
              className="col-span-3"
              onChange={(e) => handleUpdate({ amount: parseFloat(e.target.value) })}
            />
          </div>
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="unit" className="text-right">
              Unit
            </Label>
            <Select
              defaultValue={ingredient.unit}
              onValueChange={(value) => handleUpdate({ unit: value })}
            >
              <SelectTrigger className="col-span-3">
                <SelectValue placeholder="Select a unit" />
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
        </div>
        <DialogFooter>
          <Button type="submit">Save changes</Button>
        </DialogFooter>
      </form>
    </DialogContent>
  )
}