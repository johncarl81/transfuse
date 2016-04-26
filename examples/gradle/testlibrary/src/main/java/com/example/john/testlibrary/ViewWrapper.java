package com.example.john.testlibrary;

import android.support.v7.app.AppCompatActivity;

import org.androidtransfuse.annotations.*;
import android.view.ViewGroup;
import javax.inject.Inject;


public class ViewWrapper {
    @Inject @View(RBridge.id.some_view_group) ViewGroup viewGroup;
}