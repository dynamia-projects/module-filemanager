/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.modules.filemanager.ui;

import com.dynamia.tools.io.FileInfo;
import com.dynamia.tools.web.ui.ChildrenLoader;
import com.dynamia.tools.web.ui.*;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

/**
 *
 * @author mario
 */
public class DirectoryTree extends Tree implements ChildrenLoader<FileInfo>, EventListener<Event> {

    private File selected;
    private EntityTreeModel<FileInfo> treeModel;

    private EntityTreeNode<FileInfo> rootNode;
    private boolean showHiddenFolders;
    private final File rootDirectory;

    public DirectoryTree(File rootDirectory) {
        this.rootDirectory = rootDirectory;

        init();
    }

    public DirectoryTree(Path rootPath) {
        this(rootPath.toFile());
    }

    public void reset() {
        initModel();
    }

    private void init() {
        setHflex("1");
        setVflex("1");
        addEventListener(Events.ON_CLICK, this);
        setItemRenderer(new DirectoryTreeItemRenderer());

        setVflex("1");
        setHflex("1");

        initModel();
    }

    private void initModel() {
        FileInfo file = new FileInfo(rootDirectory);

        rootNode = new EntityTreeNode<FileInfo>(file);
        treeModel = new EntityTreeModel<FileInfo>(rootNode);
        for (EntityTreeNode<FileInfo> entityTreeNode : getSubdirectories(file)) {
            rootNode.addChild(entityTreeNode);
        }
        setModel(treeModel);

    }

    private Collection<EntityTreeNode<FileInfo>> getSubdirectories(FileInfo file) {
        File[] subs = file.getFile().listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    if (!isShowHiddenFolders()) {
                        return !pathname.isHidden() && !pathname.getName().startsWith(".");
                    }
                    return true;
                }
                return false;
            }
        });

        List<EntityTreeNode<FileInfo>> subdirectories = new ArrayList<EntityTreeNode<FileInfo>>();
        if (subs != null) {
            for (File sub : subs) {
                subdirectories.add(new DirectoryTreeNode(new FileInfo(sub), this));
            }
        }

        Collections.sort(subdirectories, new Comparator<EntityTreeNode<FileInfo>>() {

            @Override
            public int compare(EntityTreeNode<FileInfo> o1, EntityTreeNode<FileInfo> o2) {
                return o1.getData().getName().compareTo(o2.getData().getName());
            }
        });

        return subdirectories;
    }

    @Override
    public void loadChildren(LazyEntityTreeNode<FileInfo> node) {
        for (EntityTreeNode<FileInfo> treeNode : getSubdirectories(node.getData())) {
            node.addChild(treeNode);
        }
    }

    @Override
    public void onEvent(Event event) throws Exception {
        Treeitem item = getSelectedItem();
        if (item != null) {
            DirectoryTreeNode node = item.getValue();
            setSelected(node.getData().getFile());
        }
    }

    public File getSelected() {
        return selected;
    }

    public void setSelected(File value) {
        this.selected = value;
        Events.postEvent(new Event(Events.ON_SELECT, this, value));
    }

    public boolean isShowHiddenFolders() {
        return showHiddenFolders;
    }

    public void setShowHiddenFolders(boolean showHiddenFolders) {
        this.showHiddenFolders = showHiddenFolders;
    }

}