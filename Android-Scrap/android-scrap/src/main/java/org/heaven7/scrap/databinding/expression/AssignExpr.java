package org.heaven7.scrap.databinding.expression;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class AssignExpr extends Expression {

//like: target &= value;

    Expression target;
    Expression value;
    Operator operator;

    @Override
    public <R, A> R accept(IExpressionVisitor<R, A> visitor, A arg) {
        return visitor.visit(this,arg);
    }

    public Expression getTarget() {
        return target;
    }
    public void setTarget(Expression target) {
        this.target = target;
    }

    public Expression getValue() {
        return value;
    }
    public void setValue(Expression value) {
        this.value = value;
    }

    public Operator getOperator() {
        return operator;
    }
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public enum Operator{
        Assign("="),

        Plus("+="),
        Minus("-="),
        Star("*="),
        Slash("/="),
        Rem("%="),

        And("&="),
        Or("|="),
        Xor("^="),

        Lshift("<<="),
        RsignedShift(">>="),
        RunsignedShift(">>>=")
        ;
        final String value;

        Operator(String value) {
            this.value = value;
        }
    }
}
