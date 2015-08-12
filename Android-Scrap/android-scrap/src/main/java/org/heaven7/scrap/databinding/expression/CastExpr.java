package org.heaven7.scrap.databinding.expression;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class CastExpr extends Expression{
   // ((ImageView)v )
    private String variable;
    private String castClassname;

    @Override
    public <R, A> R accept(IExpressionVisitor<R, A> visitor, A arg) {
        return visitor.visit(this,arg);
    }

    public String getVariable() {
        return variable;
    }
    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getCastClassname() {
        return castClassname;
    }
    public void setCastClassname(String castClassname) {
        this.castClassname = castClassname;
    }
}
