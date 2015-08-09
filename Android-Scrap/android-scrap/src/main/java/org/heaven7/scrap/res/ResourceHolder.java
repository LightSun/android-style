package org.heaven7.scrap.res;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.heaven7.scrap.util.ResourceUtil;

public class ResourceHolder implements ResourceHoldable {

	private Context mContext;

	public ResourceHolder(Context context) {
		this.mContext = context;
	}
	
	@Override
	public CharSequence getText(String resName){
		return mContext.getResources().getText(getStringId(resName));
	}	

	@Override
	public Bitmap getBitmap(String resName){
		return BitmapFactory.decodeResource(mContext.getResources(), getDrawableId(resName));
	}
	
	@Override
	public Bitmap getBitmap(String resName,int width,int height){
		 Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), getDrawableId(resName));
		 if(width <=0 || height<=0){
			 return bitmap;
		 }
		 return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}
	
	@Override
	public int getId(String resName){
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.Id);
	}
	
	@Override
	public int getStringId(String resName){
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.String);
	}
	
	@Override
	public int getLayoutId(String resName){
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.Layout);
	}
	
	@Override
	public int getDrawableId(String resName){
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.Drawable);
	}	

	@Override
	public int getStyleId(String resName){
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.Style);
	}
	
	@Override
	public int getAnimId(String resName){
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.Anim);
	}	
	
	@Override
	public int getColorId(String resName){
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.Color);
	}
	@Override
	public int getDimenId(String resName){
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.Dimen);
	}

	@Override
	public int getRawId(String resName) {
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.Raw);
	}
	
	@Override
	public int getStringArrayId(String resName){
		return ResourceUtil.getResId(mContext, resName, ResourceUtil.ResourceType.StringArray);
	}

	@Override
	public Context getContext() {
		return mContext;
	}
}
