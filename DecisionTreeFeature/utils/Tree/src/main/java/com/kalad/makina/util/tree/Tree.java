package com.kalad.makina.util.tree;

import info.smart_tools.smartactors.iobject.ifield.IField;
import info.smart_tools.smartactors.iobject.iobject.IObject;
import info.smart_tools.smartactors.ioc.ioc.IOC;
import info.smart_tools.smartactors.ioc.named_keys_storage.Keys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {
    private Map<String, IField> levelFList = new HashMap<>();
    private List<String> levelsNames;
    private Map<String, Map<Object, Node>> levels = new HashMap<>();
    private IField descriptionF;


    public Tree(List<String> levelsList, List<Node> nodes) {
        try {
            for (String level : levelsList) {
                this.levelFList.put(level, IOC.resolve(Keys.getOrAdd(IField.class.getCanonicalName()), level));
                this.levels.put(level, new HashMap<>());
            }
            this.levelsNames = levelsList;
            this.descriptionF = IOC.resolve(Keys.getOrAdd(IField.class.getCanonicalName()), "description");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void match(IObject data) {
        try {
            for (String levelName : levelsNames) {
                Map<Object, Node> level = levels.get(levelName);
                IField levelF = levelFList.get(levelName);
                Object dataVal = levelF.in(data);
                Node node = level.get(dataVal);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
