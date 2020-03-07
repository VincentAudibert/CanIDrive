package com.vaudibert.canidrive.domain.drink

interface IIngestedDrinkProvider {
    fun getDrinks() : List<IIngestedDrink>
}