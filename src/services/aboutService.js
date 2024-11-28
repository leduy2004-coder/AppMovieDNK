import * as callPath from '../utils/httpRequest';

export const updateAbout = async (id, formData) => {
    try {
        console.log(id);
        const res = await callPath.patch(`/customer/update/${id}`, formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
        return res.data;
    } catch (err) {
        return { errCode: err.response.status };
    }
};