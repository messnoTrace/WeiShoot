package com.NationalPhotograpy.weishoot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noTrace on 2015-04-17.
 */
public abstract  class CommomAdapter<T> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected  final  int mItemLayoutId;


    //自己添加为了实现选中效果
    private Map<Integer,Boolean> isSelected;
    private List<CommomViewHolder> list_holders;


    public CommomAdapter(Context context,List<T> mDatas,int mItemLayoutId){
        mInflater= LayoutInflater.from(context);
        this.mContext=context;
        this.mDatas=mDatas;
        this.mItemLayoutId=mItemLayoutId;
        init();

    }

    public void setData(List<T> list){
        this.mDatas=list;
        init();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void init(){
        isSelected=new HashMap<Integer, Boolean>();
        list_holders=new ArrayList<CommomViewHolder>();
        for(int i=0;i<mDatas.size();i++){
            isSelected.put(i,false);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final CommomViewHolder mHolder=getViewHolder(position,convertView,parent);
        list_holders.add(mHolder);
        convert(mHolder,(T)getItem(position),position);
        return mHolder.getConvertView();
    }

    public abstract  void convert(CommomViewHolder mHolder,T item,int position);
    private CommomViewHolder getViewHolder(int position,View convertView,ViewGroup parent){
        return  CommomViewHolder.get(mContext,convertView,parent,mItemLayoutId,position);
    }

    public Map<Integer,Boolean> getSelectedMap()
    {
        return isSelected;
    }

    public List<CommomViewHolder> getHolderList(){
        return  list_holders;
    }







}
