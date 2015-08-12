package org.heaven7.scrap.databinding.expression;

import org.heaven7.scrap.databinding.expression.base.BooleanExpr;
import org.heaven7.scrap.databinding.expression.base.FloatExpr;
import org.heaven7.scrap.databinding.expression.base.IntExpr;
import org.heaven7.scrap.databinding.expression.base.StringExpr;

/**
 * Created by heaven7 on 2015/8/12.
 */
public interface IExpressionVisitor<R,A> {

    R visit(MethodAccessExpr accessExpr, A arg) ;
    R visit(FieldAccessExpr accessExpr, A arg) ;
    R visit(AssignExpr accessExpr, A arg) ;
    R visit(EnclosedExpr enclosedExpr, A arg);

    R visit(ArrayAccessExpr arrayAccessExpr, A arg);

    R visit(CastExpr castExpr, A arg);

    R visit(IntExpr intExpr, A arg);
    R visit(FloatExpr floatExpr, A arg);
    R visit(BooleanExpr booleanExpr, A arg);
    R visit(StringExpr stringExpr, A arg);

    R visit(TernaryExpr ternaryExpr, A arg);
}
