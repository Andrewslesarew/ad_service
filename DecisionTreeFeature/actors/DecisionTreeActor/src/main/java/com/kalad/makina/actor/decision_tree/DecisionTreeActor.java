package com.kalad.makina.actor.decision_tree;

import com.kalad.makina.actor.decision_tree.wrapper.MessageWrapper;

public class DecisionTreeActor {
    public void handle(MessageWrapper message) throws Exception {
        message.setMock("something");
    }
}
