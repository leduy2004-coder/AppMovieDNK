import * as callPath from '../utils/httpRequest';

export const getAllSchedule = async () => {
    try {
        const res = await callPath.get('/schedule/getAll', {});

        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};


export const getAllShift = async (date) => {
    try {
        const res = await callPath.get(`/schedule/getAllShift`, {
            params: { date },
        });
        return res;
    } catch (err) {
        console.error('Lỗi khi gọi API getAvailableShifts:', err);
        return { errCode: err.response?.status || 500, message: err.message };
    }
};


export const getAllRoom = async (date, shift) => {
    try {

        const res = await callPath.get(`/schedule/getAllRoom`, {
            params: { date, shift },
        });
        return res;
    } catch (err) {
        console.error('Lỗi khi gọi API getAvailableRooms:', err);
        return { errCode: err.response?.status || 500, message: err.message };
    }
};


export const insertSchedule = async (scheduleDetails) => {
    try {
        const res = await callPath.post('/schedule/insert-schedule', scheduleDetails, {});
        console.log(res.data)
        return res.data;
    } catch (err) {
        console.error('Lỗi khi gọi API getAvailableRooms:', err);
        // Xử lý lỗi và trả về mã lỗi (nếu có)
        return { errCode: err.response?.status || 500, message: err.message };
    }
};


export const removeSchedule = async (maSuat) => {
    try {
        const res = await callPath.deleted(`/schedule/delete/${maSuat}`, {});

        return res.data;
    } catch (err) {
        return { errorCode: err.response.status };
    }
};
