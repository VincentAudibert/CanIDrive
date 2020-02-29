package com.vaudibert.canidrive.domain.drink

open class PresetDrink(
    val name: String,
    val volume: Double,
    val degree: Double,
    var count: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (other !is PresetDrink) return false

        return this.name == other.name &&
                this.volume == other.volume &&
                this.degree == other.degree &&
                this.count == other.count
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + volume.hashCode()
        result = 31 * result + degree.hashCode()
        result = 31 * result + count
        return result
    }
}