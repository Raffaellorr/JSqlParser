package net.sf.jsqlparser.statement.create.table;

import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class PrimaryKey {
    private ExpressionList expressionList;
    boolean brackets = false;

    public ExpressionList getExpressionList() {
        return expressionList;
    }

    public void setExpressionList(ExpressionList expressionList) {
        setExpressionList(expressionList, false);
    }

    public void setExpressionList(ExpressionList partitionExpressionList, boolean brackets) {
        this.expressionList = partitionExpressionList;
        this.brackets = brackets;
    }

    public boolean isBrackets() {
        return brackets;
    }

    public void setBrackets(boolean brackets) {
        this.brackets = brackets;
    }

    public void toStringPrimaryKey(StringBuilder sb) {
        if (expressionList != null && !expressionList.getExpressions().isEmpty()) {
            sb.append("PRIMARY KEY ");
            sb.append(PlainSelect.
                    getStringList(expressionList.getExpressions(), true, brackets));
            sb.append(" ");
        }
    }
}
