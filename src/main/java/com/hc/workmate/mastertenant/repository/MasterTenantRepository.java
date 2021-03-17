package com.hc.workmate.mastertenant.repository;

import com.hc.workmate.mastertenant.entity.MasterTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> {
    MasterTenant findByTenantClientId(Long clientId);
}
