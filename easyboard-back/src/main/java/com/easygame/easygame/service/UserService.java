package com.easygame.easygame.service;

import com.easygame.easygame.DTO.UserInfoResponse;
import com.easygame.easygame.DTO.auth.DetailsRequest;
import com.easygame.easygame.DTO.auth.UpdateResponse;
import com.easygame.easygame.model.UserModel;
import com.easygame.easygame.model.UsersDetails;
import com.easygame.easygame.repository.UserDetailsRepository;
import com.easygame.easygame.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;
    private final FileService fileService;
    private final UserDetailsRepository detailsRepository;

    /**
     * Сохранение пользователя
     *
     * @return сохраненный пользователь
     */
    public UserModel save(UserModel user) {
        return repository.save(user);
    }

    public UsersDetails saveDetails(UsersDetails usersDetails) {return detailsRepository.save(usersDetails);}

    public List<UserInfoResponse> getUsersByKeyword(String keyword){
        return repository.findUsersByKeyword(keyword).stream()
                .filter(userModel -> !userModel.equals(getCurrentUser()))
                .limit(25)
                .map(UserInfoResponse::new).toList();
    }

    public void setDetails(DetailsRequest detailsRequest){
        var userDetails = getCurrentUser().getUsersDetails();
        userDetails.setStatus(
                detailsRequest.getStatus() != null && !detailsRequest.getStatus().isEmpty()
                        ? detailsRequest.getStatus()
                        : " "
        );

        userDetails.setName(
                detailsRequest.getName() != null && !detailsRequest.getName().isEmpty()
                        ? detailsRequest.getName()
                        : " "
        );

        userDetails.setSurname(
                detailsRequest.getSurname() != null && !detailsRequest.getSurname().isEmpty()
                        ? detailsRequest.getSurname()
                        : " "
        );
        saveDetails(userDetails);
    }


    public void saveAvatar(MultipartFile file){
        try{
            var currentUser = getCurrentUser();
            currentUser.setAvatar(fileService.saveFile(file));
            save(currentUser);
            currentUser.getUsersDetails().setPhotoPath(currentUser.getAvatar());
            saveDetails(currentUser.getUsersDetails());
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * Создание пользователя
     *
     * @return созданный пользователь
     */
    public UserModel create(UserModel user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        logger.info("Пользователь " + user.getUsername() + " успешно зарегистрирован");
        return save(user);
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @return пользователь
     */
    public UserModel getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение пользователя по имени пользователя
     * <p>
     * Нужен для Spring Security
     *
     * @return пользователь
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public boolean isAuthenticated(){
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }


    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public UserModel getCurrentUser() {

        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    /**
     * Получение текущего пользователя по токену
     *
     * @return текущий пользователь
     */
    public UpdateResponse update() {
        UserModel user = getCurrentUser();
        UpdateResponse updateResponse = UpdateResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .enabled(user.isEnabled())
                .build();
        return updateResponse;
    }

}
