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

