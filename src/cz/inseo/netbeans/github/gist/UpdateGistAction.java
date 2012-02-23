package cz.inseo.netbeans.github.gist;

import cz.inseo.netbeans.github.gist.util.GistUtils;
import cz.inseo.netbeans.github.tools.InfoDialog;
import java.awt.Frame;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.actions.EditAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.windows.WindowManager;

@ActionID(category = "Edit",
id = "cz.inseo.netbeans.github.gist.UpdateGistAction")
//@ActionRegistration(iconBase = "cz/inseo/netbeans/github/resources/images/update.png",
//displayName = "#CTL_UpdateGistAction")
@ActionReferences({
	@ActionReference(path = "Editors/Popup", position = 4041),
        @ActionReference(path = "Menu/Edit", position = 1485),
        @ActionReference(path = "Toolbars/Clipboard", position = 500)
})
@Messages("CTL_UpdateGistAction=Update Gist")
public final class UpdateGistAction extends EditAction{
        private static final long serialVersionUID = 1L;
        
        @Override
        protected void initialize() {
                super.initialize();
        }

        @Override
        protected int mode() {
                return MODE_EXACTLY_ONE;
        }

        @Override
        protected Class<?>[] cookieClasses() {
                return new Class[]{EditorCookie.class};
        }

        @Override
        protected void performAction(Node[] activatedNodes) {
                if(!GistUtils.isLogin()){
                        InfoDialog.showInfo(NbBundle.getMessage(CreateGistAction.class, "MSG_LoginNG"));
                        return;
                }
                
                EditorCookie cookie = activatedNodes[0].getLookup().lookup(EditorCookie.class);

                // TODO bug : can't run action from Toolbar,
                JTextComponent component = cookie.getOpenedPanes()[0];
                

                final String mimeType = NbEditorUtilities.getMimeType(component);
                final String displayName = activatedNodes[0].getDisplayName();
                FileObject fileObject = NbEditorUtilities.getFileObject(component.getDocument());
                final String gistId = fileObject.getParent().getPath();
                
                if(gistId == null || gistId.equals("") || !GistUtils.isGistId(gistId)){
                        InfoDialog.showInfo(NbBundle.getMessage(CreateGistAction.class, "MSG_NotGistFile"));
                        return;
                }
                
                String t = component.getSelectedText();
                if (t == null) {
                        t = component.getText();
                }
                if (t.equals("") || t == null){
                        InfoDialog.showError(NbBundle.getMessage(CreateGistAction.class, "MSG_NoData"));
                        return;
                }
                final String text = t;
                if (text != null && !text.isEmpty()) {
                        RequestProcessor.getDefault().execute(new Runnable() {

                                @Override
                                public void run() {

                                        SwingUtilities.invokeLater(new Runnable() {

                                                @Override
                                                public void run() {

                                                        Frame f = WindowManager.getDefault().getMainWindow();
                                                        int x = f.getX() + f.getWidth() / 2;
                                                        int y = f.getY() + f.getHeight() / 2;
                                                        UpdateGistDialog dialog = new UpdateGistDialog(f, true);
                                                        dialog.setLocation(x, y);

                                                        dialog.setFile(displayName, text, mimeType, gistId);

                                                        dialog.setVisible(true);
                                                }
                                        });
                                }
                        });
                }
        }

        @Override
        public String getName() {
                return NbBundle.getMessage(UpdateGistAction.class, "UpdateGistDialog.title");
        }

        @Override
        public HelpCtx getHelpCtx() {
                return HelpCtx.DEFAULT_HELP;
        }
        

}
