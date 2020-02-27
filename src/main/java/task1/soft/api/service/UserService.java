package task1.soft.api.service;

import task1.soft.api.entity.User;

import java.util.List;

public interface UserService {

    public void saveCEO();

    void findAllUsers();

    List<User> findAllEmployyesOfDepForHead(Long idDep);
    List<User> findAllEmployyesOfDepForCEO(Long idDep);

    List<User> findAll();

    void save(User employee);

    void saveEmployee();

    void update(User employee, Long id);

    void createRoles();
}
