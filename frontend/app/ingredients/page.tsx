'use client'

import { useState, useEffect } from 'react'
import { useSession } from 'next-auth/react'
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { MoreVertical, Edit, Trash, Plus, X, Calendar as CalendarIcon } from "lucide-react"
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger, DialogClose } from "@/components/ui/dialog"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Spacer } from '@/components/Spacer'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { format } from "date-fns"

interface Ingredient {
  id: number;
  name: string;
  amount: number;
  unit: string;
  expirationDate: string | null;
}

interface EditingIngredient extends Ingredient {
  tempName: string;
  tempAmount: number;
  tempUnit: string;
  tempExpirationDate: Date | null;
}

export default function IngredientsList() {
    const { data: session } = useSession()
    const [ingredients, setIngredients] = useState<Ingredient[]>([])
    const [newIngredient, setNewIngredient] = useState('')
    const [newAmount, setNewAmount] = useState<number>(0)
    const [newUnit, setNewUnit] = useState<string>('g')
    const [newExpirationDate, setNewExpirationDate] = useState<Date | null>(null)
    const [loading, setLoading] = useState(true)
    const [editingIngredient, setEditingIngredient] = useState<EditingIngredient | null>(null)

    const units = ['g', 'kg', 'oz', 'lb', 'ml', 'l', 'cup', 'tbsp', 'tsp', 'pcs', 'dozen']
    const INGREDIENT_API_BASE_URL = "http://localhost:8080/api/ingredients"

    useEffect(() => {
        if (session?.user?.email) {
            fetchIngredients()
        }
    }, [session])

    const fetchIngredients = async () => {
        if (!session?.user?.email) return

        setLoading(true)
        try {
            const response = await fetch(`${INGREDIENT_API_BASE_URL}/get?email=${session.user.email}`)
            if (response.ok) {
                const data = await response.json()
                setIngredients(data)
            } else {
                console.error('Failed to fetch ingredients')
            }
        } catch (error) {
            console.error('Error fetching ingredients:', error)
        }
        setLoading(false)
    }

    const addIngredient = async () => {
        if (!session?.user?.email) return
        if (newIngredient.trim() !== '') {
            const newIngredientData = {
                name: newIngredient.trim(),
                amount: newAmount,
                unit: newUnit,
                expirationDate: newExpirationDate ? format(newExpirationDate, 'MM-dd-yyyy') : null
            }

            try {
                const response = await fetch(`${INGREDIENT_API_BASE_URL}/add?email=${session.user.email}`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(newIngredientData),
                })

                if (response.ok) {
                    const addedIngredient = await response.json()
                    setIngredients([...ingredients, addedIngredient])
                    setNewIngredient('')
                    setNewAmount(0)
                    setNewUnit('g')
                    setNewExpirationDate(null)
                } else {
                    console.error('Failed to add ingredient')
                }
            } catch (error) {
                console.error('Error adding ingredient:', error)
            }
        }
    }

    const startEditing = (ingredient: Ingredient) => {
        setEditingIngredient({
            ...ingredient,
            tempName: ingredient.name,
            tempAmount: ingredient.amount,
            tempUnit: ingredient.unit,
            tempExpirationDate: ingredient.expirationDate ? new Date(ingredient.expirationDate) : null
        })
    }

    const handleEditSubmit = async () => {
        if (!session?.user?.email || !editingIngredient) return

        const updatedIngredient = {
            id: editingIngredient.id,
            name: editingIngredient.tempName,
            amount: editingIngredient.tempAmount,
            unit: editingIngredient.tempUnit,
            expirationDate: editingIngredient.tempExpirationDate ? format(editingIngredient.tempExpirationDate, 'MM-dd-yyyy') : null
        }

        try {
            const response = await fetch(`${INGREDIENT_API_BASE_URL}/add?email=${session.user.email}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(updatedIngredient),
            })

            if (response.ok) {
                const updatedIngredientData = await response.json()
                setIngredients(ingredients.map(ing => 
                    ing.id === updatedIngredientData.id ? updatedIngredientData : ing
                ))
                setEditingIngredient(null)
            } else {
                console.error('Failed to update ingredient')
            }
        } catch (error) {
            console.error('Error updating ingredient:', error)
        }
    }

    const deleteIngredient = async (id: number) => {
        if (!session?.user?.email) return

        try {
            const response = await fetch(`${INGREDIENT_API_BASE_URL}/delete/${id}?email=${session.user.email}`, {
                method: 'DELETE',
            })

            if (response.ok) {
                setIngredients(ingredients.filter(ing => ing.id !== id))
            } else {
                console.error('Failed to delete ingredient')
            }
        } catch (error) {
            console.error('Error deleting ingredient:', error)
        }
    }

    if (loading) {
        return <div>Loading...</div>
    }

    if (!session) {
        return <div>Please sign in to view your ingredients.</div>
    }

    return (
        <div className="container mx-auto p-4">
            <h1 className="text-3xl font-bold mb-6">Ingredients List</h1>
            <Card>
                <CardContent>
                    <Spacer size={20} />
                    <ScrollArea className="h-[60vh] mb-4">
                        <ul className="space-y-2">
                        {ingredients.map((ingredient) => (
                            <li key={ingredient.id} className="flex items-center justify-between p-2 hover:bg-accent rounded-md">
                                <span>
                                    {ingredient.name} - {ingredient.amount} {ingredient.unit}
                                    {ingredient.expirationDate && (
                                        <span className="ml-2 text-sm text-muted-foreground">
                                            Expires: {ingredient.expirationDate}
                                        </span>
                                    )}
                                </span>
                                <div className="flex items-center gap-2">
                                    <Button
                                        variant="ghost"
                                        size="icon"
                                        onClick={() => startEditing(ingredient)}
                                    >
                                        <Edit className="h-4 w-4" />
                                        <span className="sr-only">Edit</span>
                                    </Button>
                                    <Button
                                        variant="ghost"
                                        size="icon"
                                        onClick={() => deleteIngredient(ingredient.id)}
                                    >
                                        <Trash className="h-4 w-4" />
                                        <span className="sr-only">Delete</span>
                                    </Button>
                                </div>
                            </li>
                            ))}
                        </ul>
                    </ScrollArea>
                    <div className="space-y-2 mt-6">
                        <h2 className="text-2xl font-bold">Add New Ingredient</h2>
                        <div className="flex gap-4 flex-wrap">
                            <div className="flex-1 space-y-1">
                                <Label htmlFor="ingredient-name">Name</Label>
                                <Input
                                    id="ingredient-name"
                                    placeholder="Ingredient name"
                                    value={newIngredient}
                                    onChange={(e) => setNewIngredient(e.target.value)}
                                />
                            </div>
                            <div className="w-[200px] space-y-1">
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
                            <div className="w-[300px] space-y-1">
                                <Label htmlFor="ingredient-expiration">Expiration Date</Label>
                                <Popover>
                                    <PopoverTrigger asChild>
                                        <Button
                                            variant={"outline"}
                                            className={`w-full justify-start text-left font-normal ${
                                                !newExpirationDate && "text-muted-foreground"
                                            }`}
                                        >
                                            <CalendarIcon className="mr-2 h-4 w-4" />
                                            {newExpirationDate ? format(newExpirationDate, "PPP") : <span>Pick a date</span>}
                                        </Button>
                                    </PopoverTrigger>
                                    <PopoverContent className="w-auto p-0" align="start">
                                        <Calendar
                                            mode="single"
                                            selected={newExpirationDate || undefined}
                                            onSelect={(date: Date | undefined) => setNewExpirationDate(date || null)}
                                            initialFocus
                                        />
                                    </PopoverContent>
                                </Popover>
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

            <Dialog open={!!editingIngredient} onOpenChange={(open) => !open && setEditingIngredient(null)}>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle>Edit Ingredient</DialogTitle>
                    </DialogHeader>
                    <div className="grid gap-4 py-4">
                        <div className="grid gap-2">
                            <Label htmlFor="edit-name">Name</Label>
                            <Input
                                id="edit-name"
                                value={editingIngredient?.tempName || ''}
                                onChange={(e) => setEditingIngredient(prev => 
                                    prev ? { ...prev, tempName: e.target.value } : null
                                )}
                            />
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="edit-amount">Amount</Label>
                            <Input
                                id="edit-amount"
                                type="number"
                                value={editingIngredient?.tempAmount || 0}
                                onChange={(e) => setEditingIngredient(prev => 
                                    prev ? { ...prev, tempAmount: parseFloat(e.target.value) } : null
                                )}
                            />
                        </div>
                        <div className="grid gap-2">
                            <Label htmlFor="edit-unit">Unit</Label>
                            <Select
                                value={editingIngredient?.tempUnit}
                                onValueChange={(value) => setEditingIngredient(prev => 
                                    prev ? { ...prev, tempUnit: value } : null
                                )}
                            >
                                <SelectTrigger id="edit-unit">
                                    <SelectValue placeholder="Select unit" />
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
                        <div className="grid gap-2">
                            <Label htmlFor="edit-expiration">Expiration Date</Label>
                            <Popover>
                                <PopoverTrigger asChild>
                                    <Button
                                        id="edit-expiration"
                                        variant={"outline"}
                                        className={`w-full justify-start text-left font-normal ${
                                            !editingIngredient?.tempExpirationDate && "text-muted-foreground"
                                        }`}
                                    >
                                        <CalendarIcon className="mr-2 h-4 w-4" />
                                        {editingIngredient?.tempExpirationDate ? format(editingIngredient.tempExpirationDate, "PPP") : <span>Pick a date</span>}
                                    </Button>
                                </PopoverTrigger>
                                <PopoverContent className="w-auto p-0" align="start">
                                    <Calendar
                                        mode="single"
                                        selected={editingIngredient?.tempExpirationDate || undefined}
                                        onSelect={(date: Date | undefined) => setEditingIngredient(prev => 
                                            prev ? { ...prev, tempExpirationDate: date || null } : null
                                        )}
                                        initialFocus
                                    />
                                </PopoverContent>
                            </Popover>
                        </div>
                    </div>
                    <div className="flex justify-end gap-2">
                        <Button variant="outline" onClick={() => setEditingIngredient(null)}>
                            Cancel
                        </Button>
                        <Button onClick={handleEditSubmit}>
                            Save Changes
                        </Button>
                    </div>
                </DialogContent>
            </Dialog>
        </div>
    )
}