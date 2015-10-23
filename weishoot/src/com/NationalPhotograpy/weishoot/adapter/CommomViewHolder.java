package com.NationalPhotograpy.weishoot.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by noTrace on 2015-04-17.
 */
public class CommomViewHolder {
    private final SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    private CommomViewHolder(Context context,ViewGroup parent,int layoutId,int position){
        this.mViews=new SparseArray<View>();
        this.mPosition=position;
        mConvertView= LayoutInflater.from(context).inflate(layoutId,parent,false);
        mConvertView.setTag(this);
    }

    public static CommomViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
        if(convertView==null){
            return  new CommomViewHolder(context,parent,layoutId,position);
        }
        return  (CommomViewHolder)convertView.getTag();
    }


    public <T extends View>T getView(int viewId)
    {
        View view=mViews.get(viewId);
        if(view==null){
            view=mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }
        return(T)view;
    }


    public View getConvertView()
    {
        return  mConvertView;
    }


    public CommomViewHolder setText(int viewId,String text){
        TextView view=getView(viewId);
        view.setText(text);
        return  this;
    }

    public CommomViewHolder setImageResourceId(int viewId,int drawableId){
        ImageView view=getView(viewId);
        view.setImageResource(drawableId);
        return  this;
    }


    public CommomViewHolder setImageBitmap(int viewId,Bitmap bm){
        ImageView view=getView(viewId);
        view.setImageBitmap(bm);
        return  this;
    }

    public CommomViewHolder setImageUri(int viewId,String uri){
        ImageView view=getView(viewId);
        ImageLoader.getInstance().displayImage(uri, view);
        return  this;
    }


    public CommomViewHolder setVisiblility(int viewId,int visibility){
        View view=getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public CommomViewHolder setBackgroundResource(int viewId,int drawable){
        View view=getView(viewId);
        view.setBackgroundResource(drawable);
        return this;
    }

    public CommomViewHolder setOnClickListener(int viewId,View.OnClickListener listener){
        View view=getView(viewId);
        view.setOnClickListener(listener);
        return  this;
    }







    public int getPosition(){
        return  mPosition;
    }

}
