/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.modules.filemanager.actions;

import java.io.File;
import tools.dynamia.actions.ActionEvent;
import tools.dynamia.actions.InstallAction;
import tools.dynamia.io.FileInfo;
import tools.dynamia.io.ImageUtil;
import tools.dynamia.modules.filemanager.FileManager;
import tools.dynamia.modules.filemanager.FileManagerAction;
import tools.dynamia.modules.filemanager.ui.TextEditor;
import tools.dynamia.ui.MessageType;
import tools.dynamia.ui.UIMessages;

/**
 *
 * @author mario
 */
@InstallAction
public class EditTextFileAction extends FileManagerAction {

    public EditTextFileAction() {
        setName("Edit Text File");
        setImage("edit-page");
        setPosition(5.01);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        FileManager manager = (FileManager) evt.getSource();

        if (evt.getData() instanceof FileInfo) {
            File editfile = ((FileInfo) evt.getData()).getFile();
            if (!ImageUtil.isImage(editfile)) {
                TextEditor textEditor = new TextEditor(editfile);
                textEditor.setFileManager(manager);
                textEditor.show();
            } else {
                UIMessages.showMessage("Cannot edit selected file", MessageType.ERROR);
            }
        } else {
            UIMessages.showMessage("Select one file to edit", MessageType.WARNING);
        }
    }

}
