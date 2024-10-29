// routes/movies.js
const express = require('express');
const axios = require('axios');
const router = express.Router();
const sql = require('mssql');
const config = require('../config/dbConfig'); // Import cấu hình
const axiosRetry = require('axios-retry');


// Cấu hình retry và timeout cho axios để xử lý lỗi và gián đoạn kết nối
axios.defaults.timeout = 5000; // Đặt timeout 5 giây cho mỗi yêu cầu
axios.defaults.retry = 3;
axios.defaults.retryDelay = 1000; // Thử lại sau 1 giây nếu gặp lỗi

axios.interceptors.response.use(null, async (error) => {
    const config = error.config;
    if (!config || config._retry) return Promise.reject(error);

    config._retry = true;
    return axios(config);
});

// Hàm để lấy chi tiết của từng phim với retry và timeout
const fetchMovieDetails = async (movies) => {
    const movieDetailsPromises = movies.map(async (movie) => {
        try {
            const movieResponse = await axios.get(`http://localhost:3000/movies/phim/${movie.maPhim}`);
            return movieResponse.data;
        } catch (err) {
            console.error(`Lỗi khi lấy thông tin phim ${movie.maPhim}:`, err.message);
            return null; // Trả về null nếu có lỗi
        }
    });

    return await Promise.all(movieDetailsPromises);
};

const fetchMovieDetailsSequential = async (movies) => {
    const movieDetails = [];
    for (const movie of movies) {
        try {
            const movieResponse = await axios.get(`http://localhost:3000/movies/phim/${movie.maPhim}`);
            movieDetails.push(movieResponse.data);
        } catch (err) {
            console.error(`Lỗi khi lấy thông tin phim ${movie.maPhim}:`, err.message);
        }
    }
    return movieDetails;
};
// Route để lấy những phim đang chiếu
router.get('/phimdangchieu', async (req, res) => {
    let pool;

    try {
        pool = await sql.connect(config);

        // Lấy danh sách maPhim từ stored procedure GetSuatChieu
        let result = await pool.request().execute('GetSuatChieu');
        console.log(result.recordset);

        // Lấy thông tin chi tiết của các phim
        const movieDetails = await fetchMovieDetailsSequential(result.recordset);

        // Trả về danh sách thông tin chi tiết của các phim
        res.json(movieDetails.filter(movie => movie !== null));
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu phim đang chiếu:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu phim đang chiếu');
    } finally {
        if (pool) {
            await sql.close();
        }
    }
});

// Route để lấy những phim chưa chiếu
router.get('/phimchuachieu', async (req, res) => {
    let pool;

    try {
        pool = await sql.connect(config);

        // Lấy danh sách maPhim từ stored procedure GetSuatChuaChieu
        let result = await pool.request().execute('GetSuatChuaChieu');
        console.log(result.recordset);

        // Lấy thông tin chi tiết của các phim
        const movieDetails = await fetchMovieDetails(result.recordset);

        // Trả về danh sách thông tin chi tiết của các phim
        res.json(movieDetails.filter(movie => movie !== null));
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu phim chưa chiếu:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu phim chưa chiếu');
    } finally {
        if (pool) {
            await sql.close();
        }
    }
});


// Route để lấy thông tin của phim thông qua maPhim
router.get('/phim/:maPhim', async (req, res) => {
    const { maPhim } = req.params; // Lấy mã phim từ URL
    try {
        let pool = await sql.connect(config);
        let result = await pool.request()
            .input('maPhim', sql.NVarChar, maPhim)
            .query('SELECT * FROM Phim WHERE maPhim = @maPhim');
           
        if (result.recordset.length > 0) {
            res.json(result.recordset[0]); // Trả về thông tin chi tiết của phim
        } else {
            res.status(404).send('Phim không tìm thấy');
        }
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu phim:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu phim');
    } finally {
        await sql.close();
    }
});

