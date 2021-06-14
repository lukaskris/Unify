package com.forestry.plantation.querybuilder

/**
 * Created by Lukas Kristianto on 6/12/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class JoinClauseBuilder
{
    internal lateinit var remoteTable : String
    fun remoteTable(remoteTable : String) = also { this.remoteTable=remoteTable }

    internal lateinit var remoteColumn : String
    fun remoteColumn(remoteColumn : String) = also { this.remoteColumn=remoteColumn }

    internal lateinit var table : String
    fun table(table : String) = also { this.table=table }

    internal lateinit var column : String
    fun column(column : String) = also { this.column=column }
}