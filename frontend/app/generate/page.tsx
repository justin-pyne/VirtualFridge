'use client'

import { useState } from 'react'
import { useSession } from 'next-auth/react'
import { Quote, Star } from 'lucide-react'
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardFooter } from "@/components/ui/card"
import { Progress } from "@/components/ui/progress"

interface Ingredient {
  id: number;
  name: string;
  amount: number;
  unit: string;
  caloriesPer100g: number;
  proteinPer100g: number;
  carbsPer100g: number;
  fatPer100g: number;
}

interface Recipe {
  id: number;
  name: string;
  description: string;
  ingredients: Ingredient[];
  instructions: string[];
  isFavorite: boolean;
  totalCalories: number;
  totalProtein: number;
  totalCarbs: number;
  totalFat: number;
  preparationTime: string;
}

export default function RecipeGeneratorPage() {
  const { data: session } = useSession()
  const [generatedRecipes, setGeneratedRecipes] = useState<Recipe[]>([])
  const [funnyQuote, setFunnyQuote] = useState("I'm on a seafood diet. I see food, and I eat it!")
  const [loading, setLoading] = useState(false)

  const RECIPE_API_BASE_URL = "http://localhost:8080/api/recipes"
  const FAVORITE_API_BASE_URL = "http://localhost:8080/api/favorites"

  const generateRecipes = async () => {
    if (!session?.user?.email) return

    setLoading(true)
    try {
      const response = await fetch(`${RECIPE_API_BASE_URL}/get?email=${session.user.email}`)
      if (response.ok) {
        const newRecipes = await response.json()
        setGeneratedRecipes([...newRecipes, ...generatedRecipes])
      } else {
        console.error('Failed to generate recipes')
      }
    } catch (error) {
      console.error('Error generating recipes:', error)
    }
    setLoading(false)
  }

  const toggleFavorite = async (index: number) => {
    if (!session?.user?.email) return

    const recipe = generatedRecipes[index]
    
    try {
      const response = await fetch(`${FAVORITE_API_BASE_URL}/favorite`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
          email: session.user.email,
          recipeId: recipe.id.toString()
        })
      })

      if (response.ok) {
        const updatedRecipes = [...generatedRecipes]
        updatedRecipes[index] = {
          ...recipe,
          isFavorite: !recipe.isFavorite
        }
        setGeneratedRecipes(updatedRecipes)

        const message = await response.text()
        console.log(message)
      } else {
        console.error('Failed to toggle favorite')
      }
    } catch (error) {
      console.error('Error toggling favorite:', error)
    }
  }

  if (!session) {
    return <div>Please sign in to generate recipes.</div>
  }

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">Recipe Generator</h1>
      <Button 
        onClick={generateRecipes} 
        className="w-full mb-4"
        disabled={loading}
      >
        {loading ? 'Generating...' : 'Generate New Recipes'}
      </Button>
      {generatedRecipes.map((recipe, index) => (
        <Card key={index} className="mb-6">
          <CardContent className="p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <h2 className="text-2xl font-bold mb-2">{recipe.name}</h2>
                <p className="text-muted-foreground mb-4">{recipe.description}</p>
                <h3 className="text-xl font-semibold mb-2">Ingredients:</h3>
                <ul className="list-disc list-inside mb-4">
                  {recipe.ingredients.map((ingredient, i) => (
                    <li key={i}>{`${ingredient.amount} ${ingredient.unit} ${ingredient.name}`}</li>
                  ))}
                </ul>
                <h3 className="text-xl font-semibold mb-2">Nutrients:</h3>
                <div className="space-y-2">
                  <div className="flex items-center">
                    <span className="w-16">Carbs:</span>
                    <Progress value={recipe.totalCarbs} max={100} className="flex-1 mr-2" />
                    <span>{recipe.totalCarbs}g</span>
                  </div>
                  <div className="flex items-center">
                    <span className="w-16">Fat:</span>
                    <Progress value={recipe.totalFat} max={50} className="flex-1 mr-2" />
                    <span>{recipe.totalFat}g</span>
                  </div>
                  <div className="flex items-center">
                    <span className="w-16">Protein:</span>
                    <Progress value={recipe.totalProtein} max={50} className="flex-1 mr-2" />
                    <span>{recipe.totalProtein}g</span>
                  </div>
                  <div className="flex items-center">
                    <span className="w-16">Calories:</span>
                    <span>{recipe.totalCalories}</span>
                  </div>
                </div>
              </div>
              <div>
                <h3 className="text-xl font-semibold mb-2">Instructions:</h3>
                <ol className="list-decimal list-inside">
                  {recipe.instructions.map((instruction, i) => (
                    <li key={i} className="mb-2">{instruction}</li>
                  ))}
                </ol>
              </div>
            </div>
            <div className="mt-4 flex justify-end gap-2">
              <Button
                variant={recipe.isFavorite ? "secondary" : "outline"}
                onClick={() => toggleFavorite(index)}
                className="flex items-center gap-2"
              >
                <Star className={recipe.isFavorite ? "fill-current" : ""} />
                {recipe.isFavorite ? "Favorited" : "Add to Favorites"}
              </Button>
            </div>
          </CardContent>
        </Card>
      ))}
      <Card>
        <CardFooter className="flex flex-col items-center">
          <div className="mt-4 text-center italic text-muted-foreground flex items-start">
            <Quote className="w-4 h-4 mr-2 mt-1 flex-shrink-0" />
            <span>{funnyQuote}</span>
          </div>
        </CardFooter>
      </Card>
    </div>
  )
}