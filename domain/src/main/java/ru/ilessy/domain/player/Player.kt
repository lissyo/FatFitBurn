package ru.ilessy.domain.player

abstract class Player : PlayerAction {
    abstract var media: Media?
    abstract fun initializationPlayer()
    abstract fun releasePlayer()
}