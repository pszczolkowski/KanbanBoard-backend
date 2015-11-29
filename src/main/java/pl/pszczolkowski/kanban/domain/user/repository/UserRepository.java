package pl.pszczolkowski.kanban.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.pszczolkowski.kanban.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findOneByLoginIgnoreCase(String login);

}
