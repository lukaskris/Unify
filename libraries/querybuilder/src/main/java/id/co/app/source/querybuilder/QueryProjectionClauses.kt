package com.forestry.plantation.querybuilder

import java.util.*

/**
 * Created by Lukas Kristianto on 6/12/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */


class QueryProjectionClauses : LinkedList<String>()
{
    fun addAllFieldsFromTable(tableName : String) : QueryProjectionClauses
    {
        add("$tableName.*")
        return this
    }

    fun addField(fieldName : String, fromTable : String, projectAs : String) : QueryProjectionClauses
    {
        add("$fromTable.$fieldName as $projectAs")
        return this
    }

    fun addField(builder : ProjectionClauseBuilder)
    {
        addField(fieldName = builder.fieldName, fromTable = builder.fromTable, projectAs = builder.projectAs)
    }

    fun merge() : String
    {
        if (isNotEmpty())
            return this.joinToString(separator = " , ")
        return "*"
    }
}