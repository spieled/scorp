package org.studease.scorp.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.studease.scorp.dataaccess.dao.UserDao;
import org.studease.scorp.dataaccess.entity.User;

/**
 * Author: liushaoping
 * Date: 2015/5/12.
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional
    public void addUser(User user) {
        userDao.save(user);
    }

    @Transactional
    public void deleteUser(int id) {
        userDao.delete(id);
    }

    public User findUserById(int id) {
        return userDao.get(id);
    }

}
