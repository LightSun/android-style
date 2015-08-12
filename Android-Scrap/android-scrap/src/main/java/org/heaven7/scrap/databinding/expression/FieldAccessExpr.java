package org.heaven7.scrap.databinding.expression;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class FieldAccessExpr extends Expression {

    private String className;
    private String fieldName;
    private boolean isStatic;

    @Override
    public <R, A> R accept(IExpressionVisitor<R, A> visitor, A arg) {
        return visitor.visit(this,arg);
    }

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    public String getFieldName() {
        return fieldName;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean isStatic() {
        return isStatic;
    }
    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
}
