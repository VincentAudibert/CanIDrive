package com.vaudibert.canidrive.domain.drinker

/**
 * Represents the person drinking.
 * The parameters such as weight and sex may change as the user adjusts the inputs.
 */

class PhysicalBody(
    private var weight: Double = 80.0,
    private var sex: String = "NONE",
    var isYoungDriver: Boolean = false,
    var isProfessionalDriver:Boolean = false
) {
    private var sexFactor: Double = 0.6

    var decreaseFactor: Double = 0.085

    var effectiveWeight:Double = weight * sexFactor

    init {
        changeSex(sex)
        setDecreaseFactor(sex)
    }

    fun changeSex(sex: String) {
        this.sex = sex
        setSexFactor(sex)
        effectiveWeight = computeEffectiveWeight()
        setDecreaseFactor(sex)
    }

    fun changeWeight(weight: Double) {
        this.weight = weight
        effectiveWeight = computeEffectiveWeight()
    }

    private fun setSexFactor(sex: String) {
        sexFactor = if (sex == "MALE") 0.7 else 0.6
    }

    private fun computeEffectiveWeight() = sexFactor * weight


    private fun setDecreaseFactor(sex: String) {
        decreaseFactor = if (sex == "MALE") 0.1 else 0.085
    }

    fun getWeight() = weight

    fun getSex() = sex

}