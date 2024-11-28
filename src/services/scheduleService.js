import * as callPath from '../utils/httpRequest';

export const getAllSchedule = async () => {
    try {
        const res = await callPath.get('/schedule/getAll', {});

        return res;
    } catch (err) {
        return { errCode: err.response.status };
    }
};


export const getAllRoom = async () => {
    try {
        // Gọi API với các tham số được truyền vào
        const res = await callPath.get(`/schedule/getAllRoom`, {});
        return res; // Trả về dữ liệu từ API
    } catch (err) {
        console.error('Lỗi khi gọi API getAvailableShifts:', err);
        // Xử lý lỗi và trả về mã lỗi (nếu có)
        return { errCode: err.response?.status || 500, message: err.message };
    }
};


export const getAvailableShifts = async (ngayChieu, maPhong) => {
    try {
        // Gọi API với các tham số được truyền vào
        const res = await callPath.get(`/schedule/getAvailableShifts/${ngayChieu}/${maPhong}`);
        return res; // Trả về dữ liệu từ API
    } catch (err) {
        console.error('Lỗi khi gọi API getAvailableRooms:', err);
        // Xử lý lỗi và trả về mã lỗi (nếu có)
        return { errCode: err.response?.status || 500, message: err.message };
    }
};


export const insertSchedule = async (scheduleDetails) => {
    try {
        const res = await callPath.post('/schedule/insertSchedule', scheduleDetails, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
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
