package com.jisu.tenantmanager;

public class AccountData {
    String id;
    String password;

    public AccountData() {
        //does not define a no-argument constructor. If you are using ProGuard, make sure these constructors are not stripped.
    }

    public AccountData(String id, String password) {
        this.id = id;
        this.password = password;
    }
}
