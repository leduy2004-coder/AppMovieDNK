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
        pool = await connectToDatabase(); 

        const result = await pool.request()
            .query('SELECT * FROM fn_GetPhimPhongCaSuatChieu()');

        res.json(result.recordset);
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu lịch chiếu phim:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu lịch chiếu phim');
    } 
});

// Route để lấy tất cả ca chiếu còn trống dựa vào ngày
router.get('/getAllShift', async (req, res) => {
    let pool;
    const { date } = req.query;
    try {
        pool = await connectToDatabase();
     
        const result = await pool.request()
        .input('ngayKT', sql.Date, date)
        .query('SELECT * FROM fKTTheoCa(@ngayKT)');

    res.json(result.recordset);
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu ca chiếu:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu ca chiếu');
    }
});

// Route để lấy tất cả Phòng chiếu dựa vào ngày chiếu và ca chiếu
router.get('/getAllRoom', async (req, res) => {
    let pool;

    try {
        pool = await connectToDatabase();
        const { date, shift } = req.query;
        // Kiểm tra nếu thiếu tham số
        if (!date || !shift) {
            return res.status(400).send('Thiếu tham số cần thiết');
        }

        const result = await pool.request()
        .input('ngayKT', sql.Date, date)
        .input('caKT', sql.VarChar, shift)
        .query('SELECT * FROM fPhongChuaChieu(@ngayKT, @caKT)');

        if (result.recordset.length === 0) {
            return res.status(404).send('Hết phòng chiếu');
        }
        res.json(result.recordset); 
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu phòng chiếu trống:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu phòng chiếu trống');
    }
});

router.post('/insert-schedule', async (req, res) => {
    const { maPhim, maPhong, maCa, ngayChieu, tinhTrang } = req.body;
    let pool;
    // Kết nối tới cơ sở dữ liệu SQL Server
    try { 
        pool = await connectToDatabase();
  
      // Thực thi câu lệnh SQL để chèn dữ liệu vào bảng 'SuatChieu'
      const result = await pool.request()
        .input('maPhim', sql.NVarChar, maPhim)
        .input('maPhong', sql.NVarChar, maPhong)
        .input('maCa', sql.NVarChar, maCa)
        .input('ngayChieu', sql.Date, ngayChieu)
        .input('tinhTrang', sql.Bit, tinhTrang)
        .query(`
          INSERT INTO [SuatChieu] ([maPhim], [maPhong], [maCa], [ngayChieu], [tinhTrang])
          VALUES (@maPhim, @maPhong, @maCa, @ngayChieu, @tinhTrang)
        `);
  
      res.status(201).json({
        message: 'Dữ liệu đã được thêm thành công!',
        data: result.recordset
      });
  
    } catch (error) {
      console.error('Error inserting data:', error);
      res.status(500).json({ error: 'Lỗi khi thêm dữ liệu' });
    }
  });

router.delete('/delete/:idSchedule', async (req, res) => {
    const { idSchedule } = req.params;  // Lấy giá trị từ tham số URL
    let pool;

    try {
        pool = await connectToDatabase();

        // Xóa dữ liệu trong bảng 'SuatChieu' với 'maSuat' khớp với idSchedule
        const result = await pool.request()
            .input('maSuat', sql.NVarChar, idSchedule)  // Đảm bảo rằng 'idSchedule' là 'maSuat' trong cơ sở dữ liệu
            .query('DELETE FROM SuatChieu WHERE maSuat = @maSuat');

        // Kiểm tra nếu có dòng bị xóa
        if (result.rowsAffected[0] > 0) {
            res.status(200).send('Xóa suất chiếu thành công');
        } else {
            res.status(404).send('Không tìm thấy suất chiếu');
        }

    } catch (err) {
        console.log('Lỗi khi xóa dữ liệu phim:', err);
        res.status(500).send('Lỗi khi xóa dữ liệu phim');
    }
});



module.exports = router;