// routes/movies.js
const express = require('express');
const axios = require('axios');
const router = express.Router();
const sql = require('mssql');
const { connectToDatabase } = require('../config/dbConfig');

// Route để lấy danh sách bình luận và thông tin khách hàng qua `maPhim`
router.get('/:maPhim', async (req, res) => {
    const { maPhim } = req.params;
    try {
        const pool = await connectToDatabase();
        console.log('23425325346536')
        // Lấy danh sách bình luận từ database với `maPhim`
        const result = await pool.request()
            .input('maPhim', sql.NVarChar, maPhim)
            .query('SELECT * FROM BinhLuan WHERE maPhim = @maPhim');

        const comments = result.recordset;

        if (comments.length > 0) {
            // Gọi hàm fetchCommentsWithUserInfo để lấy thông tin khách hàng
            const commentsWithUserInfo = await fetchCommentsWithUserInfo(comments);
            console.log('00000000')
            console.log('Fetched users:', commentsWithUserInfo);
            res.json(commentsWithUserInfo); // Trả về danh sách bình luận kèm thông tin khách hàng
        } else {
            res.status(404).send('Không tìm thấy bình luận cho phim này.');
        }
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu bình luận:', err);
        res.status(500).send('Lỗi server.');
    }
});

const fetchCommentsWithUserInfo = async (comments) => {
    try {
        // Lấy danh sách `maKH` từ các bình luận (loại bỏ trùng lặp)
        const uniqueCustomerIds = [...new Set(comments.map((comment) => comment.maKH))];
        
        // Gọi fetchUser cho từng maKH
        const users = await Promise.all(uniqueCustomerIds.map((maKH) => fetchUser(maKH)));

        // Ghép thông tin khách hàng với bình luận
        const response = comments.map((comment) => {
            const user = users.find((u) => u && u.maKH === comment.maKH);
            return {
                ...comment,
                khachHang: user || null, // Nếu không tìm thấy user, gán null
            };
        });

        return response; // Trả về bình luận với thông tin khách hàng
    } catch (err) {
        console.error('Lỗi khi xử lý bình luận và thông tin khách hàng:', err);
        throw new Error('Lỗi server.');
    }
};


// API để chèn comment
router.post('/send', async (req, res) => {
    const { maPhim, khachHang, noiDung } = req.body; // Lấy thông tin từ body

    const { maKH } = khachHang; // Lấy mã khách hàng từ đối tượng khachHang
    

    // Kiểm tra xem có đủ thông tin không
    if (!maPhim || !maKH || !noiDung) {
        return res.status(400).json({ error: 'Thiếu thông tin maPhim, maKH hoặc noiDung.' });
    }

    try {
        let pool = await connectToDatabase();

        const result = await pool.request()
            .input('maPhim', sql.VarChar(20), maPhim) 
            .input('maKH', sql.VarChar(20), maKH) 
            .input('noiDung', sql.NVarChar, noiDung) 
            .input('gio', sql.DateTime, new Date()) // Thời gian hiện tại
            .query(`
                INSERT INTO BinhLuan (maPhim, maKH, gio, noiDung, tinhTrang) 
                VALUES (@maPhim, @maKH, @gio, @noiDung, 1);
                
                SELECT SCOPE_IDENTITY() AS maBinhLuan; 
            `);

        // Lấy maBinhLuan từ kết quả truy vấn
        const maBinhLuan = result.recordset[0].maBinhLuan;

        const comment = await fetchComment(maBinhLuan);

        res.status(200).json({
            message: 'Chèn bình luận thành công!',
            comment: comment // Trả về BinhLuan của bình luận mới tạo
        });
    } catch (err) {
        // Xử lý lỗi nếu có
        res.status(500).json({ error: 'Có lỗi xảy ra khi thực hiện chèn bình luận.' });
        console.error("Error: ", err);
    }
});

// Route để lấy thông tin comment qua maBinhLuan
const fetchComment = async (maBinhLuan) => {
    try {
        const pool = await connectToDatabase(); 

  
        const result = await pool.request()
            .input('maBinhLuan', sql.Int, maBinhLuan)
            .query('SELECT * FROM BinhLuan WHERE maBinhLuan = @maBinhLuan');

        // Kiểm tra kết quả
        if (result.recordset.length > 0) {
            return result.recordset[0]; 
        } else {
            return null; 
        }
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu bình luận:', err);
        throw new Error('Lỗi server.'); // Ném lỗi để xử lý bên ngoài
    }
};

const fetchUser = async (maKH) => {
    try {

        const pool = await connectToDatabase(); 

        // Truy vấn thông tin khách hàng
        const result = await pool.request()
            .input('maKH', sql.NVarChar, maKH)
            .query('SELECT maKH, hoTen FROM KhachHang WHERE maKH = @maKH');

        // Kiểm tra kết quả
        if (result.recordset.length > 0) {
            return result.recordset[0]; // Trả về thông tin khách hàng đầu tiên
        } else {
            return null; // Không tìm thấy khách hàng
        }
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu khách hàng:', err);
        throw new Error('Lỗi server.'); // Ném lỗi để xử lý bên ngoài
    }
};

module.exports = router;
