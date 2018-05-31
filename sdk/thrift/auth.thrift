namespace java cn.tangxinyao.thrift.sdk.auth

include "common.thrift"

struct TSignupRequest {
    1: string username;
    2: string password;
    3: list<string> roles;
}

struct TLoginRequest {
    1: string username;
    2: string password;
}

struct TAuthResponse {
    1: i32 id;
    2: string username;
    3: list<common.TRole> roles;
}

service TAccountService {
    TAuthResponse signup(1: string traceId, 2: TSignupRequest signupRequest);
    TAuthResponse login(1: string traceId, 2: TLoginRequest loginRequest);
    TAuthResponse loadByUsername(1: string traceId, 2: string username);
    list<common.TModule> getModulesByRoleNames(1: string traceId, 2: list<string> roleNames);
}