package controller;
/**
 * Created by admin on 2018/2/2.
 */
import com.jfinal.core.Controller;

public class IndexController extends Controller {
	public static boolean loginActive = false;

	public boolean getLoginActive() {
		return loginActive;
	}

	public static void setLoginActive(boolean loginActive) {
		IndexController.loginActive = loginActive;
	}

	public void index() {
		if (loginActive) {
			setLoginActive(false);
			render("index.html");
		} else {
//			render("login.html");
			redirect("/login");
		}
	}

//	public void login(){
//		System.out.println();
//	}
}
