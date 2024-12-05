import * as callPath from '../utils/httpRequest';

export const updateAbout = async (formData) => {
    try {
        console.log(formData)
        const res = await callPath.patch(`/account/update-admin`, formData, {});
        return res.data;
    } catch (err) {
        return { errCode: err.response.status };
    }
};