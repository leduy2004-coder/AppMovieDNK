require('dotenv').config();
const cloudier = require('cloudinary').v2;

// Cấu hình Cloudinary
cloudier.config({
  cloud_name: process.env.CLOUDINARY_CLOUD_NAME,
  api_key: process.env.CLOUDINARY_API_KEY,
  api_secret: process.env.CLOUDINARY_API_SECRET,
});



// Hàm upload một ảnh
const uploadImage = (imageBuffer) => {
    return new Promise((resolve, reject) => {
        const opts = {
            overwrite: true,
            invalidate: true,
            resource_type: "auto",
          };

        // Sử dụng upload_stream để upload buffer lên Cloudinary
        cloudier.uploader.upload_stream(opts, (error, result) => {
            if (result && result.secure_url) {
                console.log('Uploaded Image URL:', result.secure_url);
                return resolve({
                    secure_url: result.secure_url,
                    public_id: result.public_id,
                });
            }
            console.error('Upload Error:', error?.message);
            return reject({ message: error?.message || 'Upload failed' });
        }).end(imageBuffer); // Kết thúc luồng upload với buffer
    });
};


// Hàm upload nhiều ảnh
const uploadMultipleImages = (images) => {
  return Promise.all(images.map((base) => uploadImage(base)))
    .then((results) => results)
    .catch((error) => {
      throw error;
    });
};

// Hàm xóa ảnh
const deleteImage = (publicUrl) => {
  console.log('34234225252')
  const publicId = publicUrl.split('/').pop().split('.')[0];
  return new Promise((resolve, reject) => {
    cloudier.uploader.destroy(publicId, (error, result) => {
      if (error) {
        console.error('Error deleting image:', error?.message);
        return reject({ message: error?.message || 'Delete failed' });
      }
      console.log('Deleted Image Result:', result);
      return resolve(result); // Trả về kết quả xóa
    });
  });
};

// Xuất các hàm
module.exports = { uploadImage, uploadMultipleImages, deleteImage };