// Route để lấy ngay chieu cua phim
router.get('/ngaychieu/:maPhim', async (req, res) => {
    const maPhim = req.params.maPhim; // Lấy maPhim từ URL

    try {
        // Kết nối SQL
        let pool = await sql.connect(config);

        // Gọi hàm fXuatNgayChieu với tham số maPhim
        let result = await pool.request()
            .input('id', sql.NVarChar(50), maPhim) // Truyền maPhim vào hàm fXuatNgayChieu
            .query('SELECT * FROM fXuatNgayChieu(@id)'); // Gọi hàm SQL

        const ngayChieuList = result.recordset.map(item => {
            return {
                ngayChieu: item.ngayChieu.toISOString().split('T')[0] // Chỉ lấy ngày tháng năm
            };
        });
        // Trả về dữ liệu dạng JSON
        res.json(ngayChieuList);
    } catch (err) {
        console.error('Lỗi khi lấy dữ liệu ngày chiếu:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu ngày chiếu');
    } finally {
        // Đóng kết nối SQL
        await sql.close();
    }

});

router.get('/get-schedule/:id', async (req, res) => {
    const phimId = req.params.id;
    try {

        let pool = await sql.connect(config)
        // Truy vấn để lấy ngày chiếu
        let result = await pool.request()
            .input('id', sql.NVarChar(50), phimId) // Truyền phimId vào hàm fXuatNgayChieu
            .query('SELECT * FROM fXuatNgayChieu(@id)'); // Gọi hàm SQL

        console.log("result recordst" + result.recordset); // Ghi log kết quả nhận được

        const ngayChieuList = result.recordset.map(item => {
            return {
                maSuat: item.maSuat,
                ngayChieu: item.ngayChieu.toISOString().split('T')[0] // Chỉ lấy ngày tháng năm
            };
        });
        console.log(ngayChieuList);
        // Tạo mảng kết quả
        const schedules = [];

        // Lặp qua từng ngày chiếu để lấy thời gian chiếu
        for (const { ngayChieu, maSuat } of ngayChieuList) { // Không cần truyền maSuat
            const resultThoiGianChieu = await pool.request()
                .input('id', sql.NVarChar, phimId)
                .input('ngayChieu', sql.Date, ngayChieu)
                .input('maSuat', sql.NVarChar(50), maSuat) // Truyền maSuat
                .query(`
                    SELECT DISTINCT thoiGianBatDau, maCa, maSuat
                    FROM dbo.fXuatThoiGianChieu(@id, @ngayChieu)
                `);

            schedules.push({
                ngayChieu: ngayChieu,
                caChieu: resultThoiGianChieu.recordset.map(thoiGian => {
                    return {
                        thoiGianBatDau: thoiGian.thoiGianBatDau.toISOString().split('T')[1].split('.')[0],
                        maCa: thoiGian.maCa,
                        maSuat: thoiGian.maSuat
                    };
                })
            });
            break;
        }
        console.log("ket qua cuoi cung: ");
        console.log(schedules);

        // Trả về kết quả JSON
        res.json(schedules);
    } catch (error) {
        console.error('Lỗi khi truy vấn:', error);
        res.status(500).send('Đã xảy ra lỗi.');
    }
});


router.get('/ticket-details/:maBook', async (req, res) => {
    const maBook = req.params.maBook; // Lấy mã đặt vé từ URL
    try {
        let pool = await sql.connect(config);
        let result = await pool.request()
            .input('maBook', sql.VarChar(20), maBook) // Đặt giá trị cho tham số maBook
            .query('SELECT * FROM fChiTietTicket(@maBook)'); // Gọi hàm fChiTietTicket

        // Kiểm tra nếu không có dữ liệu
        if (result.recordset.length === 0) {
            return res.status(404).json({ message: 'Không tìm thấy thông tin vé' });
        }

        res.json(result.recordset); // Trả về dữ liệu
    } catch (err) {
        console.error('Lỗi khi lấy chi tiết vé:', err);
        res.status(500).send('Lỗi khi lấy dữ liệu chi tiết vé');
    } finally {
        await sql.close(); // Đóng kết nối
    }
});



module.exports = router;
