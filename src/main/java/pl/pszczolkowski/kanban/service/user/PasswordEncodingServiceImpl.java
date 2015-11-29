package pl.pszczolkowski.kanban.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncodingServiceImpl implements PasswordEncodingService {

	private final BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public PasswordEncodingServiceImpl(BCryptPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String encode(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	@Override
	public boolean isMatch(String passwordToCheck, String rawPassword) {
		return passwordEncoder.matches(passwordToCheck, rawPassword);
	}
}
