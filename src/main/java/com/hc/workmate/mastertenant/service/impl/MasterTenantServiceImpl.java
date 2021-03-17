package com.hc.workmate.mastertenant.service.impl;

import com.hc.workmate.mastertenant.entity.MasterTenant;
import com.hc.workmate.mastertenant.repository.MasterTenantRepository;
import com.hc.workmate.mastertenant.service.MasterTenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MasterTenantServiceImpl implements MasterTenantService {

    private static final Logger LOG = LoggerFactory.getLogger(MasterTenantServiceImpl.class);

    @Autowired
    MasterTenantRepository masterTenantRepository;

    @Override
    public MasterTenant findByClientId(Long clientId) {
        LOG.info("findByClientId() method call...");
        return masterTenantRepository.findByTenantClientId(clientId);
    }
}
