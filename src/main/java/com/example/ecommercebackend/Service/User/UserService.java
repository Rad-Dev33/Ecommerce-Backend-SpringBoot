package com.example.ecommercebackend.Service.User;

import com.example.ecommercebackend.Dto.UserDto;
import com.example.ecommercebackend.Exceptions.ResourceNotFoundException;
import com.example.ecommercebackend.Model.Role;
import com.example.ecommercebackend.Model.User;
import com.example.ecommercebackend.Repository.Role.RoleRepository;
import com.example.ecommercebackend.Repository.UserRepository.UserRepository;
import com.example.ecommercebackend.Requests.CreateUserRequest;
import com.example.ecommercebackend.Requests.UpdateUserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
    }


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {

        return Optional.of(request)
                .filter(user->!userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(new BCryptPasswordEncoder().encode( request.getPassword()));
                    user.setFirstname(request.getFirstname());
                    user.setLastname(request.getLastname());

                    User user1=userRepository.save(user);

                    Set<Role> roles =  user1.getRoles();
                    roles.add( roleRepository.findByRolename("ROLE_USER"));
                    //roles.add( roleRepository.findByRolename("ROLE_ADMIN"));

                    return userRepository.save(user1);



                }).orElseThrow(()->new ResourceNotFoundException(request.getEmail()+ " already exist"));
    }

    @Override
    public User UpdateUser(UpdateUserRequest request, Long userId) {
        User user=getUserById(userId);
        if(request.getFirstname()!=null)user.setFirstname(request.getFirstname());
        if(request.getLastname()!=null)user.setLastname(request.getLastname());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
       userRepository.findById(userId).ifPresentOrElse(userRepository::delete,
               ()->{throw new ResourceNotFoundException("User not found");});
    }

    @Override
    public UserDto convertUsertoDto(User user) {
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    }

    public User findUserByEmail(String username){
        return userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    public void validateUserCart(String email,Long cartId){
        if(!userRepository.existsByEmailAndCartId(email,cartId))throw new ResourceNotFoundException("User Cannot Change Other Resource except His Own");
    }
}
