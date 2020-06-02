package com.example.hisland.Fragment;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragments extends Fragment {

    public Context context;
    public Resources resources;
    public LayoutInflater layoutInflater;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        this.resources=context.getResources();
        this.layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View Rootview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        if(Rootview==null){
            Rootview=inflater.inflate(getLayoutID(),container,false);
        }
        ViewGroup parent= (ViewGroup) Rootview.getParent();
        if(parent!=null){
            parent.removeView(Rootview);
        }
        return Rootview;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindEvent();
        initData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected <VV extends View> VV findView(View view,@IdRes int id){
        return view.findViewById(id);
    }
    protected <VV extends View> VV findView(@IdRes int id){
        return Rootview.findViewById(id);
    }

    protected abstract void initData();

    protected abstract void bindEvent();

    protected abstract void initView(View view);

    protected abstract int getLayoutID();


}


