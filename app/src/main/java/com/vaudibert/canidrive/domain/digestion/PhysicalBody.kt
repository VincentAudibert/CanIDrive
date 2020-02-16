package com.vaudibert.canidrive.domain.digestion

/**
 * Represents the person drinking.
 * The parameters such as weight and sex may change as the user adjusts the inputs.
 */

class PhysicalBody {
    // TODO : find a more kotlin way to declare these constants
    private val MALE = "MALE"
    private val MALE_SEX_FACTOR = 0.7
    private val MALE_MIN_DECREASE = 0.1
    private val MALE_MAX_DECREASE = 0.15

    private val FEMALE_SEX_FACTOR = 0.6
    private val FEMALE_MIN_DECREASE = 0.085
    private val FEMALE_MAX_DECREASE = 0.1

    var sex = "NONE"
        set(value) {
            field = value
            effectiveWeight = weight * (if (sex == MALE) MALE_SEX_FACTOR else FEMALE_SEX_FACTOR)
            decreaseFactor = decreaseFactorWith(value, alcoholTolerance)
            onUpdate(sex, weight)
        }

    var weight = 80.0
        set(value) {
            field = value
            effectiveWeight = weight * (if (sex == MALE) MALE_SEX_FACTOR else FEMALE_SEX_FACTOR)
            onUpdate(sex, weight)
        }

    var alcoholTolerance = 0.0
        set(value) {
            if (value in 0.0..1.0) {
                field = value
                decreaseFactor = decreaseFactorWith(sex, value)
            }
        }

    private fun decreaseFactorWith(sex: String, tolerance: Double): Double {
        return if (sex == "MALE")
            tolerance * MALE_MAX_DECREASE + (1-tolerance) * MALE_MIN_DECREASE
        else
            tolerance * FEMALE_MAX_DECREASE + (1-tolerance) * FEMALE_MIN_DECREASE
    }

    var onUpdate = { _ : String, _ : Double -> }

    var decreaseFactor: Double = FEMALE_MIN_DECREASE

    var effectiveWeight:Double = weight * FEMALE_SEX_FACTOR

}