package org.heaven7.scrap.databinding.expression;

/**
 * Created by heaven7 on 2015/8/12.
 */
public class MethodAccessExpr extends Expression{
//StringUtils.xxx(aa,bb)
    private String rawString;
    private String className;
    private String methodName;
    private boolean  isStatic;

    private int mParamCount;
    private Expression[] mParamExpressions;

    public void setParamCount(int count){
        this.mParamCount = count;
        mParamExpressions = new Expression[count];
    }
    public int getParamCount(){
        return mParamCount;
    }
    public Expression[] getParamExpressions(){
        return mParamExpressions;
    }
    public void setParamExpressions(Expression...expressions){
        if(expressions.length != mParamCount){
            throw new IllegalStateException("expressions.length must = count");
        }
        for(int i=0,size = mParamCount ; i<size ;i++){
            mParamExpressions[i] = expressions[i];
        }
    }

    public String getRawString() {
        return rawString;
    }
    public void setRawString(String rawString) {
        this.rawString = rawString;
    }

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    /** get the method name or field name */
    public String getMethodName() {
        return methodName;
    }
    /** set the method name */
    public void setMethodName(String methodname) {
        this.methodName = methodname;
    }
    public boolean isStatic() {
        return isStatic;
    }
    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    @Override
    public <R, A> R accept(IExpressionVisitor<R, A> visitor, A arg) {
        return visitor.visit(this,arg);
    }
}
