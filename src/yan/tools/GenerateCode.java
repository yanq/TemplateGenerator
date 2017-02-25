package yan.tools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

/**
 * generate code from template
 * Created by yan on 2017/2/24.
 */
public class GenerateCode extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        String message = "Complete !";
        Project project = e.getData(PlatformDataKeys.PROJECT);
        File baseDir = new File(project.getBasePath());

        VirtualFile file = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if (file.exists()){
            File currentFile = new File(file.getCanonicalPath());
            if (Generator.isTargetFile(currentFile)){
                Generator.generateCode(currentFile, baseDir);
            }else {
                message = "Hi,this tools is for java or groovy file please open it first.";
            }
        }else {
            message = "Please choose a java or groovy file first .";
        }


        if (!message.equals("")){
            Messages.showMessageDialog(project, message, "Information", Messages.getInformationIcon());
        }
    }
}
