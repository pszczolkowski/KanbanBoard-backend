package pl.pszczolkowski.kanban.service.user;

public interface PasswordEncodingService {

	String encode(String rawPassword);

	boolean isMatch(String passwordToCheck, String encodedPassword);

}
