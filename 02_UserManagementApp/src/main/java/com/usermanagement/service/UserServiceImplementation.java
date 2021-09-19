package com.usermanagement.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.usermanagement.constants.AppConstants;
import com.usermanagement.entity.City;
import com.usermanagement.entity.Country;
import com.usermanagement.entity.State;
import com.usermanagement.entity.User;
import com.usermanagement.exception.FileMissingException;
import com.usermanagement.exception.PasswordEncryptDecryptException;
import com.usermanagement.exception.UserManagementAppException;
import com.usermanagement.formbinding.LoginForm;
import com.usermanagement.formbinding.RegForm;
import com.usermanagement.formbinding.UnlockAccForm;
import com.usermanagement.properties.AppProperties;
import com.usermanagement.repository.CityRepository;
import com.usermanagement.repository.CountryRepository;
import com.usermanagement.repository.StateRepository;
import com.usermanagement.repository.UserRepository;
import com.usermanagement.util.EmailUtils;
import com.usermanagement.util.PwdUtils;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private StateRepository stateRepo;
	@Autowired
	private CityRepository cityRepo;
	@Autowired
	private AppProperties appProp;
	@Autowired
	private EmailUtils email;
	
	private Logger logger=LoggerFactory.getLogger(UserServiceImplementation.class);

	@Override
	public Map<Integer, String> getCountry() {
		List<Country> countriesList = countryRepo.findAll();
		Map<Integer, String> countryMap = new HashMap<>();
		countriesList.forEach(country -> 
			countryMap.put(country.getCountryId(), country.getCountryName()));
		return countryMap;
	}

	@Override
	public Map<Integer, String> getState(Integer countryId) {
		List<State> stateList = stateRepo.findByCountryId(countryId);
		Map<Integer, String> stateMap = new HashMap<>();
		stateList.forEach(state -> 
			stateMap.put(state.getStateId(), state.getStateName()));
		return stateMap;
	}

	@Override
	public Map<Integer, String> getCity(Integer stateId) {
		List<City> cityList = cityRepo.findByStateId(stateId);
		Map<Integer, String> cityMap = new HashMap<>();
		cityList.forEach(city -> 
			cityMap.put(city.getCityId(), city.getCityName()));
		return cityMap;
	}

	@Override
	public String handleSignInBtn(LoginForm loginForm) throws UserManagementAppException {
		String msg;
		String encryptPwd = PwdUtils.encryptPwd(loginForm.getUserPwd());
		User user = userRepo.findByUserEmailAndUserPwd(loginForm.getUserEmail(), encryptPwd);
		if (user != null) {
			String status = user.getAccStatus();
			if (AppConstants.LOCK.equals(status)) {
				msg = appProp.getMessages().get(AppConstants.ACC_LOCKED);
			} else {
				msg = AppConstants.SUCCESS;
			}
		} else {
			msg = appProp.getMessages().get(AppConstants.INVALID_CRD);
		}
		return msg;
	}

	@Override
	public String checkEmail(String email) {
		String msg;

		User user = new User();
		user.setUserEmail(email.trim());

		Example<User> example = Example.of(user);

		Optional<User> optional = userRepo.findOne(example);

		if (optional.isPresent()) {
			msg = AppConstants.DUPLICATE;
		} else {
			msg = AppConstants.UNIQUE;
		}
		return msg;
	}

	@Override
	public boolean hndlSignUpBtn(RegForm regForm) throws UserManagementAppException {

		User user = new User();

		BeanUtils.copyProperties(regForm, user);

		String pazzword = generateRandomPazzword(6);
		String encryptPwd = PwdUtils.encryptPwd(pazzword);

		user.setAccStatus(AppConstants.LOCK);
		user.setUserPwd(encryptPwd);

		user = userRepo.save(user);
		
		String body = readEmailBodyFile(user);

		boolean sendEmail = email.sendEmail(user.getUserEmail(), 
							appProp.getMessages().get(AppConstants.EMAIL_SUBJECT),body);
		
		if(user.getUserId()!=null && sendEmail) {
			return true;
		}

		return false;
	}

	@Override
	public boolean unlckAccFnctnlty(UnlockAccForm unloclAccForm) throws UserManagementAppException {
		String temPassword = unloclAccForm.getTemPassword();
		String encryptPwd = PwdUtils.encryptPwd(temPassword);
		
		User user = userRepo.findByUserEmailAndUserPwd
						(unloclAccForm.getUserEmail(), encryptPwd);
		if (user != null) {
			String pwd = PwdUtils.encryptPwd(unloclAccForm.getNewPassword());
			user.setUserPwd(pwd);
			user.setAccStatus(AppConstants.UNLOCK);
			userRepo.save(user);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean frgtPwdFuctnlty(String useremail) throws UserManagementAppException {
		Optional<User> optional = getUserByEmail(useremail);
		if (optional.isPresent()) {
			User userEntity = optional.get();
			String body = rdFrgtPwdBdyFl(userEntity);
			boolean sendEmail = email.sendEmail(userEntity.getUserEmail(),
					appProp.getMessages().get(AppConstants.FORGOT_PAZZWORD_EMAIL_SUBJECT), body);
			if(sendEmail) {
				return true;
			}
		} 
			return false;
	}

	private Optional<User> getUserByEmail(String email) {
		User user = new User();
		user.setUserEmail(email.trim());
		Example<User> example = Example.of(user);
		return userRepo.findOne(example);
	}

	private static String generateRandomPazzword(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(AppConstants.CANDIDATE_CHARS
					.charAt(random.nextInt(AppConstants.CANDIDATE_CHARS.length())));
		}
		return sb.toString();
	}

	private String readEmailBodyFile(User user) throws FileMissingException {
		String body = AppConstants.EMPTY_STRING;
		StringBuilder sb = new StringBuilder();
		try (FileReader path = new FileReader(appProp.getMessages()
				.get(AppConstants.EMAIL_BODY_FILE)))
		{
			body = emailBodyFile(user, sb, path);
		} catch (Exception e) {
			logger.error("File is Missing {}",new FileMissingException(e.getMessage()),e);
		}
		return body;
	}

	private String emailBodyFile(User user, StringBuilder sb, FileReader path)
			throws IOException, PasswordEncryptDecryptException {
		String body;
		BufferedReader br = new BufferedReader(path);
		String line = br.readLine();
		while (line != null) {
			sb.append(line + "\n");
			line = br.readLine();
		}
		br.close();
		body = sb.toString();
		String decryptPwd = PwdUtils.decryptPwd(user.getUserPwd());
		body = body.replace(AppConstants.FIRST_NAME, user.getFirstName());
		body = body.replace(AppConstants.LAST_NAME, user.getLastName());
		body = body.replace(AppConstants.PAZZWORD, decryptPwd);
		body = body.replace(AppConstants.EMAIL, user.getUserEmail());
		return body;
	}

	private String rdFrgtPwdBdyFl(User user) throws UserManagementAppException {
		String body = AppConstants.EMPTY_STRING;
		StringBuilder sb = new StringBuilder();
		try (FileReader path = new FileReader(appProp.getMessages()
				.get(AppConstants.FORGOT_PAZZWORD_BODY)))
		{
			body = emailBodyFile(user, sb, path);

		} catch (IOException e) {
			logger.error("File is Missing {}",new FileMissingException(e.getMessage()),e);
		}
		return body;
	}

}
