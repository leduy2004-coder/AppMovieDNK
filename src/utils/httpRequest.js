import axios from 'axios';

// Tạo instance của Axios
const httpRequest = axios.create({
    baseURL: process.env.REACT_APP_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// GET
export const get = async (path, options = {}) => {
    const response = await httpRequest.get(path, options);
    return response.data;
};

// POST
export const post = async (path, data, options = {}) => {
    const response = await httpRequest.post(path, data, options);
    return response;
};

// PATCH
export const patch = async (path, data, options = {}) => {
    const response = await httpRequest.patch(path, data, options);
    return response;
};

// DELETE
export const deleted = async (path, options = {}) => {
    const response = await httpRequest.delete(path, options);
    return response;
};

export default httpRequest;
