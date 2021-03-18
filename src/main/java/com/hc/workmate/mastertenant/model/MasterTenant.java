package com.hc.workmate.mastertenant.model;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "master_tenant")
public class MasterTenant implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_client_id")
    private Long tenantClientId;

    @Size(max = 50)
    @Column(name = "db_name",nullable = false)
    private String dbName;

    @Size(max = 100)
    @Column(name = "url",nullable = false)
    private String url;

    @Size(max = 50)
    @Column(name = "username",nullable = false)
    private String username;
    @Size(max = 100)
    @Column(name = "password",nullable = false)
    private String password;
    @Size(max = 100)
    @Column(name = "driver_class",nullable = false)
    private String driverClass;
    @Size(max = 10)
    @Column(name = "status",nullable = false)
    private String status;

    public MasterTenant() {
    }

    public MasterTenant(@Size(max = 50) String dbName, @Size(max = 100) String url, @Size(max = 50) String username, @Size(max = 100) String password, @Size(max = 100) String driverClass, @Size(max = 10) String status) {
        this.dbName = dbName;
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClass = driverClass;
        this.status = status;
    }

    public Long getTenantClientId() {
        return tenantClientId;
    }

    public MasterTenant setTenantClientId(Long tenantClientId) {
        this.tenantClientId = tenantClientId;
        return this;
    }

    public String getDbName() {
        return dbName;
    }

    public MasterTenant setDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public MasterTenant setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public MasterTenant setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public MasterTenant setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public MasterTenant setDriverClass(String driverClass) {
        this.driverClass = driverClass;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public MasterTenant setStatus(String status) {
        this.status = status;
        return this;
    }
}
