package org.heaven7.scrap.databinding.expression;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class TernaryExpr extends Expression {
    @Override
    public <R, A> R accept(IExpressionVisitor<R, A> visitor, A arg) {
        return visitor.visit(this,arg);
    }
}
