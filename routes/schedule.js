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

router.post('/insertSchedule', async (req, res) => {
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
        .input('ngayChieu', sql.DateTime, ngayChieu)
        .input('tinhTrang', sql.Int, tinhTrang)
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

  router.post('/updateSchedule/maSuat', async (req, res) => {
    const { maPhim, maPhong, maCa, ngayChieu, tinhTrang } = req.body;
    let pool;
    console.log('Dữ liệu nhận được:', req.body); 
    if (!maPhim || !maPhong || !maCa || !ngayChieu || !tinhTrang ) {
        return res.status(400).json({ message: 'Dữ liệu không đầy đủ, vui lòng kiểm tra lại' });
    }
    try {
        // Kết nối tới cơ sở dữ liệu SQL Server
        pool = await connectToDatabase();
  
        // Thực thi câu lệnh SQL để cập nhật dữ liệu trong bảng 'SuatChieu'
        const result = await pool.request()
            .input('maPhim', sql.NVarChar, maPhim)
            .input('maPhong', sql.NVarChar, maPhong)
            .input('maCa', sql.NVarChar, maCa)
            .input('ngayChieu', sql.DateTime, ngayChieu)
            .input('tinhTrang', sql.NVarChar, tinhTrang)
            .query(`
                UPDATE [dbo].[SuatChieu]
                SET [maPhong] = @maPhong, [maCa] = @maCa, [ngayChieu] = @ngayChieu, [tinhTrang] = @tinhTrang
                WHERE [maPhim] = @maPhim
            `);
  
        res.status(200).json({
            message: 'Cập nhật dữ liệu thành công!',
            data: result.recordset
        });
  
    } catch (error) {
        console.error('Error updating data:', error);
        res.status(500).json({ error: 'Lỗi khi cập nhật dữ liệu' });
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