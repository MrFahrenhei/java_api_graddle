package com.genesis.application.repository.impl;

import com.genesis.application.db.Database;
import com.genesis.application.model.User;
import com.genesis.application.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public List<User> findAll() {
        return Database.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(Database.findById(id));
    }

    @Override
    public User save(User user) {
        return Database.save(user);
    }

    @Override
    public boolean deleteById(int id) {
        return Database.delete(id);
    }
}
