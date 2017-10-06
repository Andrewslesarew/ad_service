package com.kalad.makina.actor.decision_tree;

import com.kalad.makina.actor.decision_tree.wrapper.MessageWrapper;
import com.kalad.makina.util.tree.ITree;
import com.kalad.makina.util.tree.Tree;
import info.smart_tools.smartactors.iobject.iobject.IObject;

public class DecisionTreeActor {
    ITree tree1 = new Tree();
    ITree tree2 = new Tree();

    public DecisionTreeActor() {

    }

    public void updateMerchants() {

    }

    public void handle(MessageWrapper message) throws Exception {
        IObject headersObj = message.getHeaders();

    }
}
