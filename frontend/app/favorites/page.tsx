'use client'

import { useEffect, useState } from 'react'
import { useSession } from 'next-auth/react'
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Trash2, CookingPot } from "lucide-react"

interface Ingredient {
  id: number
  name: string
  amount: number
  unit: string
  caloriesPer100g: number
  proteinPer100g: number
  carbsPer100g: number
  fatPer100g: number
}

interface Recipe {
  id: number
  name: string
  description: string
  preparationTime: string
  ingredients: Ingredient[]
  instructions: string[]
  totalCalories: number
  totalProtein: number
  totalCarbs: number
  totalFat: number
}

const FAVORITES_API_BASE_URL = "http://localhost:8080/api/favorites"
const RECIPES_API_BASE_URL = "http://localhost:8080/api/recipes"

export default function Component() {
  const [recipes, setRecipes] = useState<Recipe[]>([])
  const [loading, setLoading] = useState(true)
  const [cookingStatus, setCookingStatus] = useState<{ [key: number]: string }>({})
  const { data: session } = useSession()

  useEffect(() => {
    const fetchRecipes = async () => {
      if (!session?.user?.email) return

      setLoading(true)
      try {
        const response = await fetch(`${FAVORITES_API_BASE_URL}/get?email=${encodeURIComponent(session.user.email)}`)
        if (!response.ok) throw new Error('Failed to fetch recipes')
        const data = await response.json()
        setRecipes(data)
      } catch (error) {
        console.error('Error fetching recipes:', error)
      } finally {
        setLoading(false)
      }
    }

    if (session?.user?.email) {
      fetchRecipes()
    }
  }, [session])

  const handleDelete = async (id: number) => {
    if (!session?.user?.email) return

    try {
      const response = await fetch(`${FAVORITES_API_BASE_URL}/favorite?email=${encodeURIComponent(session.user.email)}&recipeId=${id}`, {
        method: 'POST',
      })
      if (!response.ok) throw new Error('Failed to toggle favorite status')
      setRecipes(recipes.filter(recipe => recipe.id !== id))
      console.log('Recipe removed from favorites successfully')
    } catch (error) {
      console.error('Error removing recipe from favorites:', error)
    }
  }

  const handleCook = async (recipe: Recipe) => {
    if (!session?.user?.email) return

    try {
      const response = await fetch(`${RECIPES_API_BASE_URL}/cook/${recipe.id}?email=${encodeURIComponent(session.user.email)}`, {
        method: 'POST',
      })
    
      const data = await response.text()
    
      if (response.ok) {
        setCookingStatus(prev => ({
          ...prev,
          [recipe.id]: "The recipe has been cooked and the ingredients have been deducted."
        }))
      } else if (response.status === 400) {
        setCookingStatus(prev => ({
          ...prev,
          [recipe.id]: "Insufficient ingredients for this recipe."
        }))
      } else {
        throw new Error(data || 'Failed to cook recipe')
      }
    } catch (error) {
      console.error('Error cooking recipe:', error)
      setCookingStatus(prev => ({
        ...prev,
        [recipe.id]: "An unexpected error occurred while cooking the recipe."
      }))
    }
  }

  if (loading) {
    return <div>Loading...</div>
  }

  if (!session) {
    return <div>Please sign in to view your favorite recipes.</div>
  }

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-4xl font-bold mb-6">Favorite Recipes</h1>
      <div className="grid gap-6">
        {recipes.map((recipe) => (
          <Card key={recipe.id} className="overflow-hidden">
            <CardContent className="p-6">
              <div className="grid md:grid-cols-2 gap-6">
                <div>
                  <h2 className="text-2xl font-bold mb-2">{recipe.name}</h2>
                  <p className="text-muted-foreground mb-4">{recipe.description}</p>
                  
                  <h3 className="text-xl font-semibold mb-2">Ingredients:</h3>
                  <ul className="list-disc pl-5 space-y-1 mb-4">
                    {recipe.ingredients.map((ingredient) => (
                      <li key={ingredient.id}>
                        {ingredient.amount} {ingredient.unit} {ingredient.name}
                      </li>
                    ))}
                  </ul>

                  <div className="space-y-2">
                    <h3 className="text-xl font-semibold mb-2">Nutrients:</h3>
                    <div className="space-y-2">
                      <NutrientBar label="Carbs" value={recipe.totalCarbs} max={100} />
                      <NutrientBar label="Fat" value={recipe.totalFat} max={100} />
                      <NutrientBar label="Protein" value={recipe.totalProtein} max={100} />
                    </div>
                    <p className="text-sm mt-2">Calories: {Math.round(recipe.totalCalories)}</p>
                  </div>
                </div>

                <div>
                  <h3 className="text-xl font-semibold mb-2">Instructions:</h3>
                  <ol className="list-decimal pl-5 space-y-2 mb-6">
                    {recipe.instructions.map((instruction, index) => (
                      <li key={index}>{instruction}</li>
                    ))}
                  </ol>
                </div>
              </div>

              <div className="flex flex-col items-end gap-2 mt-4">
                <div className="flex gap-2">
                  <Button
                    onClick={() => handleCook(recipe)}
                    className="flex items-center gap-2"
                  >
                    <CookingPot className="h-4 w-4" />
                    Cook Recipe
                  </Button>
                  <Button
                    variant="destructive"
                    onClick={() => handleDelete(recipe.id)}
                    className="flex items-center gap-2"
                  >
                    <Trash2 className="h-4 w-4" />
                    Delete
                  </Button>
                </div>
                {cookingStatus[recipe.id] && (
                  <p className={`text-sm ${
                    cookingStatus[recipe.id].includes("cooked") 
                      ? "text-green-600" 
                      : "text-red-600"
                  }`}>
                    {cookingStatus[recipe.id]}
                  </p>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  )
}

function NutrientBar({ label, value, max }: { label: string; value: number; max: number }) {
  const percentage = Math.min((value / max) * 100, 100)
  
  return (
    <div className="space-y-1">
      <div className="flex justify-between text-sm">
        <span>{label}:</span>
        <span>{Math.round(value)}g</span>
      </div>
      <div className="h-2 bg-muted rounded-full overflow-hidden">
        <div
          className="h-full bg-primary rounded-full"
          style={{ width: `${percentage}%` }}
        />
      </div>
    </div>
  )
}