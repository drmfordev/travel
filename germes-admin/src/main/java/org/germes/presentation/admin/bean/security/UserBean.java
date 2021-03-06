package org.germes.presentation.admin.bean.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import java.io.IOException;
import java.io.Serializable;

@Named
@ViewScoped
@Getter
@Setter
/**
 * JSF-managed bean to store user credentials for authentication
 * @author Morenets
 *
 */
public class UserBean implements Serializable {
	private static final Logger log = LoggerFactory.getLogger(UserBean.class);

	private String username;

	private String password;

	public void login() {
		Subject subject = SecurityUtils.getSubject();

		UsernamePasswordToken token = new UsernamePasswordToken(getUsername(), getPassword());

		try {
			subject.login(token);

			FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");

		} catch (UnknownAccountException ex) {
			facesError("Unknown account");
			log.error(ex.getMessage(), ex);
		} catch (IncorrectCredentialsException ex) {
			facesError("Wrong password");
			log.error(ex.getMessage(), ex);
		} catch (LockedAccountException ex) {
			facesError("Locked account");
			log.error(ex.getMessage(), ex);
		} catch (AuthenticationException | IOException ex) {
			facesError("Unknown error: " + ex.getMessage());
			log.error(ex.getMessage(), ex);
		} finally {
			token.clear();
		}
	}

	private void facesError(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
	}
}
