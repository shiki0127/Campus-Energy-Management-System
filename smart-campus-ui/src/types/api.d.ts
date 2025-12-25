// 1. 通用的后端返回结构 (对应 Java 的 Result<T>)
export interface ApiResponse<T = any> {
    code: string;
    msg: string;
    data: T;
}

// 2. 登录成功后返回的数据 (Token)
export interface LoginResult {
    token: string;
}

// 3. 用户信息 (备用)
export interface UserInfo {
    id: number;
    username: string;
    role: string;
}