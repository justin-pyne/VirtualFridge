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

