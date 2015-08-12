package org.heaven7.scrap.databinding.expression;

/** eg: (xxx=aaa.xx())
 * Created by heaven7 on 2015/8/12.
 */
public class EnclosedExpr extends Expression {

    Expression inner;

    @Override
    public <R, A> R accept(IExpressionVisitor<R, A> visitor, A arg) {
        return visitor.visit(this,arg);
    }

    public Expression getInner() {
        return inner;
    }
    public void setInner(Expression inner) {
        this.inner = inner;
    }
}
