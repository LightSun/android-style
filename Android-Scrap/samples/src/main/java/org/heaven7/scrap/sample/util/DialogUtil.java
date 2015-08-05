package org.heaven7.scrap.sample.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.BadTokenException;
import android.widget.PopupWindow;

public class DialogUtil {

	private static ProgressDialog sDialog;
	private static CharSequence sDefaultMsg;
	
	public static void dismiss(){
		if(sDialog!=null && sDialog.isShowing()){
			sDialog.dismiss();
			sDialog = null;
		}
	}
	
	public static void showProgressDialog(Activity activity){
		if(sDefaultMsg == null)
			sDefaultMsg = "loading";
		showProgressDialog(activity,sDefaultMsg);
	}
	
	public static void showProgressDialog(Activity activity,CharSequence content){
		showProgressDialog(activity,"",content);
	}
	public static void showProgressDialog(Activity activity,CharSequence title, CharSequence content){
		try{
			if(sDialog == null){
				sDialog = ProgressDialog.show(activity, title, content);
				sDialog.setCanceledOnTouchOutside(false);
				sDialog.setCancelable(false);
				sDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						sDialog = null;
					}
				});
			}else{
				if(sDialog.isShowing()){
					Log.w("showProgressDialog()", "dialog is showing...check?");
					return;
				}
			}
		}catch(BadTokenException e){
			e.printStackTrace();
		}
	}
	
	public static AlertDialog alert(Activity context,String title, String content,
			String leftStr,String rightStr,
			DialogInterface.OnClickListener leftBtnListener,
			DialogInterface.OnClickListener rightBtnListener){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton(leftStr, leftBtnListener);
		builder.setNegativeButton(rightStr, rightBtnListener);
		AlertDialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
		return dialog;
	}
	
	//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static Dialog create(Activity context,int theme,View view){
		/*AlertDialog.Builder  builder= null ;builder = new AlertDialog.Builder(context);
		
		if(view.getParent()!=null){ //be careful when you reuse this view
			((ViewGroup)view.getParent()).removeView(view);
		}
		builder.setView(view);
		AlertDialog d = builder.create();
		d.setCanceledOnTouchOutside(false);*/
		Dialog d = new Dialog(context, theme);
		if(view.getParent()!=null){ //be careful when you reuse this view
			((ViewGroup)view.getParent()).removeView(view);
		}
		d.setContentView(view);
		d.setCanceledOnTouchOutside(false);
		return d;
	}
	
	//遮罩实现类似dialog
	public static PopupWindow popup(Activity context,View view,int width,int height,int animStyle){
		PopupWindow pw = new PopupWindow(context);
        pw.setHeight(height);
        pw.setWidth(width);
        pw.setBackgroundDrawable(new ColorDrawable(0x88222222));
        pw.setOutsideTouchable(true);
        pw.setFocusable(true);
        pw.setAnimationStyle(animStyle);
        if(view.getParent()!=null)
        	((ViewGroup)view.getParent()).removeView(view);
        pw.setContentView(view);
        pw.showAtLocation(context.getWindow().getDecorView(), Gravity.LEFT|Gravity.TOP, 0, 0);
        return pw;
	}
	
	/**弹出有动画，自定义样式的dialog*/
	public static Dialog create(Context context,int theme,int animId,int layout){
		//if(layout.getParent()!=null)
			//((ViewGroup)layout.getParent()).removeView(layout);
		Dialog d = new Dialog(context, theme);
		d.getWindow().setWindowAnimations(animId);
		d.setContentView(layout);
		//d.findViewById(btnId)
		d.setCancelable(true);
		//d.show();
		return d;
	}
	
	public static void dismiss(Dialog d){
		if(d!=null && d.isShowing()){
			try{
			    d.dismiss();
			}catch (BadTokenException e) {
				e.printStackTrace();
			}
		}
	}

}
