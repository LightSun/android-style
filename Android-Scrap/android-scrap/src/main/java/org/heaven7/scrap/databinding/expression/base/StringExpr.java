package org.heaven7.scrap.databinding.expression.base;

import org.heaven7.scrap.databinding.expression.IExpressionVisitor;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class StringExpr extends BaseExpr {

    private String value;

    @Override
    public <R, A> R accept(IExpressionVisitor<R, A> visitor, A arg) {
        return visitor.visit(this,arg);
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
}
