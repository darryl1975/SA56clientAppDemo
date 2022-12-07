package sg.edu.nus.iss.demoapp.demo.service;

import java.util.List;

import sg.edu.nus.iss.demoapp.demo.model.Role;

public interface RoleService {
    List<Role> findAll();
	
	Role findById(Long id);
 
    Role create(Role e);
 
    Role update(Role e);
 
    Long delete(Long id);
}
