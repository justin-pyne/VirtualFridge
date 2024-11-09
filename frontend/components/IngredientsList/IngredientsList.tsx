'use client'

import { useState } from 'react'
import Link from 'next/link'
import { Button } from "@/components/ui/button"
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { ScrollArea } from "@/components/ui/scroll-area"
import { MoreVertical, Edit, Trash, Plus } from "lucide-react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Spacer } from '@/components/Spacer'

const mockingredientfortest = [
  { id: 1, name: 'chicken', amount: 500, unit: 'g' }
]

export function IngredientsList() {
  const [ingredients, setIngredients] = useState(mockingredientfortest)
  const [newIngredient, setNewIngredient] = useState('')
  const [newAmount, setNewAmount] = useState<number>(0)
  const [newUnit, setNewUnit] = useState<string>('g')

  const addIngredient = () => {
    if (newIngredient.trim() !== '') {
      setIngredients([...ingredients, { 
        id: ingredients.length + 1, 
        name: newIngredient.trim(), 
        amount: newAmount,
        unit: newUnit 
      }])
      setNewIngredient('')
      setNewAmount(0)
      setNewUnit('g')
    }
  }

  const updateIngredient = (id: number, updatedIngredient: any) => {
    setIngredients(ingredients.map(ing => ing.id === id ? { ...ing, ...updatedIngredient } : ing))
  }

  const deleteIngredient = (id: number) => {
    setIngredients(ingredients.filter(ing => ing.id !== id))
  }

  const units = ['g', 'kg', 'oz', 'lb', 'ml', 'l', 'cup', 'tbsp', 'tsp', 'pcs', 'dozen']

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Ingredients List</h1>
      <Card>
        <CardContent>
        <Spacer size={20} />
            <ul>
              {ingredients.map((ingredient) => (
                <li key={ingredient.id} className="mb-2 flex items-center justify-between">
                  <span>{ingredient.name} - {ingredient.amount} {ingredient.unit}</span>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" className="h-8 w-8 p-0">
                        <span className="sr-only">Open menu</span>
                        <MoreVertical className="h-4 w-4" />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      <Dialog>
                        <DialogTrigger asChild>
                          <DropdownMenuItem onSelect={(e) => e.preventDefault()}>
                            <Edit className="mr-2 h-4 w-4" />
                            <span>Edit</span>
                          </DropdownMenuItem>
                        </DialogTrigger>
                        <DialogContent className="sm:max-w-[425px]">
                          <DialogHeader>
                            <DialogTitle>Edit Ingredient</DialogTitle>
                          </DialogHeader>
                          <div className="grid gap-4 py-4">
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="name" className="text-right">
                                Name
                              </Label>
                              <Input
                                id="name"
                                defaultValue={ingredient.name}
                                className="col-span-3"
                                onChange={(e) => updateIngredient(ingredient.id, { name: e.target.value })}
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
                                onChange={(e) => updateIngredient(ingredient.id, { amount: parseFloat(e.target.value) })}
                              />
                            </div>
                            <div className="grid grid-cols-4 items-center gap-4">
                              <Label htmlFor="unit" className="text-right">
                                Unit
                              </Label>
                              <Select
                                defaultValue={ingredient.unit}
                                onValueChange={(value) => updateIngredient(ingredient.id, { unit: value })}
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
                        </DialogContent>
                      </Dialog>
                      <DropdownMenuItem onSelect={() => deleteIngredient(ingredient.id)}>
                        <Trash className="mr-2 h-4 w-4" />
                        <span>Delete</span>
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </li>
              ))}
            </ul>
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
        </CardContent>
      </Card>
    </div>
  )
}