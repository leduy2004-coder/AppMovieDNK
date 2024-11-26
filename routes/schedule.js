const express = require('express');
const axios = require('axios');
const router = express.Router();
const sql = require('mssql');
const { connectToDatabase } = require('../config/dbConfig');
const axiosRetry = require('axios-retry');

//lấy dữ liệu lịch chiếu phim
router.get('/getAll', async (req, res) => {
    let pool;

    try {
        pool = await connectToDatabase();  // Kết nối cơ sở dữ liệu

        // Thực hiện truy vấn gọi hàm fn_GetPhimPhongCaSuatChieu()
        const result = await pool.request()
            .query('SELECT * FROM fn_GetPhimPhongCaSuatChieu()');

        res.json(result.recordset); // Trả về kết quả dưới dạng JSON
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu lịch chiếu phim:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu lịch chiếu phim');
    } 
});

// Route để lấy tất cả ca chiếu cont rống dựa vào ngày
router.get('/getAllRoom', async (req, res) => {
    let pool;

    
    try {
        pool = await connectToDatabase();
     

        const result = await pool.request()
        .query('SELECT * FROM PhongChieu');

    res.json(result.recordset); // Trả về kết quả dưới dạng JSON
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu ca chiếu:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu ca chiếu');
    }
});

// Route để lấy tất cả Phong chiếu dựa vào ngày chiếu và ca chiếu
router.get('/getAvailableShifts/:ngayChieu/:maPhong', async (req, res) => {
    let pool;

    try {
        pool = await connectToDatabase();
        const { ngayChieu, maPhong } = req.params;

        // Kiểm tra nếu thiếu tham số
        if (!ngayChieu || !maPhong) {
            return res.status(400).send('Thiếu tham số cần thiết: ngayChieu, maPhong');
        }

        // Query: Lấy danh sách các ca chiếu còn trống
        const result = await pool.request()
            .input('ngayChieu', sql.Date, ngayChieu)
            .input('maPhong', sql.NVarChar, maPhong)
            .query(`
                SELECT maCa, tenCa
                FROM CaChieu
                WHERE maCa NOT IN (
                    SELECT DISTINCT maCa
                    FROM SuatChieu
                    WHERE ngayChieu = @ngayChieu AND maPhong = @maPhong
                )
            `);

        // Kiểm tra nếu không có ca chiếu trống
        if (result.recordset.length === 0) {
            return res.status(404).send('Hết ca chiếu');
        }
        res.json(result.recordset); // Trả về danh sách các ca chiếu còn trống
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu phòng chiếu trống:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu phòng chiếu trống');
    }
});

router.post('/updateSchedule', async (req, res) => {
    const { maVe, maPhim, maNV, tinhTrang, soLuongToiDa, soLuongDaBan, tien } = req.body;
    let pool;

    try {
        pool = await connectToDatabase();

        // Kiểm tra dữ liệu đầu vào
        if (!maVe || !maPhim || !maNV || !tinhTrang || !soLuongToiDa || !soLuongDaBan || !tien) {
            return res.status(400).json({ message: 'Dữ liệu không đầy đủ, vui lòng kiểm tra lại' });
        }

        // Thực hiện câu lệnh UPDATE bảng Ve
        await pool.request()
            .input('maPhim', sql.VarChar(50), maPhim)
            .input('soLuongToiDa', sql.Int, soLuongToiDa)
            .input('soLuongDaBan', sql.Int, soLuongDaBan)
            .query(`
                UPDATE [Cinema_version4].[dbo].[Ve]
                SET 
                    [maPhim] = @maPhim,
                    [soLuongToiDa] = @soLuongToiDa,
                    [soLuongDaBan] = @soLuongDaBan,
                WHERE [maVe] = @maVe
            `);

        // Trả về kết quả thành công
        res.status(200).json({ message: 'Dữ liệu đã được cập nhật thành công!' });

    } catch (err) {
        console.error('Lỗi khi cập nhật dữ liệu:', err);
        res.status(500).json({ message: 'Lỗi khi cập nhật dữ liệu vào cơ sở dữ liệu', error: err.message });
    }
});


module.exports = router;