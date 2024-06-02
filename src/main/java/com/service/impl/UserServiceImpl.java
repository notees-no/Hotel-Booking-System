package com.service.impl;

import com.entity.User;
import com.repository.UserRepository;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User read(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<User> read() {
        return userRepository.findAll();
    }
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void edit(User entity) {
        User user = userRepository.findById(entity.getId()).orElseThrow(IllegalArgumentException::new);
        user.setUsername(entity.getUsername());
        user.setPassword(entity.getPassword());
        userRepository.save(user);
    }
    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }
    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

}