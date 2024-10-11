'use client';

import { useState } from 'react';
import RecipeClientComponent from './RecipeClientComponent';

export default function RecipePage() {
    return (
        <div>
            <h1>Recipe Finder</h1>
            <RecipeClientComponent />
        </div>
    )
};
