package pl.pszczolkowski.kanban.web.restapi.account;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import pl.pszczolkowski.kanban.domain.user.bo.UserBO;
import pl.pszczolkowski.kanban.domain.user.finder.UserSnapshotFinder;
import pl.pszczolkowski.kanban.domain.user.snapshot.UserSnapshot;

@RestController
@RequestMapping("/account")
public class AccountApi {

	private final UserSnapshotFinder userSnapshotFinder;
	private final UserBO userBO;
	private final Validator userRegisterValidator;

	@Autowired
	public AccountApi(UserSnapshotFinder userSnapshotFinder, UserBO userBO,
			@Qualifier("accountRegisterValidator") Validator userRegisterValidator) {
		this.userSnapshotFinder = userSnapshotFinder;
		this.userBO = userBO;
		this.userRegisterValidator = userRegisterValidator;
	}

	@InitBinder("accountRegister")
	protected void initNewBinder(WebDataBinder binder) {
		binder.setValidator(userRegisterValidator);
	}

	private UserSnapshot getLoggedUserSnapshot() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		return userSnapshotFinder.findByLogin(username);
	}

	@ApiOperation(
		value = "Register new account", 
		notes = "Returns registered account data")
	@ApiResponses({ @ApiResponse(code = 200, message = "Account registered") })
	@RequestMapping(
		value = "/register", 
		method = RequestMethod.POST, 
		consumes = APPLICATION_JSON_VALUE)
	public HttpEntity<Account> register(@Valid @RequestBody AccountRegister accountRegister) {
		UserSnapshot userSnapshot = userBO.add(accountRegister.getLogin(), accountRegister.getPassword(), accountRegister.getUsername());
		
		return ResponseEntity
				.ok()
				.body(new Account(userSnapshot));
	}

	@ApiOperation(
		value = "Get account data for logged user", 
		notes = "Returns account data for logged user")
	@ApiResponses({ @ApiResponse(code = 200, message = "Found account data for logged user") })
	@RequestMapping(method = GET)
	public HttpEntity<Account> get() {
		UserSnapshot userSnapshot = getLoggedUserSnapshot();
		return ResponseEntity
				.ok()
				.body(new Account(userSnapshot));
	}

}
