package com.example.john.testlibrary;

import org.androidtransfuse.annotations.*;
import org.androidtransfuse.annotations.TransfuseModule;
import org.rbridge.Bridge;

@Bridge(R.class)
@TransfuseModule(library = true, namespace = "Atoms")
public class TestLibraryModule {

}
