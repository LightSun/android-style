package org.heaven7.scrap.core.oneac;

/**
 * the back event handler
 * Created by heaven7 on 2015/8/3.
 */
public interface IBackEventProcessor{

    /**
     * handle the back event .
     * @return  true to consume the back event.
     */
    boolean handleBackEvent();
}
