package org.androidrobotics.example.simple;

import android.widget.TextView;
import org.androidrobotics.annotations.*;

import javax.inject.Inject;

/**
 * @author John Ericksen
 */
@Activity("SecondActivity")
@Layout(R.layout.second)
public class SecondActivityDelegate {

    private TextView textView;
    @Inject
    @View(R.id.text3)
    private TextView textView2;
    @Inject
    @Extra("testExtra")
    private String testExtra;

    @Inject
    public SecondActivityDelegate(@View(R.id.text2) TextView textView) {
        this.textView = textView;
    }

    @OnTouch
    public void update() {
        textView.setText("touched");
    }

    @OnCreate
    public void onCreate() {
        textView2.setText(testExtra);
    }

    public TextView getTextView() {
        return textView;
    }

    public String getTestExtra() {
        return testExtra;
    }
}
