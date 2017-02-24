package yan.tools;

import com.intellij.codeInsight.navigation.NavigationUtil;
import com.intellij.navigation.GotoRelatedItem;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

/**
 * generate code from template
 * Created by yan on 2017/2/24.
 */
public class GenerateCode extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        String message = "Good Good Learn , Day Day Up !";
        VirtualFile currentFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        Project project = e.getData(PlatformDataKeys.PROJECT);

//        if (isTargetFile(currentFile.getCanonicalPath())){
//            List<GotoRelatedItem> items = findRelatedSymbols(currentFile,project);
//            if (items.isEmpty()){
//                message="Sorry,nothing related found!";
//            }else{
//                NavigationUtil.getRelatedItemsPopup(items, "Choose Target").showInBestPositionFor(e.getDataContext());
//                return;
//            }
//        }else {
//            message = "Hi,this tools is for grails related files of domain,controller and views,\nyour current file looks like not the target file.";
//        }

        if (!message.equals("")){
            Messages.showMessageDialog(project, message, "Information", Messages.getInformationIcon());
        }
    }
}
