package com.rouxinpai.arms.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/8/29 15:52
 * desc   :
 */
public class BindingReflex {

    /**
     * 反射获取ViewBinding
     *
     * @param <V>    ViewBinding 实现类
     * @param aClass 当前类
     * @param from   from
     * @return viewBinding实例
     */
    public static <V extends ViewBinding> V reflexViewBinding(Class<?> aClass, View from) {
        try {
            Type genericSuperclass = aClass.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) Objects.requireNonNull(genericSuperclass)).getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    Class<?> tClass;
                    try {
                        tClass = (Class<?>) actualTypeArgument;
                    } catch (Exception e) {
                        continue;
                    }
                    if (ViewBinding.class.isAssignableFrom(tClass)) {
                        Method inflate = tClass.getMethod("bind", View.class);
                        return (V) inflate.invoke(null, from);
                    }
                }
            }
            return reflexViewBinding(aClass.getSuperclass(), from);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 反射获取ViewBinding
     *
     * @param <V>    ViewBinding 实现类
     * @param aClass 当前类
     * @param from   from
     * @return viewBinding实例
     */
    public static <V extends ViewBinding> V reflexViewBinding(Class<?> aClass, LayoutInflater from) {
        try {
            Type genericSuperclass = aClass.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) Objects.requireNonNull(genericSuperclass)).getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    Class<?> tClass;
                    try {
                        tClass = (Class<?>) actualTypeArgument;
                    } catch (Exception e) {
                        continue;
                    }
                    if (ViewBinding.class.isAssignableFrom(tClass)) {
                        Method inflate = tClass.getMethod("inflate", LayoutInflater.class);
                        return (V) inflate.invoke(null, from);
                    }
                }
            }
            return reflexViewBinding(aClass.getSuperclass(), from);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 反射获取ViewBinding
     *
     * @param <V>    ViewBinding 实现类
     * @param aClass 当前类
     * @param from   layoutInflater
     * @param b      attachToRoot
     * @return viewBinding实例
     */
    public static <V extends ViewBinding> V reflexViewBinding(Class<?> aClass, LayoutInflater from, ViewGroup viewGroup, boolean b) {
        try {
            Type genericSuperclass = aClass.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) Objects.requireNonNull(genericSuperclass)).getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    Class<?> tClass;
                    try {
                        tClass = (Class<?>) actualTypeArgument;
                    } catch (Exception e) {
                        continue;
                    }
                    if (ViewBinding.class.isAssignableFrom(tClass)) {
                        Method inflate = tClass.getDeclaredMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
                        return (V) inflate.invoke(null, from, viewGroup, b);
                    }
                }
            }
            return (V) reflexViewBinding(aClass.getSuperclass(), from, viewGroup, b);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
