package sg.edu.nus.iss.demoapp.demo.service;

import java.util.List;

// import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;
import sg.edu.nus.iss.demoapp.demo.model.Member;

public interface MemberService {
    List<Member> findAll();
	
	Member findById(Long id);
 
    Member create(Member e);
 
    Member update(Member e);
 
    Long delete(Long id);
}
