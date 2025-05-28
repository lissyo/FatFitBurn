package ru.ilessy.domain.enums

enum class WorkoutType(private val typeWorkout: Int) {
    UNDEFINED(0),
    WORKOUT(1),
    ONLINE(2),
    ONLINE_WORKOUT(3);

    companion object {
        fun fromInt(typeWorkout: Int): WorkoutType {
            return entries.find { it.typeWorkout == typeWorkout } ?: UNDEFINED
        }
    }

}