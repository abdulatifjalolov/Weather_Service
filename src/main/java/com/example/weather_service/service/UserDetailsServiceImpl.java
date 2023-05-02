package com.example.weather_service.service;

import com.example.weather_service.common.exception.RecordNotFountException;
import com.example.weather_service.dto.request.UserRequestDTO;
import com.example.weather_service.dto.response.UserResponseDTO;
import com.example.weather_service.entity.CityEntity;
import com.example.weather_service.entity.Enum.RoleEnum;
import com.example.weather_service.entity.UserEntity;
import com.example.weather_service.repository.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService, AuditorAware<Long> {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;
    private final CityService cityService;


    public void createUser(UserEntity user) {
        Optional<UserEntity> optionalUserEntity =
                userRepository.findByEmail(user.getEmail());
        if (optionalUserEntity.isPresent())
            throw new IllegalArgumentException(String.format("email %s already exist", user.getEmail()));
        if (isAdmin(user)) {
            throw new IllegalArgumentException("user role cannot be ADMIN");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void subscribeToCity(UserEntity userEntity, Long cityId) {
        CityEntity cityEntity = cityService.get(cityId);
        userEntity = userRepository.findById(userEntity.getId()).get();
        List<CityEntity> cityEntities = userEntity.getCityEntities();
        if (cityEntities == null) {
            userEntity.setCityEntities(List.of(cityEntity));
        }else {
            cityEntities.add(cityEntity);
        }
        userEntity.setCityEntities(cityEntities);
        userRepository.save(userEntity);
    }


    public List<UserEntity> list() {
        return userRepository.findAll();
    }

    public UserEntity updateUser(Long id, UserRequestDTO userRequestDTO) {
        UserEntity data = userRepository.findById(id).orElseThrow(() ->
                new RecordNotFountException(String.format("User not found for %s", id))
        );
        Long originalId = data.getId();
        UserEntity userEntity = UserEntity.of(userRequestDTO);
        userEntity.setCityEntities(data.getCityEntities());
        userEntity.setId(originalId);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    public UserResponseDTO getUserSubscribeCities(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(() ->
                new RecordNotFountException(String.format("User not found for %s", id))
        );
        return new UserResponseDTO(
                user.getEmail(),
                user.getCityEntities()
        );
    }
    public UserResponseDTO getCurrentUserSubscribeCities(Long currentUserId) {
        UserEntity user = userRepository.findById(currentUserId).orElseThrow(() ->
                new RecordNotFountException(String.format("User not found for %s", currentUserId))
        );
        List<CityEntity> visibleCityEntities =
                user.getCityEntities().stream()
                .filter(CityEntity::isVisible)
                .toList();
        return new UserResponseDTO(
                user.getEmail(),
                visibleCityEntities
        );
    }

    private boolean isAdmin(UserEntity userEntity) {
        return userEntity
                .getRoleEnumList().stream()
                .anyMatch((e) -> {
                    return e.name().equals(RoleEnum.ADMIN.name());
                });
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("email {0} not found", email)
                ));
    }

    @Override
    public @NonNull Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserEntity principal))
            return Optional.empty();
        return Optional.of(principal.getId());
    }
}
