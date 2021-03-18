package com.hc.workmate.controller;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.hc.workmate.mastertenant.config.DBContextHolder;
import com.hc.workmate.mastertenant.model.MasterTenant;
import com.hc.workmate.mastertenant.service.MasterTenantService;
import com.hc.workmate.security.UserTenantInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hc.workmate.tenant.model.ERole;
import com.hc.workmate.tenant.model.Role;
import com.hc.workmate.tenant.model.User;
import com.hc.workmate.payload.request.LoginRequest;
import com.hc.workmate.payload.request.SignupRequest;
import com.hc.workmate.payload.response.JwtResponse;
import com.hc.workmate.payload.response.MessageResponse;
import com.hc.workmate.tenant.repository.RoleRepository;
import com.hc.workmate.tenant.repository.UserRepository;
import com.hc.workmate.security.jwt.JwtUtils;
import com.hc.workmate.security.service.impl.UserDetailsImpl;
import org.springframework.web.context.annotation.ApplicationScope;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	private Map<String, String> mapValue = new HashMap<>();
	private Map<String, String> userDbMap = new HashMap<>();

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	MasterTenantService masterTenantService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		LOGGER.info("authenticateUser() method call...");
		if(null == loginRequest.getUsername() || loginRequest.getUsername().isEmpty()){
			return ResponseEntity.badRequest().body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), "User name is required"));
		}
		//set database parameter
		MasterTenant masterTenant = masterTenantService.findByClientId(loginRequest.getTenantOrClientId());
		if(null == masterTenant || "INACTIVE".equals(masterTenant.getStatus().toUpperCase())){
			throw new RuntimeException("Please contact service provider.");
		}
		//Entry Client Wise value dbName store into bean.
		loadCurrentDatabaseInstance(masterTenant.getDbName(), loginRequest.getUsername());
		LOGGER.info("STAND HERE0");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername());
		String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());
		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		LOGGER.info("STAND HERE1");
		//Map the value into applicationScope bean
		setMetaDataAfterLogin();
		LOGGER.info("STAND HERE2");
		return ResponseEntity.ok(new JwtResponse(accessToken,
												 refreshToken,
												 jwtUtils.getJwtAtExpirationMs(),
												 jwtUtils.getJwtRtExpirationMs(),
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	private void loadCurrentDatabaseInstance(String databaseName, String username) {
		LOGGER.info("loadCurrentDatabaseInstance() method call...");
		LOGGER.info("DB NAME: " + databaseName);
		DBContextHolder.setCurrentDb(databaseName);
		mapValue.put(username, databaseName);
	}

	@Bean(name = "userTenantInfo")
	@ApplicationScope
	public UserTenantInformation setMetaDataAfterLogin() {
		UserTenantInformation tenantInformation = new UserTenantInformation();
		if (mapValue.size() > 0) {
			for (String key : mapValue.keySet()) {
				if (null == userDbMap.get(key)) {
					//Here Assign putAll due to all time one come.
					userDbMap.putAll(mapValue);
				} else {
					userDbMap.put(key, mapValue.get(key));
				}
			}
			mapValue = new HashMap<>();
		}
		tenantInformation.setMap(userDbMap);
		return tenantInformation;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(HttpStatus.BAD_REQUEST.value(), "Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()),
							 System.currentTimeMillis());

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse(HttpStatus.CREATED.value(), "User registered successfully!"));
	}
}