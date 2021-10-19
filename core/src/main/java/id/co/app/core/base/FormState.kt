package id.co.app.core.base


/**
 * Created by Lukas Kristianto on 14/09/21 21.32.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
enum class FormState {
    NEW, DRAFT, FINAL, SYNCED;

    fun isNew() = this == NEW
    fun isDraft() = this == DRAFT
    fun isFinal() = this == FINAL
    fun isSynced() = this == SYNCED
}