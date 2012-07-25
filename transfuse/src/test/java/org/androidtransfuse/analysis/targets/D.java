package org.androidtransfuse.analysis.targets;//D -> E (EImpl) -> F (FProvider.get()) -> D

import javax.inject.Inject;

public class D {
    private E e;

    @Inject
    public D(E e) {
        this.e = e;
    }
}