// routes/MOVIE_TICKET.js
const express = require('express');
const router = express.Router();
const sql = require('mssql');
const config = require('../config/dbConfig'); // Import cấu hình

// Route để lấy thông tin về vé của phim
router.get('/:maPhim', async (req, res) => {
    const { maPhim } = req.params;

        try {
             let pool = await sql.connect(config);

            const result = await pool.request()
                .input('maPhim', sql.VarChar(20), maPhim)
                .query('SELECT * FROM Ve WHERE maphim = @maPhim');

            res.json(result.recordset);
        } catch (error) {
            console.error('Lỗi khi truy vấn:', error);
            res.status(500).send('Có lỗi xảy ra khi truy vấn cơ sở dữ liệu.');
        }
});




// API để chèn bản ghi vào bảng BookVe
router.post('/bookve', async (req, res) => {
    const { maKH, maVe, maSuat, tongTien } = req.body; // Lấy dữ liệu từ request body

    // Kiểm tra xem có đủ thông tin không
    if (!maKH || !maVe || !maSuat || !tongTien) {
        return res.status(400).json({ error: 'Thiếu thông tin maKH, maVe, maSuat hoặc tongTien.' });
    }

    try {
        let pool = await sql.connect(config); // Kết nối đến cơ sở dữ liệu
        const result = await pool.request()
            .input('maKH', sql.VarChar(20), maKH)
            .input('maVe', sql.VarChar(20), maVe)
            .input('maSuat', sql.VarChar(20), maSuat)
            .input('tongTien', sql.Decimal(18, 2), tongTien)
            .query(`
                INSERT INTO [BookVe] ([maKH], [maVe], [maSuat], [tongTien]) 
                VALUES (@maKH, @maVe, @maSuat, @tongTien);

                SELECT SCOPE_IDENTITY() AS maBook; -- Lấy ID của bản ghi mới
            `);

        const maBook = result.recordset[0].maBook; // Lấy maBook từ kết quả truy vấn

        res.status(200).json({
            message: 'Insert thành công!',
            maBook: maBook // Trả về maBook mới tạo
        });

    } catch (err) {
        res.status(500).json({ error: 'Có lỗi xảy ra khi thực hiện insert.' });
        console.error("Error: ", err);
    }
});



//insert ghe da dat
// Route để chèn ghế đã đặt// Route để chèn ghế đã đặt
router.post('/bookghe', async (req, res) => {
    const { maBook, maGhe } = req.body; // Lấy maBook và maGhe từ body

    if (!maBook || !maGhe || !Array.isArray(maGhe) || maGhe.length === 0) {
        return res.status(400).json({ message: 'Thiếu maBook hoặc maGhe.' });
    }

    let pool;

    try {
        pool = await sql.connect(config); // Kết nối với cơ sở dữ liệu

        // Bắt đầu giao dịch
        const transaction = new sql.Transaction(pool);
        await transaction.begin();

        try {
            // Thực hiện từng yêu cầu chèn ghế một cách tuần tự
            for (const ghe of maGhe) {
                await transaction.request()
                    .input('maBook', sql.VarChar(20), maBook)
                    .input('maGhe', sql.VarChar(20), ghe)
                    .query('INSERT INTO BookGhe (maBook, maGhe) VALUES (@maBook, @maGhe)');
            }

            // Cam kết giao dịch
            await transaction.commit();
            res.status(200).json({ message: 'Chèn ghế thành công.' });
        } catch (error) {
            // Hủy bỏ giao dịch trong trường hợp lỗi
            await transaction.rollback();
            console.error('Lỗi khi chèn dữ liệu vào BookGhe:', error.message);
            res.status(500).json({ message: 'Lỗi khi chèn dữ liệu vào BookGhe.', error: error.message });
        }
    } catch (error) {
        console.error('Lỗi khi kết nối:', error.message);
        res.status(500).json({ message: 'Lỗi khi kết nối tới cơ sở dữ liệu.', error: error.message });
    } finally {
        // Đảm bảo đóng kết nối
        if (pool) {
            await pool.close();
        }
    }
});



module.exports = router; // Đảm bảo export router
