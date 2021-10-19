package id.co.app.querybuilder

/**
 * Created by Lukas Kristianto on 6/12/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class ProjectionClauseBuilder
{
    internal lateinit var fieldName : String
    fun fieldName(fieldName : String) = also { this.fieldName=fieldName }

    internal lateinit var fromTable : String
    fun fromTable(fromTable : String) = also { this.fromTable=fromTable }

    internal lateinit var projectAs : String
    fun projectAs(projectAs : String) = also { this.projectAs=projectAs }
}