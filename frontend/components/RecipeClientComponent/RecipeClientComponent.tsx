'use client';

import { useState } from 'react';
import { useSession } from 'next-auth/react';

export default function RecipeClientComponent() {
    interface Ingredient {
        name: string;
        amount: number;
        unit: string;
    }

    interface Recipe {
        id: number;
        name: string;
        description: string;
        preparationTime: string;
        ingredients: Ingredient[];
        instructions: string[];
        isFavorite: boolean;
    }

    const { data: session } = useSession();
    const [recipes, setRecipes] = useState<Recipe[]>([]);
    const [ingredients, setIngredients] = useState<Ingredient[]>([]);
    const [favorites, setFavorites] = useState<Recipe[]>([]);
    const [showIngredients, setShowIngredients] = useState(false);
    const [showFavorites, setShowFavorites] = useState(false);
    const [message, setMessage] = useState('');

    const [ingredientName, setIngredientName] = useState('');
    const [ingredientAmount, setIngredientAmount] = useState<number | string>('');
    const [ingredientUnit, setIngredientUnit] = useState('');

    const fetchRecipes = async () => {
        try {
            if (!session?.user?.email) return;
            setMessage('Generating new recipes...');
            setRecipes([]);
            const response = await fetch(`http://localhost:8080/api/recipes/get?email=${session.user.email}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('Failed to fetch recipes');
            }

            const data = await response.json();
            setRecipes(data.map((recipe: Recipe) => ({ ...recipe, isFavorite: false })));

            setMessage('Generated new recipes!');
        } catch (error) {
            console.error(error);
            setMessage('Error fetching recipes');
        }
    };

    const fetchAllIngredients = async () => {
        if (!session?.user?.email) return;
        
        try {
            setMessage('Fetching ingredients...');
            const response = await fetch(`http://localhost:8080/api/ingredients/get?email=${session.user.email}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('Failed to fetch ingredients');
            }

            const data = await response.json();
            setIngredients(data);
            setMessage('Fetched all ingredients');
        } catch (error) {
            console.error(error);
            setMessage('Error fetching ingredients');
        }
    };

    const fetchFavoriteRecipes = async () => {
        if (!session?.user?.email) return;

        try {
            setMessage('Fetching favorite recipes...');
            const response = await fetch(`http://localhost:8080/api/favorites/get?email=${session.user.email}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('Failed to fetch favorite recipes');
            }

            const data = await response.json();
            setFavorites(data.map((recipe: Recipe) => ({ ...recipe, isFavorite: true })));
            setMessage('Fetched favorite recipes');
        } catch (error) {
            console.error(error);
            setMessage('Error fetching favorite recipes');
        }
    };

    const toggleIngredientsList = () => {
        if (!showIngredients) {
            fetchAllIngredients();
        }
        setShowIngredients(!showIngredients);
    };

    const toggleFavoritesList = () => {
        if (!showFavorites) {
            fetchFavoriteRecipes();
        }
        setShowFavorites(!showFavorites);
    };

    const clearRecipes = () => {
        setRecipes([]);
        setMessage('Recipes cleared!');
    };

    const toggleFavorite = async (recipeId: number) => {
        if (!session?.user?.email) return;
        
        try {
            const response = await fetch(`http://localhost:8080/api/favorites/favorite?email=${session.user.email}&recipeId=${recipeId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('Failed to update favorite status');
            }

            const message = await response.text();
            setMessage(message);
            setRecipes(recipes.map(recipe =>
                recipe.id === recipeId ? { ...recipe, isFavorite: !recipe.isFavorite } : recipe
            ));
        } catch (error) {
            console.error(error);
            setMessage('Error updating favorite status');
        }
    };

    const addIngredient = async () => {
        if (!session?.user?.email) return;
        
        try {
            const response = await fetch(`http://localhost:8080/api/ingredients/add?email=${session.user.email}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    name: ingredientName,
                    amount: ingredientAmount,
                    unit: ingredientUnit,
                }),
            });

            if (!response.ok) {
                throw new Error('Failed to add/update ingredient');
            }

            const result = await response.json();
            setMessage(`Ingredient ${result.name} successfully added!`);
            setIngredientName('');
            setIngredientAmount('');
            setIngredientUnit('');
        } catch (error) {
            console.error(error);
            setMessage('Error adding/updating ingredient');
        }
    };

    return (
        <div>
            <button onClick={fetchRecipes}>Get Recipes</button>
            <button onClick={clearRecipes}>Clear Recipes</button>
            <button onClick={toggleFavoritesList}>
                {showFavorites ? 'Hide Favorites' : 'Show Favorites'}
            </button>
            <button onClick={toggleIngredientsList}>
                {showIngredients ? 'Hide Ingredients' : 'Show Ingredients'}
            </button>

            {message && <p>{message}</p>}

            <div>
                <ul>
                    {recipes.map((recipe, index) => (
                        <li key={index}>
                            <h3>{recipe.name}</h3>
                            <p>{recipe.description}</p>
                            <p>Preparation Time: {recipe.preparationTime}</p>
                            <h4>Ingredients</h4>
                            <ul>
                                {recipe.ingredients.map((ingredient, i) => (
                                    <li key={i}>
                                        {ingredient.name}: {ingredient.amount} {ingredient.unit}
                                    </li>
                                ))}
                            </ul>
                            <h4>Instructions</h4>
                            <ol>
                                {recipe.instructions.map((instruction, i) => (
                                    <li key={i}>{instruction}</li>
                                ))}
                            </ol>
                            <button onClick={() => toggleFavorite(recipe.id)}>
                                {recipe.isFavorite ? '★ Unfavorite' : '☆ Favorite'}
                            </button>
                        </li>
                    ))}
                </ul>
            </div>

            {showFavorites && (
                <div>
                    <h2>Favorite Recipes</h2>
                    <ul>
                        {favorites.map((recipe, index) => (
                            <li key={index}>
                                <h3>{recipe.name}</h3>
                                <p>{recipe.description}</p>
                                <p>Preparation Time: {recipe.preparationTime}</p>
                                <h4>Ingredients</h4>
                                <ul>
                                    {recipe.ingredients.map((ingredient, i) => (
                                        <li key={i}>
                                            {ingredient.name}: {ingredient.amount} {ingredient.unit}
                                        </li>
                                    ))}
                                </ul>
                                <h4>Instructions</h4>
                                <ol>
                                    {recipe.instructions.map((instruction, i) => (
                                        <li key={i}>{instruction}</li>
                                    ))}
                                </ol>
                                <button onClick={() => toggleFavorite(recipe.id)}>
                                    {recipe.isFavorite ? '★ Unfavorite' : '☆ Favorite'}
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            {showIngredients && (
                <div>
                    <h2>All Ingredients</h2>
                    <ul>
                        {ingredients.map((ingredient, index) => (
                            <li key={index}>
                                {ingredient.name}: {ingredient.amount} {ingredient.unit}
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            <div>
                <h2>Add Ingredient</h2>
                <input
                    type="text"
                    value={ingredientName}
                    onChange={(e) => setIngredientName(e.target.value)}
                    placeholder="Ingredient Name"
                />
                <input
                    type="number"
                    value={ingredientAmount}
                    onChange={(e) => setIngredientAmount(e.target.value)}
                    placeholder="Amount"
                />
                <input
                    type="text"
                    value={ingredientUnit}
                    onChange={(e) => setIngredientUnit(e.target.value)}
                    placeholder="Unit (e.g., grams, cups)"
                />
                <button onClick={addIngredient}>Add Ingredient</button>
            </div>
        </div>
    );
}