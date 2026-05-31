package com.dierlisson.rickmortyhub.data.mapper

import com.dierlisson.rickmortyhub.data.local.entity.CharacterEntity
import com.dierlisson.rickmortyhub.data.remote.dto.CharacterDto
import com.dierlisson.rickmortyhub.domain.model.Character

fun CharacterDto.toDomainModel(): Character {
    return Character(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        imageUrl = this.image,
        type = this.type,
        gender = this.gender,
        originName = this.origin.name,
        locationName = this.location.name,
        isFavorite = false
    )
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        imageUrl = this.imageUrl
    )
}

fun CharacterEntity.toDomainModel(): Character {
    return Character(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        imageUrl = this.imageUrl,
        isFavorite = true
    )
}
