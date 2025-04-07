package com.skcc.ra.bap.repository;


import com.skcc.ra.account.domain.auth.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Integer> {

}
