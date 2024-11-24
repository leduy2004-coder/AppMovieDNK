import * as callPath from '../utils/httpRequest';

export const login = async (tenTK, matKhau) => {
    try {
        const res = await callPath.post('/account/login', {
            tenTK,
            matKhau,
        });
        return res.data;
    } catch (err) {
        return { errCode: err.response.status };
    }
};


export const logout = async (token) => {
    try {
        const res = await callPath.post('auth/logout', {}, token);
        return res;
    } catch (err) {
        return { errorCode: err.response.status };
    }
};


