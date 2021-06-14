package id.co.app.source.core.base.base

abstract class BaseModel {
    abstract val id: Any
    abstract fun equals(other: BaseModel): Boolean
}