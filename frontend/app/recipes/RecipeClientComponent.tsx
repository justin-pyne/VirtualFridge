'use client';

import { useState } from 'react';

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
            setMessage('Generating new recipes...');
            setRecipes([]);
            const response = await fetch('http://localhost:8080/api/recipes/get', {
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
        try {
            setMessage('Fetching ingredients...');
            const response = await fetch('http://localhost:8080/api/ingredients/get', {
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
        try {
            setMessage('Fetching favorite recipes...');
            const response = await fetch('http://localhost:8080/api/favorites/get', {
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
        try {
            const response = await fetch(`http://localhost:8080/api/favorites/favorite/${recipeId}`, {
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
        try {
            const response = await fetch('http://localhost:8080/api/ingredients/add', {
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

