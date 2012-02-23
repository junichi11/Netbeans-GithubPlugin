package cz.inseo.netbeans.github.gist.util;

import cz.inseo.netbeans.github.GitHubAuth;
import java.io.IOException;
import org.eclipse.egit.github.core.client.RequestException;

/**
 *
 * @author junichi11
 */
public class GistUtils {
        private GistUtils(){
        }
        
        public static boolean isGistId(String gistId){
                try {
                        GitHubAuth.getGistService(true).getGist(gistId);
                } catch (RequestException ex){
                        return false;
                } catch (IOException ex) {
                        return false;
                }
                return true;
        }
        
        public static boolean isLogin(){
                try {
                        GitHubAuth.tryLogin();
                }catch (IllegalArgumentException ex){
                        return false;
                } catch (IOException ex) {
                        return false;
                }
                return true;
        }
}
