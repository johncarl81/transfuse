package org.androidrobotics.example.simple;

import android.widget.TextView;
import org.androidrobotics.annotations.*;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity(name = "SecondActivity", label = "@string/app_name")
@Layout(R.layout.second)
public class SecondActivityDelegate {

    private TextView textView;
    @Inject
    @View(R.id.text3)
    private TextView textView3;
    @Inject
    @Extra("testExtra")
    private String testExtra;
    @Inject
    @Resource(R.array.simpleStringArray)
    private String[] simpleStringArray;

    @Inject
    public SecondActivityDelegate(@View(R.id.text2) TextView textView) {
        this.textView = textView;
    }

    @OnTouch
    public void update() {
        textView.setText("touched");
        textView3.setText(simpleStringArray[0]);
    }

    @OnCreate
    public void onCreate() {
        textView3.setText(testExtra);
    }

    public TextView getTextView() {
        return textView;
    }

    public String getTestExtra() {
        return testExtra;
    }

    public String[] getSimpleStringArray() {
        return simpleStringArray;
    }
}
