package com.forestry.plantation.querybuilder

import java.util.*

/**
 * Created by Lukas Kristianto on 6/12/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class QueryWhereConditions : LinkedList<String>()
{
    fun addSearchConditions(search : String?, onColumns : Array<String>)
    {
        if (search!=null&&search.isNotEmpty())
        {
            val likeArgument="'%${SQLEscape.escapeAndUnquoteString(search)}%'"

            if (onColumns.size==1)
                add(" ${onColumns[0]} like $likeArgument ")
            else
            {
                val subcondition=QueryWhereConditions()
                for (columnName in onColumns)
                    subcondition.add(" $columnName like $likeArgument ")
                add("(${subcondition.mergeWithOr()})")
            }
        }
    }

    fun addOrCondition(filter: String){
        add("($filter)")
    }

    fun mergeWithAnd() =
        if (!isEmpty())
            this.joinToString(separator = " and ")
        else " 1==1 "

    fun mergeWithOr() =
        if (!isEmpty())
            this.joinToString(separator = " or ")
        else " 1==1 "
}