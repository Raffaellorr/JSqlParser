package net.sf.jsqlparser.expression;

public class LambdaExpression extends BinaryExpression  {

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String getStringExpression() {
        return "->";
    }
}
