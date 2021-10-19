package id.co.app.querybuilder

import java.util.*

/**
 * Created by Lukas Kristianto on 6/12/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class QueryJoinClauses : LinkedList<String>()
{
    fun addInnerJoin      (remoteTable : String, remoteColumn : String, table : String, column : String) = join("inner",       remoteTable, remoteColumn, table, column)
    fun addCrossJoin      (remoteTable : String, remoteColumn : String, table : String, column : String) = join("cross",       remoteTable, remoteColumn, table, column)
    fun addLeftOuterJoin  (remoteTable : String, remoteColumn : String, table : String, column : String) = join("left outer",  remoteTable, remoteColumn, table, column)
    fun addRightOuterJoin (remoteTable : String, remoteColumn : String, table : String, column : String) = join("right outer", remoteTable, remoteColumn, table, column)
    fun addFullOuterJoin  (remoteTable : String, remoteColumn : String, table : String, column : String) = join("full outer",  remoteTable, remoteColumn, table, column)
    fun addLeftOuterJoinCustom(query: String) = add("left outer join $query")

    private fun join(type : String, remoteTable : String, remoteColumn : String, table : String, column : String)
    {
        add(" $type join $remoteTable on $table.$column = ${if(!remoteColumn.contains("SELECT")) "$remoteTable.$remoteColumn" else remoteColumn}")
    }

    fun addInnerJoin      (builder : JoinClauseBuilder) = join("inner",       builder)
    fun addCrossJoin      (builder : JoinClauseBuilder) = join("cross",       builder)
    fun addLeftOuterJoin  (builder : JoinClauseBuilder) = join("left outer",  builder)
    fun addRightOuterJoin (builder : JoinClauseBuilder) = join("right outer", builder)
    fun addFullOuterJoin  (builder : JoinClauseBuilder) = join("full outer",  builder)

    private fun join(type : String, builder : JoinClauseBuilder)
    {
        join(type = type, remoteTable = builder.remoteTable, remoteColumn = builder.remoteColumn,
            table = builder.table, column = builder.column)
    }

    fun merge() : String?
    {
        if (isNotEmpty())
            return this.joinToString(separator = " ")
        return null
    }
}
