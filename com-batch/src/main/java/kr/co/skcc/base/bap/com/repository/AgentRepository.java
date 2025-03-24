package kr.co.skcc.base.bap.com.repository;


import kr.co.skcc.base.com.account.domain.auth.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Integer> {

}
