package com.revature.controllers;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.revature.dto.ProfileDTO;
import com.revature.models.Profile;
import com.revature.services.ProfileServiceImpl;
import com.revature.utilites.SecurityUtil;
import lombok.extern.log4j.Log4j2;


@Log4j2
@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class ProfileControllerTest {
	 private static final String USERNAME = "dummyUsername";
	 private static final String PASSWORD = "abc123";
	 private static final String EMAIL = "dummy@email.com";
	 private static final boolean VERIFICATION = true;
	 private static Profile expected = new Profile();

	 private static final String USERNAME2 = "dummyUsername2";
	 private static final String PASSWORD2 = "abc123";
	 private static final String EMAIL2 = "dummy2@email.com";
	 private static Profile expected2 = new Profile();
	 
	 private static final String TOKEN_NAME = "Authorization";
	
	 private ProfileDTO profiledto;
	 private ProfileDTO testprofiledto;
	 
	 @Mock
	 private ProfileServiceImpl profileService;

	 @InjectMocks
	 ProfileController profileController;

	 @BeforeEach
	 void initMock() {
	      MockitoAnnotations.openMocks(this);
	      String name = "dummyName";
	      String passkey = "c8ZLBnfDh3YsvZ2dW1KDWY6ZTak8+v+/L74e8Vfoydk1IfySsVCAZVKTZfrtPKodzUXEiR+69yjOz1qqf7U4rA==jnW" +
	                "2sIxW7inUlQqGJCNrNa7Eavj5uMGQAYZ0S6xNz65p79QaOk8eZpOChJlFPvIadohhOuHg5PFGeewM2YmkVR260YPhJwK/GUR3YXs" +
	                "UH0+KjOQKuHAHY8CyLwpBsNV0DsPO56jn2As1CfBMJc9VSNsx37W4Vo5MCaOTisZwBWjUsTG9i+HecqcK9C0tIWC2Jn44oX5BAS3" +
	                "1Huev/ZIWf2OE3FjHSMLyJs0TmJAPxBg6IllwEhv75+ffGbZENZVkkHV3LRxUWKtGdQa5tSOt6pdzIZtdSO6o81zXD6BBDfEZo22" +
	                "qCVCR2C2DQbsmaMJhInHwxEZ3RS3/1N+rz85STq/h+nKvqBcoPOFfq0o5tRRnOlRVFpWuONhiY7IUSqtTZCJmqHnALSRFJwMGFPj" +
	                "mUkq1nQxux1rgxllacVb+AT4bS+Xbw8DyUwGepmTCiw4t75krGyCSBArcmfiFBtgDkVZTFIJ+GMFhFbpWv2darLcxKlgSdur/z9Y" +
	                "CYoZcKm9vrrH+CaFykfIUdjnln5jhLoRmjeBIHgYWITG5J5/NCzAM+a3k4Y92/hbgDDE15GD1ud1EU8GHY4eb5LU1pAb2O7zbcW9" +
	                "pQbtVcbqyJGNRFA6OAGcWb1R0+04d0+1DA6BjTDsxkltgsvUpLrVFBo4VaFAT6Jf4ZI2Pg39WjFY1an8=";

	       expected = new Profile(USERNAME, passkey, name, name, EMAIL, VERIFICATION);
	       String name2 = "dummyName2";
	       String passkey2 = "c8ZLBnfDh3YsvZ2dW1KDWY6ZTak8+v+/L74e8Vfoydk1IfySsVCAZVKTZfrtPKodzUXEiR+69yjOz1qqf7U4rA==jnW"
	                +
	                "2sIxW7inUlQqGJCNrNa7Eavj5uMGQAYZ0S6xNz65p79QaOk8eZpOChJlFPvIadohhOuHg5PFGeewM2YmkVR260YPhJwK/GUR3YXs" +
	                "UH0+KjOQKuHAHY8CyLwpBsNV0DsPO56jn2As1CfBMJc9VSNsx37W4Vo5MCaOTisZwBWjUsTG9i+HecqcK9C0tIWC2Jn44oX5BAS3" +
	                "1Huev/ZIWf2OE3FjHSMLyJs0TmJAPxBg6IllwEhv75+ffGbZENZVkkHV3LRxUWKtGdQa5tSOt6pdzIZtdSO6o81zXD6BBDfEZo22" +
	                "qCVCR2C2DQbsmaMJhInHwxEZ3RS3/1N+rz85STq/h+nKvqBcoPOFfq0o5tRRnOlRVFpWuONhiY7IUSqtTZCJmqHnALSRFJwMGFPj" +
	                "mUkq1nQxux1rgxllacVb+AT4bS+Xbw8DyUwGepmTCiw4t75krGyCSBArcmfiFBtgDkVZTFIJ+GMFhFbpWv2darLcxKlgSdur/z9Y" +
	                "CYoZcKm9vrrH+CaFykfIUdjnln5jhLoRmjeBIHgYWITG5J5/NCzAM+a3k4Y92/hbgDDE15GD1ud1EU8GHY4eb5LU1pAb2O7zbcW9" +
	                "pQbtVcbqyJGNRFA6OAGcWb1R0+04d0+1DA6BjTDsxkltgsvUpLrVFBo4VaFAT6Jf4ZI2Pg39WjFY1an8=";
	       expected2 = new Profile(USERNAME2, passkey2, name2, name2, EMAIL2, VERIFICATION);
	       profiledto = new ProfileDTO();
	       profiledto.setUsername(USERNAME);
	       profiledto.setPasskey(passkey);
	       profiledto.setFirstName(name);
	       profiledto.setLastName(name);
	       profiledto.setEmail(EMAIL);
	       profiledto.setVerification(VERIFICATION);
	       
	       testprofiledto = new ProfileDTO();
	       testprofiledto.setUsername(USERNAME2);
	       testprofiledto.setPasskey(passkey2);
	       testprofiledto.setFirstName(name2);
	       testprofiledto.setLastName(name2);
	       testprofiledto.setEmail(EMAIL2);
	       testprofiledto.setVerification(VERIFICATION);
	  }
	 
	 @BeforeEach
	 private void initEachMock() {
		 MockitoAnnotations.openMocks(this);
	 }
	 
	 private void generateHeaders() {
		  Profile newProfile = profiledto.toProfile();
		  HttpHeaders responseHeaders = new HttpHeaders();
         String token = SecurityUtil.generateToken(new ProfileDTO(newProfile));
         responseHeaders.set(TOKEN_NAME, token);
	 }
	 
	  @Test
	  public void testRegister() throws Exception {
		  when(profileService.getProfileByEmail(any(Profile.class))).thenReturn(expected);
		  ResponseEntity<ProfileDTO> responseEntity = profileController.addNewProfile(profiledto);
		  assertNotNull(responseEntity);
		  assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.IM_USED.value());
	  }
	 
	  @Test
	  public void testLogin() {
		  when(profileService.login(any(String.class),any(String.class) )).thenReturn(expected);
		  ResponseEntity<ProfileDTO> responseEntity = profileController.login(USERNAME, PASSWORD);
		  assertNotNull(responseEntity);
		  assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.OK.value());
	  }
	  
	  @Test
	  public void testGetProfile() {
		  when(profileService.getProfileByPid(any(Integer.class))).thenReturn(expected);
		  ResponseEntity<ProfileDTO> responseEntity = profileController.getProfileByPid(1);
		  assertNotNull(responseEntity);
		  assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.ACCEPTED.value());
	  }
	  
	  @Test
	  public void testUpdateProfile() {
		  when(profileService.updateProfile(any(Profile.class))).thenReturn(expected2);
		  ResponseEntity<ProfileDTO> responseEntity = profileController.updateProfile(testprofiledto);
		  assertEquals(responseEntity.getBody().toProfile().getFirstName(), expected2.getFirstName());
		  assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.ACCEPTED.value()); 
	  }
	  
	  @Test
	  public void testFollow() {
		  HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
		  when(profileService.addFollowerByEmail(any(Profile.class), any(String.class))).thenReturn(expected);
		  ResponseEntity<String> responseEntity = profileController.newFollower(expected.getEmail(), 1,  req);
		  assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.ACCEPTED.value()); 
	  }
	  
	  @Test
	  public void testUnfollow() {
		  HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
		  when(req.getAttribute(any(String.class))).thenReturn(expected);
		  when(profileService.getProfileByEmail(any(Profile.class))).thenReturn(expected);
		  when(profileService.removeFollowByEmail(any(Profile.class), any(String.class))).thenReturn(expected);
		  ResponseEntity<String> responseEntity = profileController.unfollow(expected.getEmail(), req);
		  assertThat(responseEntity.getStatusCodeValue()).isEqualTo(HttpStatus.ACCEPTED.value()); 
	  }
	  
	  @Test
	  public void testGetAll() {
		  List<Profile> profileList = Arrays.asList(expected, expected2);
		  int pageRequested = 1;
		  
	      when(profileService.getAllProfilesPaginated(any(Integer.class))).thenReturn(profileList);
	      ResponseEntity<List<ProfileDTO>> list = profileController.getAllPostsbyPage(pageRequested);
	      List<Profile> profiles = list.getBody().stream().map(pdto -> pdto.toProfile() ).collect(Collectors.toList());
	      for(int index = 0; index<2;index++){
	    	  assertEquals(profiles.get(index).getPid(), profileList.get(index).getPid());
	      }
	  }
	  
	  @Test
	  public void testSearch() {
		  List<Profile> profileList = Arrays.asList(expected, expected2);
		  String query = "dummy";
	      when(profileService.search(any(String.class))).thenReturn(profileList);
	      ResponseEntity<List<ProfileDTO>> searchList = profileController.search(query);
	      List<Profile> profiles = searchList.getBody().stream().map(pdto -> pdto.toProfile() ).collect(Collectors.toList());
	      for(int index = 0; index<2;index++){
	    	  assertEquals(profiles.get(index).getPid(), profileList.get(index).getPid());
	      }
	  }
	  
	  @Test
	  public void testGetFollowing() { 
		 ResponseEntity<List<ProfileDTO>> followingList = profileController.getFollowers(1);
		 List<Profile> actualProfileList = followingList.getBody().stream().map(profileDTO -> profileDTO.toProfile() ).collect(Collectors.toList());
		 actualProfileList.forEach(profile -> System.out.println(profile));
	     assertTrue(actualProfileList.isEmpty());
	  }
}
