namespace java cn.tangxinyao.thrift.sdk.common

struct TRole {
    1: i32 id;
    2: string name;
}

struct TModule {
    1: i32 id;
    2: i32 parentId;
    3: i32 level;
    4: string name;
    5: string type;
}