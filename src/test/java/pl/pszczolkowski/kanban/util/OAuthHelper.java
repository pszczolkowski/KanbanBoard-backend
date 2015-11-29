package pl.pszczolkowski.kanban.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

/**
 * Used to append Bearer Token to mocked request.
 */
@Component
public class OAuthHelper implements ApplicationContextAware {
	
	private static ClientDetailsService clientDetailsService;
	private static AuthorizationServerTokenServices tokenService;
	private static String clientId;

	public static RequestPostProcessor bearerTokenForUser(UserSnapshot userSnapshot) {
		return mockRequest -> {
			OAuth2AccessToken token = createAccessTokenForUser(userSnapshot);
			mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
			return mockRequest;
		};
	}

	private static OAuth2AccessToken createAccessTokenForUser(UserSnapshot userSnapshot) {
		// Look up authorities, resourceIds and scopes based on clientId
		ClientDetails client = clientDetailsService.loadClientByClientId(clientId);
		Collection<GrantedAuthority> authorities = client.getAuthorities();
		Set<String> resourceIds = client.getResourceIds();
		Set<String> scopes = client.getScope();

		// Default values for other parameters
		Map<String, String> requestParameters = Collections.emptyMap();
		boolean approved = true;
		String redirectUrl = null;
		Set<String> responseTypes = Collections.emptySet();
		Map<String, Serializable> extensionProperties = Collections.emptyMap();

		// Create request
		OAuth2Request oAuth2Request = new OAuth2Request(requestParameters, clientId, authorities, approved, scopes,
				resourceIds, redirectUrl, responseTypes, extensionProperties);

		// Create OAuth2AccessToken
		User userPrincipal = new User(userSnapshot.getLogin(), "", true, true, true, true, authorities);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal,
				null, authorities);
		OAuth2Authentication auth = new OAuth2Authentication(oAuth2Request, authenticationToken);
		return tokenService.createAccessToken(auth);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		clientDetailsService = applicationContext.getBean(ClientDetailsService.class);
		tokenService = applicationContext.getBean(AuthorizationServerTokenServices.class);
		clientId = applicationContext.getEnvironment().getProperty("oauth2.clientId");
	}
}
