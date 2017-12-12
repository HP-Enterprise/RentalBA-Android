package com.gjcar.view.widget;

import android.content.Context;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.MotionEvent;
import android.util.AttributeSet;

/**http://blog.csdn.net/Gmp_467/article/details/46687157
 * @author Administrator
 *
 */
public class MySlidingPaneLayout extends SlidingPaneLayout {
    public MySlidingPaneLayout(Context context) {
        super(context, null);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MySlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO �Զ����ɵĹ��캯�����
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO �Զ����ɵķ������
    	//�жϲ˵��Ƿ�򿪣����򿪾Ͱ�ԭ���Ķ���ִ��
        if (this.isOpen()) {
            return super.onInterceptTouchEvent(arg0);
        } else {
            MotionEvent cancel = MotionEvent.obtain(arg0);
            cancel.setAction(MotionEvent.ACTION_CANCEL);
            return super.onInterceptTouchEvent(cancel);
        }

    }

}