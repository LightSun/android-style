package org.heaven7.scrap.databinding.expression;

/**
 * Created by heaven7 on 2015/8/12.
 */
public abstract class Expression {

   /* public enum ExpressionType{
        Operator,
        Access,
        Assign
    }*/

    public abstract<R,A> R accept(IExpressionVisitor<R,A> visitor,A arg);

   // public abstract ExpressionType  getExpressionType();

}
