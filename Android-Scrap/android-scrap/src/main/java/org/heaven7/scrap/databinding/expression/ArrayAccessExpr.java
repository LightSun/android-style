package org.heaven7.scrap.databinding.expression;

import org.heaven7.scrap.databinding.expression.base.IntExpr;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class ArrayAccessExpr extends Expression {

// user.getNmaes()[5]
    Expression name;//may be complex
    IntExpr index;

    // user.isFriend ? handlers.onClickFriend(user.getNames()[0] ) : handlers.onClickEnemy

    @Override
    public <R, A> R accept(IExpressionVisitor<R, A> visitor, A arg) {
        return visitor.visit(this,arg);
    }

    public Expression getName() {
        return name;
    }
    public void setName(Expression name) {
        this.name = name;
    }

    public IntExpr getIndex() {
        return index;
    }
    public void setIndex(IntExpr index) {
        this.index = index;
    }
}
