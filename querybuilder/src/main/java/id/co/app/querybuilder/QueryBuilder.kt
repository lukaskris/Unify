package id.co.app.querybuilder

import androidx.sqlite.db.SimpleSQLiteQuery


/**
 * Created by Lukas Kristianto on 6/12/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

abstract class QueryBuilder<FILTER : BaseFilter>
    (
    val filter : FILTER
)
{
    open fun buildSqlString() : String
    {
        var sql="select ${projection(QueryProjectionClauses())}"
        sql+=" from ${tableName()} "

        join(QueryJoinClauses())?.let { join ->
            if (join.isNotEmpty())
                sql+=join
        }

        where(QueryWhereConditions())?.let { where ->
            if (where.isNotEmpty())
                sql+=" where $where "
        }

        groupBy()?.let { group ->
            if(group.isNotEmpty()) sql+=" group by $group"
        }

        orderBy()?.let { order ->
            if (order.isNotEmpty())
                sql+=" order by $order "
        }

        if (isPaginationEnabled())
        {
            sql+=" limit ${filter.limit} "
            sql+=" offset ${filter.offset} "
        }

        return sql
    }

    open fun build() = SimpleSQLiteQuery(buildSqlString())

    abstract fun tableName() : String?
    open fun projection(clauses : QueryProjectionClauses) : String = "*"
    open fun join(clauses : QueryJoinClauses) : String? = null
    abstract fun where(conditions : QueryWhereConditions) : String?
    open fun orderBy() : String? = null
    open fun groupBy() : String? = null
    open fun isPaginationEnabled() : Boolean = QueryBuilderDefault.isPaginationEnabled

    val String.sqlEscaped get() = SQLEscape.escapeString(this)
    val IntArray.sqlEscaped get() = SQLEscape.escapeNumberArray(this)
    val LongArray.sqlEscaped get() = SQLEscape.escapeNumberArray(this)
    val DoubleArray.sqlEscaped get() = SQLEscape.escapeNumberArray(this)
    val FloatArray.sqlEscaped get() = SQLEscape.escapeNumberArray(this)
    val Boolean.sqlEscaped get() = SQLEscape.escapeBoolean(this)

    val Array<*>.sqlEscaped get() =
        if (firstOrNull()!=null&&first() is String)
            SQLEscape.escapeStringArray(this as Array<String>)
        else if (firstOrNull()!=null&&first() is Number)
            SQLEscape.escapeNumberCollection(toList())
        else if (firstOrNull()!=null)
            throw RuntimeException("Cannot escape ${first()!!::class.java.name}")
        else throw RuntimeException("Cannot escape empty collection")

    val Collection<*>.sqlEscaped get() =
        if (firstOrNull()!=null&&first() is String)
            SQLEscape.escapeStringCollection(this as Collection<String>)
        else if (firstOrNull()!=null&&first() is Number)
            SQLEscape.escapeNumberCollection(this)
        else if (firstOrNull()!=null)
            throw RuntimeException("Cannot escape ${first()!!::class.java.name}")
        else throw RuntimeException("Cannot escape empty collection")
}