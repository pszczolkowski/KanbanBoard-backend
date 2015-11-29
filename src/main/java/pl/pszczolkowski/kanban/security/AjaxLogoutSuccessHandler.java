package pl.pszczolkowski.kanban.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
		implements LogoutSuccessHandler {

	public static final String BEARER_AUTHENTICATION = "Bearer ";

	@Autowired
	private TokenStore tokenStore;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		String token = request.getHeader("authorization");
		if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
			final OAuth2AccessToken oAuth2AccessToken = tokenStore
					.readAccessToken(substringAfter(token, BEARER_AUTHENTICATION));

			if (oAuth2AccessToken != null) {
				tokenStore.removeAccessToken(oAuth2AccessToken);
			}
		}

		response.setStatus(HttpServletResponse.SC_OK);
	}

	private static String substringAfter(String str, String separator) {
		if (str.isEmpty()) {
			return null;
		}
		if (separator == null) {
			return null;
		}
		int pos = str.indexOf(separator);
		if (pos == -1) {
			return null;
		}
		return str.substring(pos + separator.length());
	}
}
