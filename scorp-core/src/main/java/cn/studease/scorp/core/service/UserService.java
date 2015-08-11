package cn.studease.scorp.core.service;

import cn.studease.scorp.dataaccess.dao.UserDao;
import cn.studease.scorp.dataaccess.entity.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author: liushaoping
 * Date: 2015/5/12.
 */
//@Service
public class UserService {

    //@Autowired
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
