package id.renaldi.pokedex.core.common

sealed interface Outcome<out T> {
    data class Success<out T>(val data: T) : Outcome<T>

    data class Failure(val error: Throwable) : Outcome<Nothing>
}

fun <T> Outcome<T>.dataOrNull(): T? = (this as? Outcome.Success)?.data

fun <T> Outcome<T>.dataOr(fallback: T): T = dataOrNull() ?: fallback

fun <T> Outcome<T>.errorOrNull(): Throwable? = (this as? Outcome.Failure)?.error

fun <T, R> Outcome<T>.map(transform: (T) -> R): Outcome<R> = when (this) {
    is Outcome.Success -> Outcome.Success(transform(data))
    is Outcome.Failure -> this
}

suspend fun <T> runOutcome(block: suspend () -> T): Outcome<T> = try {
    Outcome.Success(block())
} catch (error: Throwable) {
    Outcome.Failure(error)
}
