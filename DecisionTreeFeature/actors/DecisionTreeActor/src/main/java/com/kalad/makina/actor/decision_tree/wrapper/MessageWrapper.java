package com.kalad.makina.actor.decision_tree.wrapper;

import info.smart_tools.smartactors.iobject.iobject.exception.ChangeValueException;

public interface MessageWrapper {
    void setMock(String mock) throws ChangeValueException;
}
