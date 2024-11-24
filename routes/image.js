const express = require('express');
const router = express.Router();
const sql = require('mssql');
const { connectToDatabase } = require('../config/dbConfig');
const multer = require('multer');
const fs = require('fs');
const { uploadImage, uploadMultipleImages } = require('../utils/uploadImage');

// Cấu hình Multer để xử lý upload file từ client
const upload = multer();

// Route để upload 1 ảnh
router.post('/upload', upload.single('image'), async (req, res) => {
    try {
        const result = await uploadImage(req.file.buffer); 

        res.status(200).json({ message: 'Upload ảnh thành công', data: result });
    } catch (error) {
        console.error('Lỗi khi upload ảnh:', error);
        res.status(500).json({ message: 'Image upload failed', error });
    }
});

module.exports = router;
