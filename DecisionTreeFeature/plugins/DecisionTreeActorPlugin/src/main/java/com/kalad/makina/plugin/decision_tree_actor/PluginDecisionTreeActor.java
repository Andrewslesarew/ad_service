package com.kalad.makina.plugin.decision_tree_actor;

import com.kalad.makina.actor.decision_tree.DecisionTreeActor;
import info.smart_tools.smartactors.base.exception.invalid_argument_exception.InvalidArgumentException;
import info.smart_tools.smartactors.base.strategy.apply_function_to_arguments.ApplyFunctionToArgumentsStrategy;
import info.smart_tools.smartactors.feature_loading_system.bootstrap_plugin.BootstrapPlugin;
import info.smart_tools.smartactors.feature_loading_system.interfaces.ibootstrap.IBootstrap;
import info.smart_tools.smartactors.ioc.iioccontainer.exception.RegistrationException;
import info.smart_tools.smartactors.ioc.iioccontainer.exception.ResolutionException;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;

public class PluginDecisionTreeActor extends BootstrapPlugin {
    /**
     * Constructor
     *
     * @param bootstrap the bootstrap item
     */
    public PluginDecisionTreeActor(final IBootstrap bootstrap) {
        super(bootstrap);
    }


    @BootstrapPlugin.Item("DecisionTreeActorPlugin")
    public void item()
            throws ResolutionException, RegistrationException, InvalidArgumentException {
        IOC.register(Keys.getOrAdd("DecisionTreeActor"),
                new ApplyFunctionToArgumentsStrategy(
                        (args) -> new DecisionTreeActor()
                )
        );
    }
}