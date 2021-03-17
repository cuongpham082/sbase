package com.hc.workmate.mastertenant.service;

import com.hc.workmate.mastertenant.entity.MasterTenant;

public interface MasterTenantService {
    MasterTenant findByClientId(Long clientId);
}
