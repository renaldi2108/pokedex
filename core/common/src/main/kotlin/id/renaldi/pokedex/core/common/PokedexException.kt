package id.renaldi.pokedex.core.common

class PokedexNetworkException(message: String) : Exception(message)

class PokedexEmptyResponseException : Exception("PokeAPI returned no data")
