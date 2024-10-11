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

