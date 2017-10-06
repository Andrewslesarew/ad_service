package com.kalad.makina.actor.decision_tree.wrapper;

import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.iobject.iobject.exception.ChangeValueException;
import info.smart_tools.smartactors.iobject.iobject.exception.ReadValueException;

import java.nio.ReadOnlyBufferException;

public interface MessageWrapper {
    IObject getHeaders() throws ReadValueException;
    void setMock(String mock) throws ChangeValueException;
}
