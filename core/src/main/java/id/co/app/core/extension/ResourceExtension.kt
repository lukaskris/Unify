package id.co.app.core.extension

import id.co.app.core.base.Resource


/**
 * Check whether Resource is successful or failed
 * @return true if Resource.Success
 */
inline val <R> Resource<R>.isSuccessful: Boolean
  get() = this is Resource.Success

/**
 * Check whether Resource is successful or failed
 * @return true if Resource.Failure
 */
inline val <R> Resource<R>.isFailure: Boolean
  get() = this is Resource.Failure
/**
 * Check whether Resource is loading
 * @return true if Resource.Loading
 */
inline val <R> Resource<R>.isLoading: Boolean
  get() = this is Resource.Loading

/**
 * @return the value or null if it is a Resource.Failure
 */
inline val <R> Resource<R>.valueOrNull: R?
  get() = if (this is Resource.Success) value else null

/**
 * @return the value or throw an exception if failure
 */
inline val <R> Resource<R?>.valueOrThrow: R
  get() = if (this is Resource.Success) value!!
  else throw NullPointerException("value == null")


/**
 * The block() will be called when it is a success
 */
inline fun <R> Resource<R>.onSuccess(block: (R) -> Unit) {
  if (this is Resource.Success) {
    block(value)
  }
}

/**
 * The block() will be called when it is a success
 */
inline fun <R> Resource<R>.onLoading(block: () -> Unit) {
  if (this is Resource.Loading) {
    block()
  }
}
/**
 * The block() will be called when it is a done
 */
inline fun <R> Resource<R>.onDone(block: () -> Unit) {
  if (this is Resource.Done) {
    block()
  }
}

/**
 * The block() will be called when it is a failure
 */
inline fun <R> Resource<R>.onFailure(block: (String) -> Unit) {
  if (this is Resource.Failure) {
    block(message)
  }
}

/**
 * Map successful value into something else.
 * @return a new transformed Resource.
 */
inline fun <T, R> Resource<T>.map(transform: (T) -> R): Resource<R> {
  return when (this) {
    is Resource.Failure -> this
    is Resource.Success -> Resource.Success(transform(value))
    is Resource.Loading -> this
    is Resource.Done -> this
  }
}

/**
 * Map Resource successful value into something else.
 * @return a new transformed Resource.
 */
inline fun <T, R> Resource<T>.flatMap(transform: (T) -> Resource<R>): Resource<R> {
  return when (this) {
    is Resource.Failure -> this
    is Resource.Success -> transform(value)
    is Resource.Loading -> this
    is Resource.Done -> this
  }
}

/**
 * Map an throwable into another throwable
 * @return a new transform Resource
 */
inline fun <T> Resource<T>.failureMap(transform: (String) -> String): Resource<T> {
  return when (this) {
    is Resource.Failure -> Resource.Failure(transform(message))
    is Resource.Success -> this
    is Resource.Loading -> this
    is Resource.Done -> this
  }
}

/**
 * Map Resource failure's throwable into another throwable
 * @return a new transform Resource
 */
inline fun <T> Resource<T>.failureFlatMap(transform: (String) -> Resource.Failure): Resource<T> {
  return when (this) {
    is Resource.Failure -> transform(message)
    is Resource.Success -> this
    is Resource.Loading -> this
    is Resource.Done -> this
  }
}

/**
 * Get value or default value when failed.
 * @return value or default value.
 * @param defaultValue a default to be return when failed.
 */
inline fun <R> Resource<R>.valueOrDefault(defaultValue: (String) -> R): R {
  return when (this) {
    is Resource.Failure -> defaultValue(message)
    is Resource.Success -> value
    is Resource.Loading -> defaultValue("")
    is Resource.Done -> defaultValue("")
  }
}

/**
 * @return an instance of Resource
 */
inline fun <R> ResourceOf(block: () -> R): Resource<R> {
  return try {
    Resource.Success(block())
  } catch (throwable: Throwable) {
    throwable.handleException()
  }
}

/**
 * Convert a nullable Resource into a non-null Resource.
 */
@Suppress("UNCHECKED_CAST")
inline fun <R> Resource<R?>.failureOnNull(): Resource<R> {
  return when (this) {
    is Resource.Failure -> this
    is Resource.Success -> {
      if (value == null) NullPointerException().handleException()
      else this as Resource<R>
    }
    is Resource.Loading -> this
    is Resource.Done -> this
  }
}

/**
 * Convert a nullable Resource into a non-null Resource.
 */
@Suppress("UNCHECKED_CAST")

inline val <R> Resource<R?>.failureOrEmpty: String
  get() = if (this is Resource.Failure) message
  else ""

/**
 * Create a Resource<T> from a standard Resource<T>.
 * @return Resource<T>
 */
inline fun <T> Resource<T>.asResource(): Resource<T> = ResourceOf { valueOrThrow }