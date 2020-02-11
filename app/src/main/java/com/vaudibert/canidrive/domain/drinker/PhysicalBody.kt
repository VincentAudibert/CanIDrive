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
    // TODO : find a more kotlin way to declare these constants
    private val MALE = "MALE"
    private val MALE_SEX_FACTOR = 0.7
    private val MALE_DECREASE = 0.1

    private val FEMALE_SEX_FACTOR = 0.6
    private val FEMALE_DECREASE = 0.085


    var onUpdate = { _ : String, _ : Double -> }

    var decreaseFactor: Double = FEMALE_DECREASE

    var effectiveWeight:Double = weight * FEMALE_SEX_FACTOR

    init {
        update()
    }

    fun update(
        sex: String = this.sex,
        weight: Double = this.weight
    ) {
        this.sex = sex
        this.weight = weight
        effectiveWeight = weight * (if (sex == MALE) MALE_SEX_FACTOR else FEMALE_SEX_FACTOR)
        decreaseFactor = if (sex == MALE) MALE_DECREASE else FEMALE_DECREASE
        onUpdate(sex, weight)
    }

    fun getWeight() = weight

    fun getSex() = sex

}